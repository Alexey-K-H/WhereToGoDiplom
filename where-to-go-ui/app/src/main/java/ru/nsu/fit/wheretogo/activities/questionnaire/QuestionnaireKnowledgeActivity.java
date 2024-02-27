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
import ru.nsu.fit.wheretogo.activities.recommender.RecommenderActivity;
import ru.nsu.fit.wheretogo.util.Constants;
import ru.nsu.fit.wheretogo.util.KnowledgeMode;

public class QuestionnaireKnowledgeActivity extends AppCompatActivity {
    private static final String TAG = QuestionnaireKnowledgeActivity.class.getSimpleName();

    private ImageButton firstlyHereBtn;
    private ImageButton visitEarlyBtn;
    private ImageButton localBtn;

    private KnowledgeMode knowledgeMode = null;

    private Location lastLocation;

    private static AppCompatActivity activityTrigger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_knowledge);

        var extras = getIntent().getExtras();
        if (extras != null) {
            lastLocation = extras.getParcelable(Constants.LAST_LOCATION_STRING, Location.class);
            Log.d(TAG, "Последнее местоположение:["
                    + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + "]");
        }

        firstlyHereBtn = findViewById(R.id.firstly_here_btn_id);
        visitEarlyBtn = findViewById(R.id.visit_early_btn_id);
        localBtn = findViewById(R.id.local_btn_id);

        ImageButton nextBtn = findViewById(R.id.quest_know_next_btn_id);

        firstlyHereBtn.setOnClickListener(this::updateMode);
        visitEarlyBtn.setOnClickListener(this::updateMode);
        localBtn.setOnClickListener(this::updateMode);

        nextBtn.setOnClickListener(this::next);

        setActivityTrigger(this);
    }

    public void updateMode(View view) {
        if (view.getId() == R.id.firstly_here_btn_id) {
            knowledgeMode = KnowledgeMode.FIRSTLY_HERE;

            firstlyHereBtn.setImageResource(R.drawable.firstly_here_selected);
            visitEarlyBtn.setImageResource(R.drawable.visit_early);
            localBtn.setImageResource(R.drawable.local);
        }

        if (view.getId() == R.id.visit_early_btn_id) {
            knowledgeMode = KnowledgeMode.VISIT_BEFORE;

            firstlyHereBtn.setImageResource(R.drawable.firstly_here);
            visitEarlyBtn.setImageResource(R.drawable.visit_early_selected);
            localBtn.setImageResource(R.drawable.local);
        }

        if (view.getId() == R.id.local_btn_id) {
            knowledgeMode = KnowledgeMode.RESIDENT;

            firstlyHereBtn.setImageResource(R.drawable.firstly_here);
            visitEarlyBtn.setImageResource(R.drawable.visit_early);
            localBtn.setImageResource(R.drawable.local_selected);
        }
    }

    public void next(View view) {
        if (knowledgeMode == null) {
            Toast.makeText(this, "Необходимо выбрать один из вариантов", Toast.LENGTH_LONG).show();
        } else {
            var intent = new Intent(this, QuestionnaireStatementsActivity.class);
            intent.putExtra(Constants.LAST_LOCATION_STRING, lastLocation);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        var intent = new Intent(this, RecommenderActivity.class);
        intent.putExtra(Constants.LAST_LOCATION_STRING, lastLocation);
        startActivity(intent);
    }

    public static void setActivityTrigger(AppCompatActivity activity) {
        activityTrigger = activity;
    }

    public static void finishFromAnother() {
        activityTrigger.finish();
    }

}
