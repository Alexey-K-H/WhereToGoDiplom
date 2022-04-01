package ru.nsu.fit.wheretogo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.model.ShowMapMode;


public class ForYouActivity extends AppCompatActivity {

    ImageButton recommendByVisitedBtn;
    ImageButton recommendByScoredBtn;
    ImageButton recommendByOthersBtn;
    ImageButton recommendByGPSBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_you);

        recommendByVisitedBtn = (ImageButton) findViewById(R.id.by_visited_btn);
        recommendByScoredBtn = (ImageButton) findViewById(R.id.by_scored_btn);
        recommendByOthersBtn = (ImageButton) findViewById(R.id.by_others_btn);
        recommendByGPSBtn = (ImageButton) findViewById(R.id.by_gps_btn);

        recommendByGPSBtn.setOnClickListener(this::openNearestRecommender);
        recommendByVisitedBtn.setOnClickListener(this::openVisitedRecommender);
        recommendByScoredBtn.setOnClickListener(this::openScoredRecommender);
        recommendByOthersBtn.setOnClickListener(this::openUsersRecommender);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openNearestRecommender(View view){
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("show_map_mode", ShowMapMode.NEAREST.ordinal());
        startActivity(intent);
    }

    public void openVisitedRecommender(View view){
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("show_map_mode", ShowMapMode.RECOMMENDED_VISITED.ordinal());
        startActivity(intent);
    }

    public void openScoredRecommender(View view){
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("show_map_mode", ShowMapMode.RECOMMENDED_SCORED.ordinal());
        startActivity(intent);
    }

    public void openUsersRecommender(View view){
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("show_map_mode", ShowMapMode.RECOMMENDED_USERS.ordinal());
        startActivity(intent);
    }
}
