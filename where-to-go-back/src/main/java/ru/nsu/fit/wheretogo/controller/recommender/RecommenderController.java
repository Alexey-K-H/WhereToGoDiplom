package ru.nsu.fit.wheretogo.controller.recommender;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.place.CategoryDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.model.recommender.RouteRecommenderRequest;
import ru.nsu.fit.wheretogo.model.recommender.RouteRecommenderResponse;
import ru.nsu.fit.wheretogo.service.place.CategoryService;
import ru.nsu.fit.wheretogo.service.recommender.RecommenderService;
import ru.nsu.fit.wheretogo.service.score.ScoreService;
import ru.nsu.fit.wheretogo.service.user.StayPointService;
import ru.nsu.fit.wheretogo.service.user.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommenderController {

    private final RecommenderService recommenderService;
    private final CategoryService categoryService;
    private final StayPointService stayPointService;
    private final ScoreService scoreService;
    private final UserService userService;

    @GetMapping("/nearest/category")
    public ResponseEntity<List<PlaceBriefDTO>> getNearestPlaces(
            @RequestParam(name = "_my_lat") Double myLat,
            @RequestParam(name = "_my_lon") Double myLon,
            @RequestParam(name = "_start_dist") Double startDist,
            @RequestParam(name = "_max_dist") Double maxDist,
            @RequestParam(name = "_limit") Integer limit,
            @RequestParam(name = "category", required = false) List<Integer> categoryId
    ) {
        var categoryIds = "";
        categoryIds = Objects.requireNonNullElseGet(
                        categoryId,
                        () -> categoryService.getAllCategories().stream().map(CategoryDTO::getId).toList())
                .stream().map(String::valueOf).collect(Collectors.joining(","));

        return new ResponseEntity<>(
                recommenderService.getNearestPlacesByCategory(
                        myLat,
                        myLon,
                        startDist,
                        maxDist,
                        limit,
                        categoryIds),
                HttpStatus.OK);
    }

    @GetMapping("/stay_points")
    public ResponseEntity<List<PlaceBriefDTO>> getStayPointRecommendations() {
        if (stayPointService.ifUserHasStayPoints()) {
            return new ResponseEntity<>(
                    recommenderService.getNearestPlacesByStayPoints(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    recommenderService.getNearestPlacesByVisited(),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/content_based")
    public ResponseEntity<List<PlaceBriefDTO>> getContentBasedRecommendations() {
        return new ResponseEntity<>(
                recommenderService.getContentBasedRecommendations(),
                HttpStatus.OK);
    }

    @GetMapping("/collaborative_filter")
    public ResponseEntity<List<PlaceBriefDTO>> getCollaborativeFilteringRecommendations() {
        if (scoreService.ifUserHasScores(userService.getCurrentUser().getId())) {
            return new ResponseEntity<>(
                    recommenderService.getCollaborativeRecommendationsByScores(),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    recommenderService.getCollaborativeRecommendationsByFavourites(),
                    HttpStatus.OK
            );
        }
    }

    @PostMapping("/route")
    public ResponseEntity<RouteRecommenderResponse> getRouteRecommendation(
            @RequestBody() RouteRecommenderRequest request) {
        return new ResponseEntity<>(recommenderService.getRouteRecommendation(request), HttpStatus.OK);
    }

}