package ru.nsu.fit.wheretogo.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.entity.Place;
import ru.nsu.fit.wheretogo.service.PlaceService;
import ru.nsu.fit.wheretogo.util.PictureLoader;

public class VisitedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);
        getVisitedPlaces();
    }

    private void getVisitedPlaces() {
        PlaceService placeService = ServiceGenerator.createService(PlaceService.class);
        Call<List<Place>> placeCall = placeService.getVisitedPlaces();
        Context context = this;
        placeCall.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call,
                                   @NonNull Response<List<Place>> response) {
                initView(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call,
                                  @NonNull Throwable t) {
                Toast.makeText(context, R.string.unexpectedErrorMsg, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }

    private void initView(List<Place> places) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        RecyclerView recycleView = findViewById(R.id.visited_recycler);
        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recycleView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, height);
        recycleView.setAdapter(adapter);
        PictureLoader.loadRecyclerPictures(this, places, adapter);
    }
}