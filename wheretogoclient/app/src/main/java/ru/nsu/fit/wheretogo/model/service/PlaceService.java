package ru.nsu.fit.wheretogo.model.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsu.fit.wheretogo.model.entity.Place;

public interface PlaceService {
    @GET("/place/{id}")
    Call<Place> getPlace(@Path("id") Integer id);

    @GET("/place/favourite")
    Call<List<Place>> getFavouritePlaces();

    @GET("/place/visited")
    Call<List<Place>> getVisitedPlaces();

    @FormUrlEncoded
    @POST("/place/favourite")
    Call<Place> addFavoritePlace(@Field("placeId") Integer placeId);

    @FormUrlEncoded
    @POST("/place/visited")
    Call<Place> addVisitedPlace(@Field("placeId") Integer placeId);

}
