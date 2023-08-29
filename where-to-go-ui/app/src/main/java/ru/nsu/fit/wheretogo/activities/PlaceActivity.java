package ru.nsu.fit.wheretogo.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.entity.Score;
import ru.nsu.fit.wheretogo.service.PlaceService;
import ru.nsu.fit.wheretogo.service.ScoreService;
import ru.nsu.fit.wheretogo.util.helper.AuthorizationHelper;


public class PlaceActivity extends AppCompatActivity {

    private Long placeId;
    private String placeTitle;
    private String placeDescription;
    private String thumbnailLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View placeFullDescView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.activity_map_place,
                        (ConstraintLayout) findViewById(R.id.place_description_container)
                );


        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            //Берем информацию о местах
            placeId = Long.parseLong(getIntent().getStringExtra("id"));
            placeTitle = getIntent().getStringExtra("title");
            placeDescription = getIntent().getStringExtra("desc");
            thumbnailLink = getIntent().getStringExtra("link");
        }

        setContentView(placeFullDescView);

        fillPlaceFields(placeFullDescView);

        //Полоска рейтинга места
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        //Загружаем из БД рейтинг пользователя данного места, если он есть
        Call<Score> call = ServiceGenerator.createService(ScoreService.class)
                .getUserPlaceScore(AuthorizationHelper.getUserProfile().getId(), placeId);

        call.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(@NonNull Call<Score> call, @NonNull Response<Score> response) {
                if (response.code() == 200 && response.body() != null) {
                    Long userMark = response.body().getScoreValue();
                    ratingBar.setRating((float) userMark);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Score> call, @NonNull Throwable t) {

            }
        });


        ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) -> {

            Call<Score> call1 = ServiceGenerator.createService(ScoreService.class)
                    .setScore(AuthorizationHelper.getUserProfile().getId(),
                            placeId,
                            (int) v);

            call1.enqueue(new Callback<Score>() {
                @Override
                public void onResponse(@NonNull Call<Score> call1, @NonNull Response<Score> response) {
                    if (response.code() == 200 && response.body() != null) {
//                            Toast.makeText(PlaceActivity.this, "Ваша оценка: " + (int)v,
//                                    Toast.LENGTH_LONG).show();

                        ratingBar1.setRating(response.body().getScoreValue());
                        addVisitedPlace(placeFullDescView);
                    } else if (response.code() == 400) {

                    } else {
                        Toast.makeText(PlaceActivity.this, R.string.unexpectedErrorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Score> call1, @NonNull Throwable t) {

                }
            });
        });
    }

    private void fillPlaceFields(View view) {
        TextView placeName = (TextView) view.findViewById(R.id.place_title);
        placeName.setText(placeTitle);

        ImageView placeImage = (ImageView) view.findViewById(R.id.place_full_icon);

        Glide.with(this)
                .asBitmap()
                .load(thumbnailLink)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {

                        placeImage.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });


        TextView placeText = (TextView) view.findViewById(R.id.place_description);
        placeText.setText(placeDescription);
    }

    private void addVisitedPlace(View view) {
        Call<String> call = ServiceGenerator.createService(PlaceService.class)
                .addVisitedPlace(placeId);
        Context context = this;
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() != 200) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }
}
