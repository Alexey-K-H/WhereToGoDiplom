package ru.nsu.fit.wheretogo.service.recommender;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
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
    public PagedListDTO<PlaceBriefDTO> getNearestPlacesByCategory(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit,
            String categoryIds,
            int page,
            int size
    ) {
        var pageRequest = PageRequest.of(page, size);
        Page<Place> places = placeRepository.findNearestByCategory(
                myLat,
                myLon,
                startDist,
                maxDist,
                limit,
                categoryIds,
                pageRequest
        );
        List<PlaceBriefDTO> nearestPlaceDtos = places.toList()
                .stream()
                .map(PlaceBriefDTO::getFromEntity)
                .toList();

        var listDto = new PagedListDTO<PlaceBriefDTO>();
        listDto.setList(nearestPlaceDtos);
        listDto.setPageNum(page);
        listDto.setTotalPages(places.getTotalPages());

        return listDto;
    }

    @Override
    @Transactional
    public PagedListDTO<PlaceBriefDTO> getNearestPlacesByStayPoints(
            int page,
            int size
    ) {
        List<StayPointDTO> stayPoints = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getStayPoints().stream().map(
                StayPointDTO::getFromEntity).toList();
        var pageRequest = PageRequest.of(page, size);

        List<PlaceBriefDTO> visitedPlaces = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getVisitedPlaces().stream().map(
                PlaceBriefDTO::getFromEntity).toList();
        String isolators;

        List<PlaceBriefDTO> recommendations = new ArrayList<>();
        var totalPages = 0;

        if (visitedPlaces.isEmpty()) {
            for (StayPointDTO stayPoint : stayPoints) {
                Page<Place> currentStayPointPlaces = placeRepository.findNearestPlaces(
                        stayPoint.getLatitude(),
                        stayPoint.getLongitude(),
                        1.0,
                        5.0,
                        2,
                        pageRequest
                );

                totalPages += currentStayPointPlaces.getTotalPages();

                List<PlaceBriefDTO> currentPlaceRecommendations = currentStayPointPlaces.toList()
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
                Page<Place> currentStayPointPlaces = placeRepository.findNearestByVisited(
                        stayPoint.getLatitude(),
                        stayPoint.getLongitude(),
                        1.0,
                        5.0,
                        2,
                        isolators,
                        pageRequest
                );

                totalPages += currentStayPointPlaces.getTotalPages();

                List<PlaceBriefDTO> currentPlaceRecommendations = currentStayPointPlaces.toList()
                        .stream()
                        .map(PlaceBriefDTO::getFromEntity).toList();

                recommendations.addAll(currentPlaceRecommendations);

                for (PlaceBriefDTO recPlace : currentPlaceRecommendations) {
                    isolatorsVisited.add(recPlace.getId());
                }
                isolators = isolatorsVisited.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
        }

        var listDto = new PagedListDTO<PlaceBriefDTO>();
        listDto.setList(recommendations);
        listDto.setPageNum(page);
        listDto.setTotalPages(totalPages);

        return listDto;
    }

    @Override
    @Transactional
    public PagedListDTO<PlaceBriefDTO> getNearestPlacesByVisited(
            int page,
            int size
    ) {
        List<PlaceBriefDTO> visitedPlaces = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getVisitedPlaces().stream().map(
                PlaceBriefDTO::getFromEntity).toList();

        var pageRequest = PageRequest.of(page, size);

        List<Long> isolatorIds = new ArrayList<>();
        for (PlaceBriefDTO place : visitedPlaces) {
            isolatorIds.add(place.getId());
        }

        String isolators = isolatorIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        List<PlaceBriefDTO> recommendations = new ArrayList<>();
        var totalPages = 0;

        for (PlaceBriefDTO place : visitedPlaces) {
            var placeCoordinates = place.getCoordinates();
            Page<Place> currentVisitedPlaces = placeRepository.findNearestByVisited(
                    placeCoordinates.getLatitude().doubleValue(),
                    placeCoordinates.getLongitude().doubleValue(),
                    1.0,
                    5.0,
                    2,
                    isolators,
                    pageRequest
            );

            totalPages += currentVisitedPlaces.getTotalPages();

            List<PlaceBriefDTO> currentPlaceRecommendations = currentVisitedPlaces.toList()
                    .stream()
                    .map(PlaceBriefDTO::getFromEntity).toList();

            recommendations.addAll(currentPlaceRecommendations);

            for (PlaceBriefDTO recPlace : currentPlaceRecommendations) {
                isolatorIds.add(recPlace.getId());
            }

            isolators = isolatorIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        }

        var listDto = new PagedListDTO<PlaceBriefDTO>();
        listDto.setList(recommendations);
        listDto.setPageNum(page);
        listDto.setTotalPages(totalPages);

        return listDto;
    }

    @Override
    @Transactional
    public PagedListDTO<PlaceBriefDTO> getContentBasedRecommendations(
            int page,
            int size
    ) {
        var pageRequest = PageRequest.of(page, size);
        List<Category> categoryList = categoryRepository.findAll();

        List<UserCoefficientMain> userCoefficientMains = userCoefficientMainRepository
                .getAllByUserId(userRepository.findByEmail(SecurityContextHelper.email())
                        .orElseThrow().getId());
        Map<Category, Double> userVector = UserVectorBuilder.getUserVector(userCoefficientMains, categoryList);

        Page<Place> notVisitedPlacesPage = placeRepository.findNotVisitedByUser(
                userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow().getId(),
                pageRequest);

        List<PlaceBriefDTO> recommendations = CBFRecommender.getRecommendations(
                categoryList,
                userVector,
                notVisitedPlacesPage);

        var listDto = new PagedListDTO<PlaceBriefDTO>();
        listDto.setList(recommendations);
        listDto.setPageNum(page);
        listDto.setTotalPages(notVisitedPlacesPage.getTotalPages());

        return listDto;
    }

    @Override
    @Transactional
    public PagedListDTO<PlaceBriefDTO> getCollaborativeRecommendationsByScores(
            int page,
            int size
    ) {
        var pageRequest = PageRequest.of(page, size);
        var user = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow();

        Page<Place> notVisitedPlacesPage = placeRepository.findNotVisitedByUser(user.getId(), pageRequest);
        List<Place> placeList = notVisitedPlacesPage.stream().toList();

        Map<User, HashMap<Place, Double>> data = new HashMap<>();

        List<Score> scoreList = scoreRepository.findAll();

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

        Map<User, HashMap<Place, Double>> projectedData = SlopeOne.slopeOne(data, placeList);
        List<PlaceBriefDTO> recommendations = new ArrayList<>(
                (projectedData.get(user)).keySet().stream().map(PlaceBriefDTO::getFromEntity).toList());

        var listDto = new PagedListDTO<PlaceBriefDTO>();
        listDto.setList(recommendations);
        listDto.setPageNum(page);
        listDto.setTotalPages(notVisitedPlacesPage.getTotalPages());

        return listDto;
    }

    @Override
    @Transactional
    public PagedListDTO<PlaceBriefDTO> getCollaborativeRecommendationsByFavourites(
            int page,
            int size
    ) {
        var pageRequest = PageRequest.of(page, size);
        var user = userRepository.findByEmail(SecurityContextHelper.email()).orElseThrow();

        Page<Place> notVisitedPlacesPage = placeRepository.findNotVisitedByUser(user.getId(), pageRequest);
        List<Place> placeList = notVisitedPlacesPage.stream().toList();

        Map<User, HashMap<Place, Double>> data = new HashMap<>();

        List<Score> scoreList = scoreRepository.findAll();
        List<Place> currUserFavourPlaces = user.getFavouritePlaces();

        for (Place favourPlace : currUserFavourPlaces) {
            scoreList.add(new Score().setPlace(favourPlace).setUser(user).setScoreValue(5));
        }

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

        Map<User, HashMap<Place, Double>> projectedData = SlopeOne.slopeOne(data, placeList);

        List<PlaceBriefDTO> recommendations = new ArrayList<>(
                (projectedData.get(user)).keySet().stream().map(PlaceBriefDTO::getFromEntity).toList());

        var listDto = new PagedListDTO<PlaceBriefDTO>();
        listDto.setList(recommendations);
        listDto.setPageNum(page);
        listDto.setTotalPages(notVisitedPlacesPage.getTotalPages());

        return listDto;
    }
}
