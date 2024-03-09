package ru.nsu.fit.wheretogo.service.recommender;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.user.StayPointDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.user.User;
import ru.nsu.fit.wheretogo.entity.user.coefficient.UserCoefficient;
import ru.nsu.fit.wheretogo.model.ors.ORSDirectionResponse;
import ru.nsu.fit.wheretogo.model.ors.direction.LatLong;
import ru.nsu.fit.wheretogo.model.recommender.RouteRecommenderRequest;
import ru.nsu.fit.wheretogo.model.recommender.RouteRecommenderResponse;
import ru.nsu.fit.wheretogo.recommenders.cbf.CBFRecommender;
import ru.nsu.fit.wheretogo.recommenders.cbf.UserVectorBuilder;
import ru.nsu.fit.wheretogo.recommenders.cf.SlopeOneRecommender;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.GeneticAlgorithmRecommender;
import ru.nsu.fit.wheretogo.repository.place.CategoryRepository;
import ru.nsu.fit.wheretogo.repository.place.PlaceRepository;
import ru.nsu.fit.wheretogo.repository.score.ScoreRepository;
import ru.nsu.fit.wheretogo.repository.user.UserRepository;
import ru.nsu.fit.wheretogo.repository.user.coefficient.UserCoefficientRepository;
import ru.nsu.fit.wheretogo.service.openroute.OpenRouteService;
import ru.nsu.fit.wheretogo.utils.LatLongSequences;
import ru.nsu.fit.wheretogo.utils.helpers.SecurityContextHelper;
import ru.nsu.fit.wheretogo.utils.helpers.nearest.search.NearestSearchHelper;
import ru.nsu.fit.wheretogo.utils.stub.RouteData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RecommenderServiceImpl implements RecommenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommenderServiceImpl.class);

    private final PlaceRepository placeRepository;
    private final OpenRouteService openRouteService;
    private final UserRepository userRepository;
    private final UserCoefficientRepository userCoefficientRepository;
    private final CategoryRepository categoryRepository;
    private final ScoreRepository scoreRepository;
    private final GeneticAlgorithmRecommender geneticAlgorithmRecommender;

    private final NearestSearchHelper nearestSearchHelper;

    @Override
    public List<PlaceBriefDTO> getNearestPlacesByCategory(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit,
            String categoryIds
    ) {
        List<Place> places = placeRepository.findNearestByCategory(
                myLat,
                myLon,
                startDist,
                maxDist,
                limit,
                categoryIds
        );

        return places
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .toList();
    }

    @Override
    public List<PlaceBriefDTO> getNearestPlacesByStayPoints() {
        List<StayPointDTO> stayPoints = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getStayPoints().stream().map(
                StayPointDTO::getFromEntity).toList();

        List<PlaceBriefDTO> visitedPlaces = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getVisitedPlaces().stream().map(
                PlaceBriefDTO::getFromEntity).toList();
        String isolators;
        List<PlaceBriefDTO> recommendations = new ArrayList<>();

        if (visitedPlaces.isEmpty()) {
            for (StayPointDTO stayPoint : stayPoints) {
                List<Place> currentStayPointPlaces = placeRepository.findNearestPlaces(
                        stayPoint.getLatitude(),
                        stayPoint.getLongitude(),
                        1.0,
                        5.0,
                        2
                );

                List<PlaceBriefDTO> currentPlaceRecommendations = currentStayPointPlaces
                        .stream()
                        .map(PlaceBriefDTO::getFromEntity).toList();

                recommendations.addAll(currentPlaceRecommendations);
                recommendations = recommendations.stream().distinct().collect(toList());
            }
        } else {
            List<Long> isolatorsVisited = new ArrayList<>();
            for (PlaceBriefDTO place : visitedPlaces) {
                isolatorsVisited.add(place.getId());
            }

            isolators = isolatorsVisited.stream().map(String::valueOf).collect(Collectors.joining(","));

            for (StayPointDTO stayPoint : stayPoints) {
                List<Place> currentStayPointPlaces = placeRepository.findNearestByVisited(
                        stayPoint.getLatitude(),
                        stayPoint.getLongitude(),
                        1.0,
                        5.0,
                        2,
                        isolators
                );

                isolators = updateIsolators(recommendations, isolatorsVisited, currentStayPointPlaces);
            }
        }

        return recommendations;
    }

    @Override
    public List<PlaceBriefDTO> getNearestPlacesByVisited() {
        List<PlaceBriefDTO> visitedPlaces = userRepository.findByEmail(SecurityContextHelper.email())
                .orElseThrow().getVisitedPlaces().stream().map(
                        PlaceBriefDTO::getFromEntity).toList();

        List<Long> isolatorIds = new ArrayList<>();
        for (PlaceBriefDTO place : visitedPlaces) {
            isolatorIds.add(place.getId());
        }

        String isolators = isolatorIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        List<PlaceBriefDTO> recommendations = new ArrayList<>();

        for (PlaceBriefDTO place : visitedPlaces) {
            var placeCoordinates = place.getCoordinates();
            List<Place> currentVisitedPlaces = placeRepository.findNearestByVisited(
                    placeCoordinates.getLatitude().doubleValue(),
                    placeCoordinates.getLongitude().doubleValue(),
                    1.0,
                    5.0,
                    2,
                    isolators
            );

            isolators = updateIsolators(recommendations, isolatorIds, currentVisitedPlaces);
        }

        return recommendations;
    }

    @Override
    public List<PlaceBriefDTO> getContentBasedRecommendations() {
        List<Category> categoryList = categoryRepository.findAll();

        List<UserCoefficient> userCoefficients = userCoefficientRepository
                .getAllByUserId(userRepository.findByEmail(SecurityContextHelper.email())
                        .orElseThrow().getId());
        Map<Category, Double> userVector = UserVectorBuilder.getUserVector(userCoefficients, categoryList);

        List<Place> notVisitedPlacesPage = placeRepository.findNotVisitedByUser(
                userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getId());

        return CBFRecommender.getRecommendations(
                categoryList,
                userVector,
                notVisitedPlacesPage);
    }

    @Override
    public List<PlaceBriefDTO> getCollaborativeRecommendationsByScores() {
        var user = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow();
        List<Place> placeList = placeRepository.findNotVisitedByUser(user.getId());
        Map<User, HashMap<Place, Double>> data = new HashMap<>();

        prepareCollaborativeDataFromScoreList(data, scoreRepository.findAll());

        Map<User, HashMap<Place, Double>> projectedData = SlopeOneRecommender.slopeOne(data, placeList);

        return new ArrayList<>(
                (projectedData.get(user)).keySet().stream().map(PlaceBriefDTO::getFromEntity).toList());
    }

    @Override
    public List<PlaceBriefDTO> getCollaborativeRecommendationsByFavourites() {
        var user = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow();
        List<Place> placeList = placeRepository.findNotVisitedByUser(user.getId());
        Map<User, HashMap<Place, Double>> data = new HashMap<>();
        List<Score> scoreList = scoreRepository.findAll();

        List<Place> currUserFavourPlaces = user.getFavouritePlaces();

        for (Place favourPlace : currUserFavourPlaces) {
            scoreList.add(new Score().setPlace(favourPlace).setUser(user).setScoreValue(5));
        }

        prepareCollaborativeDataFromScoreList(data, scoreList);

        Map<User, HashMap<Place, Double>> projectedData = SlopeOneRecommender.slopeOne(data, placeList);

        return new ArrayList<>(
                (projectedData.get(user)).keySet().stream().map(PlaceBriefDTO::getFromEntity).toList());
    }

    @Override
    public RouteRecommenderResponse getRouteRecommendation(RouteRecommenderRequest request) {

        LOGGER.debug("Получен запрос на получение пути-рекомендации:{}", request);

        var geneticAlgResponse = geneticAlgorithmRecommender.execute(request);

        LOGGER.debug("Результат работы алгоритма:{}", geneticAlgResponse);

        if (geneticAlgResponse != null && !geneticAlgResponse.getRoutePlaces().isEmpty()) {
            return geneticAlgResponse;
        } else {
            LOGGER.debug("Путь-рекомендация не найден. Отображение пути-рекомендации по умолчанию.");
            var keyPoints = RouteData.getData();

            ORSDirectionResponse orsResponse = null;

            switch (request.getMode()) {
                case WALKING -> orsResponse = openRouteService.getDirectionWalking(keyPoints);
                case DRIVING -> orsResponse = openRouteService.getDirectionDriving(keyPoints);
            }

            var coordinateSequences = getCoordinateSequences(keyPoints);

            LOGGER.debug("Последовательности координат: Широты:[{}] Долготы:[{}]",
                    coordinateSequences.getLatitudes(),
                    coordinateSequences.getLongitudes());

            var keyPlacesResponse = placeRepository.findAllByLatLong(
                    coordinateSequences.getLatitudes(),
                    coordinateSequences.getLongitudes());

            return RouteRecommenderResponse
                    .builder()
                    .routePlaces(keyPlacesResponse.stream().map(PlaceBriefDTO::getFromEntity).toList())
                    .direction(orsResponse)
                    .build();
        }
    }

    private void prepareCollaborativeDataFromScoreList(Map<User, HashMap<Place, Double>> data, List<Score> scoreList) {
        for (Score score : scoreList) {
            if (data.containsKey(score.getUser())) {
                if (!data.get(score.getUser()).containsKey(score.getPlace())) {
                    data.get(score.getUser()).put(score.getPlace(), (double) score.getScoreValue());
                }
            } else {
                HashMap<Place, Double> userRating = new HashMap<>();
                userRating.put(score.getPlace(), (double) score.getScoreValue());
                data.put(score.getUser(), userRating);
            }
        }
    }

    private String updateIsolators(
            List<PlaceBriefDTO> recommendations,
            List<Long> isolatorsVisited,
            List<Place> currentStayPointPlaces) {
        List<PlaceBriefDTO> currentPlaceRecommendations = currentStayPointPlaces
                .stream()
                .map(PlaceBriefDTO::getFromEntity).toList();

        recommendations.addAll(currentPlaceRecommendations);

        for (PlaceBriefDTO recPlace : currentPlaceRecommendations) {
            isolatorsVisited.add(recPlace.getId());
        }
        return isolatorsVisited.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private LatLongSequences getCoordinateSequences(List<LatLong> keyPoints) {
        var latSequence = new StringBuilder();
        var lonSequence = new StringBuilder();

        for (var pointCoordinates : keyPoints) {
            latSequence.append(pointCoordinates.getLatitude()).append(",");
            lonSequence.append(pointCoordinates.getLongitude()).append(",");
        }

        latSequence.deleteCharAt(latSequence.lastIndexOf(","));
        lonSequence.deleteCharAt(lonSequence.lastIndexOf(","));

        return LatLongSequences
                .builder()
                .latitudes(latSequence.toString())
                .longitudes(lonSequence.toString())
                .build();
    }
}
