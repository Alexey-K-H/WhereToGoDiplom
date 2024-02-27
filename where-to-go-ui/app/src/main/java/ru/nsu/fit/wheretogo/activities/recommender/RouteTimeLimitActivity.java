package ru.nsu.fit.wheretogo.activities.recommender;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.activities.map.MapsActivity;
import ru.nsu.fit.wheretogo.activities.questionnaire.QuestionnaireKnowledgeActivity;
import ru.nsu.fit.wheretogo.activities.questionnaire.QuestionnaireStatementsActivity;
import ru.nsu.fit.wheretogo.model.ShowMapMode;
import ru.nsu.fit.wheretogo.util.Constants;

public class RouteTimeLimitActivity extends AppCompatActivity {
    private static final String TAG = RouteTimeLimitActivity.class.getSimpleName();

    private TextInputEditText hoursInput;
    private TextInputEditText minutesInput;

    private Location lastLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_time_limit_activity);

        var extras = getIntent().getExtras();
        if (extras != null) {
            lastLocation = extras.getParcelable(Constants.LAST_LOCATION_STRING, Location.class);
            Log.d(TAG, "Последнее местоположение:["
                    + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + "]");
        }

        hoursInput = findViewById(R.id.input_hours_field_id);
        minutesInput = findViewById(R.id.input_minutes_field_id);

        hoursInput.setText("2");
        minutesInput.setText("0");

        ImageButton buildRoute = findViewById(R.id.build_route_after_time_id);
        buildRoute.setOnClickListener(this::buildRoute);
    }

    public void buildRoute(View view) {
        var hours = hoursInput.getText().toString();
        var minutes = minutesInput.getText().toString();

        if (hours.isEmpty() && minutes.isEmpty()) {
            Toast.makeText(this, "Необходимо указать время", Toast.LENGTH_SHORT).show();
        } else {
            RecommenderActivity.finishFromAnother();
            QuestionnaireKnowledgeActivity.finishFromAnother();
            QuestionnaireStatementsActivity.finishFromAnother();
            finish();

            var intent = new Intent(this, MapsActivity.class);
            intent.putExtra(Constants.SHOW_MAP_MODE_STRING, ShowMapMode.RECOMMENDED_ROUTE.name());
            intent.putExtra(Constants.LAST_LOCATION_STRING, lastLocation);
            intent.putExtra(Constants.ROUTE_HOURS_LIMIT, hours);
            intent.putExtra(Constants.ROUTE_MINUTES_LIMIT, hours);
            startActivity(intent);
        }
    }


}
