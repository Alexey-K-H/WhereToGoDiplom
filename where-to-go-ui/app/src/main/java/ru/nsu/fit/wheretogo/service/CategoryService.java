package ru.nsu.fit.wheretogo.service;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.nsu.fit.wheretogo.model.place.Category;

import java.util.List;

/**
 * Сервис по работе с категориями
 */
public interface CategoryService {
    @GET("/category/list")
    Call<List<Category>> getCategories();
}
