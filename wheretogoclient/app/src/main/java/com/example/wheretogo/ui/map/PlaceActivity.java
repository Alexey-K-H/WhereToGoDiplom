package com.example.wheretogo.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.wheretogo.R;
import com.example.wheretogo.model.place.Place;

public class PlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getIntent().getExtras();

        View placeFullDescView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.activity_map_place,
                        (ConstraintLayout)findViewById(R.id.place_description_container)
                );

        Place place;

        if(arguments != null){
            place = (Place) arguments.getSerializable(Place.class.getSimpleName());
            fillPlaceFields(placeFullDescView, place);
        }

        placeFullDescView.findViewById(R.id.back_from_full_info).setOnClickListener(view -> finish());

        setContentView(placeFullDescView);
    }

    private void fillPlaceFields(View view, Place place){
        TextView placeName = (TextView)view.findViewById(R.id.place_title);
        placeName.setText(place.getName());

        ImageView placeImage = (ImageView) view.findViewById(R.id.place_full_icon);
        placeImage.setImageResource(Integer.parseInt(place.getAvatar()));

        TextView placeText = (TextView)view.findViewById(R.id.place_description);
        placeText.setText(place.getDescription());
    }

    public void finishActivity(View v){
        finish();
    }
}
