package ru.nsu.fit.wheretogo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLng;

import ru.nsu.fit.wheretogo.model.ClusterMarker;


public class PlaceActivity extends AppCompatActivity {

    private String placeTitle;
    private String placeDescription;
    private String thumbnailLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View placeFullDescView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.activity_map_place,
                        (ConstraintLayout)findViewById(R.id.place_description_container)
                );


        Bundle arguments = getIntent().getExtras();
        if(arguments != null){
            //Берем информацию о местах
            placeTitle = getIntent().getStringExtra("title");
            placeDescription = getIntent().getStringExtra("desc");
            thumbnailLink = getIntent().getStringExtra("link");
        }

        placeFullDescView.findViewById(R.id.back_from_full_info).setOnClickListener(view -> finish());

        setContentView(placeFullDescView);

        fillPlaceFields(placeFullDescView);

        //Полоска рейтинга места
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(PlaceActivity.this, "Ваша оценка: " + (int)v,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillPlaceFields(View view){
        TextView placeName = (TextView)view.findViewById(R.id.place_title);
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


        TextView placeText = (TextView)view.findViewById(R.id.place_description);
        placeText.setText(placeDescription);
    }
}
