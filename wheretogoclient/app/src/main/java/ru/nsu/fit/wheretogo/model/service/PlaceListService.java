package ru.nsu.fit.wheretogo.model.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nsu.fit.wheretogo.model.PlaceList;

public interface PlaceListService {
    @GET("/place/list")
    Call<PlaceList> getPlaceList(
            @Query("category") List<Integer> categoryIds,
            @Query("per_page") int per_page,
            @Query("page") int page
    );

    @GET("/place/nearest/category")
    Call<PlaceList> getNearestPlacesByCategory(
            @Query("_my_lat") Double myLat,
            @Query("_my_lon") Double myLon,
            @Query("_start_dist") Double startDist,
            @Query("_max_dist") Double maxDist,
            @Query("_limit") Integer limit,
            @Query("category") List<Integer> categoryIds,
            @Query("per_page") int per_page,
            @Query("page") int page
    );

    @GET("/place/recommend/stay_points")
    Call<PlaceList> getRecommendByVisited(
            @Query("per_page") int per_page,
            @Query("page") int page
    );

    @GET("/place/recommend/content_based")
    Call<PlaceList> getRecommendByScores(
            @Query("per_page") int per_page,
            @Query("page") int page
    );

    @GET("/place/recommend/collaborative_filter")
    Call<PlaceList> getRecommendByUsers(
            @Query("per_page") int per_page,
            @Query("page") int page
    );
}
