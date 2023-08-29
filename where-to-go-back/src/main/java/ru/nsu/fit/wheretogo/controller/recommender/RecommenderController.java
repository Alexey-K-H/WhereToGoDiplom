package ru.nsu.fit.wheretogo.controller.recommender;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.place.CategoryDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
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
public class RecommenderController {

    private final RecommenderService recommenderService;
    private final CategoryService categoryService;
    private final StayPointService stayPointService;
    private final ScoreService scoreService;
    private final UserService userService;

    public RecommenderController(
            RecommenderService recommenderService,
            CategoryService categoryService,
            StayPointService stayPointService,
            ScoreService scoreService,
            UserService userService) {
        this.recommenderService = recommenderService;
        this.categoryService = categoryService;
        this.stayPointService = stayPointService;
        this.scoreService = scoreService;
        this.userService = userService;
    }

    @GetMapping("/nearest/category")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> getNearestPlaces(
            @RequestParam(name = "_my_lat") Double myLat,
            @RequestParam(name = "_my_lon") Double myLon,
            @RequestParam(name = "_start_dist") Double startDist,
            @RequestParam(name = "_max_dist") Double maxDist,
            @RequestParam(name = "_limit") Integer limit,
            @RequestParam(name = "category", required = false) List<Integer> categoryId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
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
                        categoryIds,
                        page,
                        pageSize),
                HttpStatus.OK);
    }

    @GetMapping("/stay_points")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> getStayPointRecommendations(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        if (stayPointService.ifUserHasStayPoints()) {
            return new ResponseEntity<>(
                    recommenderService.getNearestPlacesByStayPoints(
                            page,
                            pageSize),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    recommenderService.getNearestPlacesByVisited(
                            page,
                            pageSize),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/content_based")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> getContentBasedRecommendations(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        return new ResponseEntity<>(
                recommenderService.getContentBasedRecommendations(
                        page,
                        pageSize),
                HttpStatus.OK);
    }

    @GetMapping("/collaborative_filter")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> getCollaborativeFilteringRecommendations(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        if (scoreService.ifUserHasScores(userService.getCurrentUserDto().getId())) {
            return new ResponseEntity<>(
                    recommenderService.getCollaborativeRecommendationsByScores(
                            page,
                            pageSize
                    ),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    recommenderService.getCollaborativeRecommendationsByFavourites(
                            page,
                            pageSize
                    ),
                    HttpStatus.OK
            );
        }
    }
}