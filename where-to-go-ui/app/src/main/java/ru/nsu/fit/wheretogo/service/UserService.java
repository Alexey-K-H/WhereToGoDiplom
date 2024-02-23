package ru.nsu.fit.wheretogo.service;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.nsu.fit.wheretogo.model.user.User;

/**
 * Сервис по работе с пользователями
 */
public interface UserService {

    @POST("/user/register")
    Call<User> registerUser(
            @Query("email") String email,
            @Query("username") String username,
            @Query("password") String password
    );

    @POST("/user/username")
    Call<User> setUsername(@Query("username") String username);

    @POST("/user/password")
    Call<Void> setPassword(@Query("password") String password);

    @POST("/user/logout")
    Call<Void> logout();

    @GET("/user")
    Call<User> getCurrentUser();

    @GET("/user/{id}")
    Call<User> getUser(@Path("id") Long id);

    @GET("/user/contain/visited")
    Call<Boolean> isUserHasVisited();

    @GET("/user/contain/favourites")
    Call<Boolean> isUserHasFavourites();

    @GET("/user/contain/visited/{id}")
    Call<Boolean> isPlaceInUserVisited(@Path("id") Long id);
}
