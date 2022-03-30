package ru.nsu.fit.wheretogo.model.service;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface StayPointService {
    @PUT("/stay_point")
    Call<String> addStayPoint(
            @Query("lat") double lat,
            @Query("lon") double lon
    );
}
