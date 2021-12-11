package ru.nsu.fit.wheretogo.model.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nsu.fit.wheretogo.model.PlaceList;

public interface PlaceListService {
    @GET("/place/list")
    Call<PlaceList> getPlaceList(
            @Query("category") List<String> categories,
            @Query("per_page") int per_page,
            @Query("page") int page
    );

}
