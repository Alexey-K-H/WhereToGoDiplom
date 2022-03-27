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

    ImageButton back;
    ImageButton recommendByVisitedBtn;
    ImageButton recommendByScoredBtn;
    ImageButton recommendByOthersBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_you);

        //Buttons init
        back = (ImageButton) findViewById(R.id.back_recomm);

        recommendByVisitedBtn = (ImageButton) findViewById(R.id.by_visited_btn);
        recommendByScoredBtn = (ImageButton) findViewById(R.id.by_scored_btn);

        recommendByOthersBtn = (ImageButton) findViewById(R.id.by_others_btn);

        //Buttons listeners
        back.setOnClickListener(this::closeRecommender);

        recommendByVisitedBtn.setOnClickListener(this::openVisitedRecommender);
        recommendByScoredBtn.setOnClickListener(this::openScoredRecommender);


    }

    private void openMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void closeRecommender(View view){
        finish();
        openMap(view);
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
