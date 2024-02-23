package ru.nsu.fit.wheretogo.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.nsu.fit.wheretogo.model.place.Place;

/**
 * Сервис по работе с местами
 */
public interface PlaceService {

    @GET("/place/{id}")
    Call<Place> getPlace(@Path("id") Long id);

    @GET("/place/favourite")
    Call<List<Place>> getFavouritePlaces();

    @GET("/place/favourite/{id}")
    Call<Boolean> findFavouriteById(@Path("id") Long id);

    @GET("/place/visited")
    Call<List<Place>> getVisitedPlaces();

    @GET("/place/visited/{id}")
    Call<Boolean> findVisitedById(@Path("id") Long id);

    @DELETE("/place/visited/{id}")
    Call<String> deleteVisited(@Path("id") Long id);

    @DELETE("/place/favourite/{id}")
    Call<String> deleteFavourite(@Path("id") Long id);

    @POST("/place/favourite/{id}")
    Call<String> addFavoritePlace(@Path("id") Long placeId);

    @POST("/place/visited/{id}")
    Call<String> addVisitedPlace(@Path("id") Long placeId);

}
