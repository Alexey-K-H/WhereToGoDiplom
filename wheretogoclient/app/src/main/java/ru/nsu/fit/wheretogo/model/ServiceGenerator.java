package ru.nsu.fit.wheretogo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nsu.fit.wheretogo.util.AuthorizationHelper;

public class ServiceGenerator {
//    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static final String BASE_URL = "http://192.168.1.103:8080/";
//    private static final String BASE_URL = "http://35.184.230.81:8080/";

    private static final OkHttpClient.Builder httpClient
            = new OkHttpClient.Builder().authenticator((route, response) -> {
        Request request = response.request();
        if (request.header("Authorization") != null) {
            // Логин и пароль неверны
            return null;
        }
        return request.newBuilder()
                .header("Authorization", Credentials.basic(
                        AuthorizationHelper.getEmail(),
                        AuthorizationHelper.getPassword()
                )).build();
    });

    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
