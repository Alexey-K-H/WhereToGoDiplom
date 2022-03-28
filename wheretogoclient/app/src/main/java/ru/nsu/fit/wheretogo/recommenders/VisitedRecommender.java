package ru.nsu.fit.wheretogo.recommenders;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.model.PlaceList;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.service.PlaceListService;
import ru.nsu.fit.wheretogo.util.AuthorizationHelper;

public class VisitedRecommender extends Recommender{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_visited);
        setRecyclerView(findViewById(R.id.visited_recommend_recycler));
        getVisitedRecommendations();
    }

    //Метод, который отправляет запрос на получение мест-рекомендаций на основе тех, которые пользователь посетил
    private void getVisitedRecommendations(){
        PlaceListService placeService = ServiceGenerator.createService(PlaceListService.class);
        Call<PlaceList> placeListCall = placeService.getRecommendByVisited(
                AuthorizationHelper.getUserProfile().getId(),
                1,
                0
        );
        Context context = this;
        placeListCall.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(@NonNull Call<PlaceList> call,
                                   @NonNull Response<PlaceList> response) {
                assert response.body() != null;
                initView(response.body().getList());
            }

            @Override
            public void onFailure(@NonNull Call<PlaceList> call, @NonNull Throwable t) {
                Toast.makeText(context, R.string.unexpectedErrorMsg, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}
