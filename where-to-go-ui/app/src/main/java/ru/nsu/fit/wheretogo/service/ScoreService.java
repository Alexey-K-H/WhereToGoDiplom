package ru.nsu.fit.wheretogo.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsu.fit.wheretogo.model.entity.Score;

/**
 * Сервис по работе с оценками
 */
public interface ScoreService {

    @GET("/score/{user_id}/{place_id}")
    Call<Score> getUserPlaceScore(@Path("user_id") Long userId, @Path("place_id") Long placeId);

    @PUT("/score")
    Call<Score> setScore(
            @Query("user_id") Long userId,
            @Query("place_id") Long placeId,
            @Query("score") Integer value
    );

    @GET("/score/exist_any/{user_id}")
    Call<Boolean> isUserHasScores(
            @Path("user_id") Long userId
    );
}
