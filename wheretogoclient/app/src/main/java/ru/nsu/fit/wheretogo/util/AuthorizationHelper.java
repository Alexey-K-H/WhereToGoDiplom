package ru.nsu.fit.wheretogo.util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.entity.User;
import ru.nsu.fit.wheretogo.model.service.UserService;

public class AuthorizationHelper {
    private static final UserService userService = ServiceGenerator.createService(UserService.class);

    private static User userProfile;
    private static String email;
    private static String password;

    public static User getUserProfile() {
        return userProfile;
    }

    public static void setUserProfile(User userProfile) {
        AuthorizationHelper.userProfile = userProfile;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        AuthorizationHelper.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AuthorizationHelper.password = password;
    }

    public static void register(
            String username,
            String email,
            String password,
            Consumer<Response<User>> onSuccess,
            Consumer<Response<User>> onFail,
            Runnable onUnexpectedError
    ) {
        userService.registerUser(email, username, password).enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    AuthorizationHelper.setUserProfile(response.body());
                    onSuccess.accept(response);
                }
                else {
                    onFail.accept(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                onUnexpectedError.run();
                Log.e("registration", t.getMessage());
            }
        });
    }

    public static void login(
            String email,
            String password,
            Consumer<Response<User>> onSuccess,
            Consumer<Response<User>> onFail,
            Runnable onUnexpectedError
    ) {
        AuthorizationHelper.setEmail(email);
        AuthorizationHelper.setPassword(password);
        userService.getCurrentUser().enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    AuthorizationHelper.setUserProfile(response.body());
                    onSuccess.accept(response);
                }
                else {
                    onFail.accept(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                onUnexpectedError.run();
                Log.e("authorization", t.getMessage());
            }
        });
    }

    private AuthorizationHelper() {}
}
