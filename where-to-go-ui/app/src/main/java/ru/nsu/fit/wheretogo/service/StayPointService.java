package ru.nsu.fit.wheretogo.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Сервис по работе с точками останова
 */
public interface StayPointService {

    @PUT("/stay_point")
    Call<String> addStayPoint(
            @Query("lat") double lat,
            @Query("lon") double lon
    );

    @GET("/stay_point/exist_any")
    Call<Boolean> isUserHasStayPoints();
}
