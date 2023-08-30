package ru.nsu.fit.wheretogo.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nsu.fit.wheretogo.model.entity.Place;

/**
 * Сервис по работе с рекомендациями
 */
public interface RecommenderService {

    /**
     * Получение списка всех мест в базе данных
     *
     * @param categoryIds список индексов категорий
     * @return список мест
     */
    @GET("/place/list")
    Call<List<Place>> getPlaceList(
            @Query("category") List<Integer> categoryIds
    );

    /**
     * Получение списка мест, в соответствии с категориями рядом с текущей геоточкой
     *
     * @param myLat       широта точки
     * @param myLon       долгота точки
     * @param startDist   начальный радиус поиска
     * @param maxDist     максимальный радиус поиска
     * @param limit       максимальное число найденных мест
     * @param categoryIds список идентификаторов категорий
     * @return список мест
     */
    @GET("/recommend/nearest/category")
    Call<List<Place>> getNearestPlacesByCategory(
            @Query("_my_lat") Double myLat,
            @Query("_my_lon") Double myLon,
            @Query("_start_dist") Double startDist,
            @Query("_max_dist") Double maxDist,
            @Query("_limit") Integer limit,
            @Query("category") List<Integer> categoryIds
    );

    /**
     * Получение списка рекомендаций на основе истории перемещений
     *
     * @return список мест
     */
    @GET("/recommend/stay_points")
    Call<List<Place>> getRecommendByVisited();

    /**
     * Получение списка рекомендаций на основе контента
     *
     * @return список мест
     */
    @GET("/recommend/content_based")
    Call<List<Place>> getRecommendByScores();

    /**
     * Получение списка рекомендаций на основе коллаборативной фильтрации
     *
     * @return список мест
     */
    @GET("/recommend/collaborative_filter")
    Call<List<Place>> getRecommendByUsers();
}
