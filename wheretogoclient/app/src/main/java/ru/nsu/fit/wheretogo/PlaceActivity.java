package ru.nsu.fit.wheretogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PlaceActivity extends AppCompatActivity {

    private String placeTitle;
    private String placeDescription;

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
            //TODO:не загружается текст описания места, исправить...
            placeDescription = getIntent().getStringExtra("desc");
            fillPlaceFields(placeFullDescView);
        }

        placeFullDescView.findViewById(R.id.back_from_full_info).setOnClickListener(view -> finish());

        setContentView(placeFullDescView);
    }

    private void fillPlaceFields(View view){
        TextView placeName = (TextView)view.findViewById(R.id.place_title);
        placeName.setText(placeTitle);

        ImageView placeImage = (ImageView) view.findViewById(R.id.place_full_icon);
        //дефолтная иконка, если нет картинки
        placeImage.setImageResource(R.drawable.unknown);
        //placeImage.setImageBitmap(marker.getIconPicture());

        TextView placeText = (TextView)view.findViewById(R.id.place_description);
        placeText.setText(placeDescription);
    }
}
