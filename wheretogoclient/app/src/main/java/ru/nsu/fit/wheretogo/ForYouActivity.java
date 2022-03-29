package ru.nsu.fit.wheretogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.recommenders.ScoredRecommender;
import ru.nsu.fit.wheretogo.recommenders.VisitedRecommender;


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

        recommendByVisitedBtn.setOnClickListener(this::openVisitedRecommender);
        recommendByScoredBtn.setOnClickListener(this::openScoredRecommender);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openVisitedRecommender(View view){
        Intent intent = new Intent(this, VisitedRecommender.class);
        startActivity(intent);
    }

    public void openScoredRecommender(View view){
        Intent intent = new Intent(this, ScoredRecommender.class);
        startActivity(intent);
    }

}
