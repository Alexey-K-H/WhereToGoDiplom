package ru.nsu.fit.wheretogo.model.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsu.fit.wheretogo.model.entity.Place;

public interface PlaceService {
    @GET("/place/{id}")
    Call<Place> getPlace(@Path("id") String id);
}
