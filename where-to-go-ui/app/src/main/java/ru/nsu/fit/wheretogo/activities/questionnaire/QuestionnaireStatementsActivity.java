package ru.nsu.fit.wheretogo.activities.questionnaire;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.activities.recommender.RouteTimeLimitActivity;
import ru.nsu.fit.wheretogo.util.Constants;

public class QuestionnaireStatementsActivity extends AppCompatActivity {
    private static final String TAG = QuestionnaireStatementsActivity.class.getSimpleName();

    private ImageButton cultureEventsBtn;
    private boolean cultureSelected = false;

    private ImageButton natureBtn;
    private boolean natureSelected = false;

    private ImageButton entertainmentBtn;
    private boolean entertainmentSelected = false;

    private ImageButton cafeEatBtn;
    private boolean cafeEatSelected = false;

    private ImageButton outsideBtn;
    private boolean outsideSelected = false;


    private Location lastLocation;
    private boolean isAtLeastOneOptionSelected = false;

    private static AppCompatActivity activityTrigger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_statements);

        var extras = getIntent().getExtras();
        if (extras != null) {
            lastLocation = extras.getParcelable(Constants.LAST_LOCATION_STRING, Location.class);
            Log.d(TAG, "Последнее местоположение:["
                    + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + "]");
        }

        cultureEventsBtn = findViewById(R.id.culture_btn_id);
        natureBtn = findViewById(R.id.nature_btn_id);
        entertainmentBtn = findViewById(R.id.entertainment_btn_id);
        cafeEatBtn = findViewById(R.id.cafe_eat_btn_id);
        outsideBtn = findViewById(R.id.outside_btn_id);

        ImageButton buildRouteBtn = findViewById(R.id.build_route_btn);

        cultureEventsBtn.setOnClickListener(this::updateCulture);
        natureBtn.setOnClickListener(this::updateNature);
        entertainmentBtn.setOnClickListener(this::updateEntertainment);
        cafeEatBtn.setOnClickListener(this::updateCafeEat);
        outsideBtn.setOnClickListener(this::updateOutside);

        buildRouteBtn.setOnClickListener(this::buildRoute);

        setActivityTrigger(this);
    }

    public void updateCulture(View view) {
        if (!isAtLeastOneOptionSelected) {
            isAtLeastOneOptionSelected = true;
        }

        if (cultureSelected) {
            cultureEventsBtn.setImageResource(R.drawable.culture_events);
            cultureSelected = false;
        } else {
            cultureEventsBtn.setImageResource(R.drawable.culture_events_selected);
            cultureSelected = true;
        }
    }

    public void updateNature(View view) {
        if (!isAtLeastOneOptionSelected) {
            isAtLeastOneOptionSelected = true;
        }

        if (natureSelected) {
            natureBtn.setImageResource(R.drawable.nature);
            natureSelected = false;
        } else {
            natureBtn.setImageResource(R.drawable.nature_selected);
            natureSelected = true;
        }
    }

    public void updateEntertainment(View view) {
        if (!isAtLeastOneOptionSelected) {
            isAtLeastOneOptionSelected = true;
        }

        if (entertainmentSelected) {
            entertainmentBtn.setImageResource(R.drawable.entertainment);
            entertainmentSelected = false;
        } else {
            entertainmentBtn.setImageResource(R.drawable.entertainmet_selected);
            entertainmentSelected = true;
        }
    }

    public void updateCafeEat(View view) {
        if (!isAtLeastOneOptionSelected) {
            isAtLeastOneOptionSelected = true;
        }

        if (cafeEatSelected) {
            cafeEatBtn.setImageResource(R.drawable.cafe_eat);
            cafeEatSelected = false;
        } else {
            cafeEatBtn.setImageResource(R.drawable.cafe_eat_selected);
            cafeEatSelected = true;
        }

    }

    public void updateOutside(View view) {
        if (!isAtLeastOneOptionSelected) {
            isAtLeastOneOptionSelected = true;
        }

        if (outsideSelected) {
            outsideBtn.setImageResource(R.drawable.outside);
            outsideSelected = false;
        } else {
            outsideBtn.setImageResource(R.drawable.outside_selected);
            outsideSelected = true;
        }
    }

    public void buildRoute(View view) {
        if (!isAtLeastOneOptionSelected) {
            Toast.makeText(this, "Необходимо выбрать хотя бы один из вариантов", Toast.LENGTH_LONG).show();
        } else {
            var intent = new Intent(this, RouteTimeLimitActivity.class);
            intent.putExtra(Constants.LAST_LOCATION_STRING, lastLocation);
            startActivity(intent);
        }
    }

    public static void setActivityTrigger(AppCompatActivity activity) {
        activityTrigger = activity;
    }

    public static void finishFromAnother() {
        activityTrigger.finish();
    }

}
