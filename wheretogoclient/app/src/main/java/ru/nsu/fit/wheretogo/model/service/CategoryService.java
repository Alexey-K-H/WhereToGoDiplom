package ru.nsu.fit.wheretogo.model.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsu.fit.wheretogo.model.entity.Category;

import java.util.List;

public interface CategoryService {
    @GET("/category/list")
    Call<List<Category>> getCategories(
            @Query("per_page") int per_page,
            @Query("page") int page
    );
}
