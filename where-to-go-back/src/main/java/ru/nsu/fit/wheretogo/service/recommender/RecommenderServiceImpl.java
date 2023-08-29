package ru.nsu.fit.wheretogo.service.recommender;

import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.user.StayPointDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.entity.score.Score;
import ru.nsu.fit.wheretogo.entity.user.User;
import ru.nsu.fit.wheretogo.entity.user.coefficient.main.UserCoefficientMain;
import ru.nsu.fit.wheretogo.recommenders.cbf.CBFRecommender;
import ru.nsu.fit.wheretogo.recommenders.cbf.UserVectorBuilder;
import ru.nsu.fit.wheretogo.recommenders.cf.SlopeOne;
import ru.nsu.fit.wheretogo.repository.place.CategoryRepository;
import ru.nsu.fit.wheretogo.repository.place.PlaceRepository;
import ru.nsu.fit.wheretogo.repository.score.ScoreRepository;
import ru.nsu.fit.wheretogo.repository.user.UserRepository;
import ru.nsu.fit.wheretogo.repository.user.coefficient.UserCoefficientMainRepository;
import ru.nsu.fit.wheretogo.utils.SecurityContextHelper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class RecommenderServiceImpl implements RecommenderService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final UserCoefficientMainRepository userCoefficientMainRepository;
    private final CategoryRepository categoryRepository;
    private final ScoreRepository scoreRepository;

    public RecommenderServiceImpl(
            PlaceRepository placeRepository,
            UserRepository userRepository,
            UserCoefficientMainRepository userCoefficientMainRepository,
            CategoryRepository categoryRepository,
            ScoreRepository scoreRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
        this.userCoefficientMainRepository = userCoefficientMainRepository;
        this.categoryRepository = categoryRepository;
        this.scoreRepository = scoreRepository;
    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public List<PlaceBriefDTO> getContentBasedRecommendations() {
        List<Category> categoryList = categoryRepository.findAll();

        List<UserCoefficientMain> userCoefficientMains = userCoefficientMainRepository
                .getAllByUserId(userRepository.findByEmail(SecurityContextHelper.email())
                        .orElseThrow().getId());
        Map<Category, Double> userVector = UserVectorBuilder.getUserVector(userCoefficientMains, categoryList);

        List<Place> notVisitedPlacesPage = placeRepository.findNotVisitedByUser(
                userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getId());

        return CBFRecommender.getRecommendations(
                categoryList,
                userVector,
                notVisitedPlacesPage);
    }

    @Override
    @Transactional
    public List<PlaceBriefDTO> getCollaborativeRecommendationsByScores() {
        var user = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow();
        List<Place> placeList = placeRepository.findNotVisitedByUser(user.getId());
        Map<User, HashMap<Place, Double>> data = new HashMap<>();

        prepareCollaborativeDataFromScoreList(data, scoreRepository.findAll());

        Map<User, HashMap<Place, Double>> projectedData = SlopeOne.slopeOne(data, placeList);

        return new ArrayList<>(
                (projectedData.get(user)).keySet().stream().map(PlaceBriefDTO::getFromEntity).toList());
    }

    @Override
    @Transactional
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

        Map<User, HashMap<Place, Double>> projectedData = SlopeOne.slopeOne(data, placeList);

        return new ArrayList<>(
                (projectedData.get(user)).keySet().stream().map(PlaceBriefDTO::getFromEntity).toList());
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
}
