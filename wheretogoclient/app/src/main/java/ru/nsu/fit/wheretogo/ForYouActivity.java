package ru.nsu.fit.wheretogo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.ShowMapMode;
import ru.nsu.fit.wheretogo.model.service.ScoreService;
import ru.nsu.fit.wheretogo.model.service.StayPointService;
import ru.nsu.fit.wheretogo.util.AuthorizationHelper;


public class ForYouActivity extends AppCompatActivity {
    private static final String TAG = ForYouActivity.class.getSimpleName();

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

        //Выполняем проврку наличия в базе данных информации об оценках и stay-point-ах
        Call<Boolean> checkStayPoints = ServiceGenerator.createService(StayPointService.class).isUserHasStayPoints();
        checkStayPoints.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if(response.code() == 200 && response.body() != null){
                    if(response.body()){
                        Log.d(TAG, "Find some stay-points");
                        recommendByVisitedBtn.setOnClickListener(ForYouActivity.this::openVisitedRecommender);
                    }else {
                        blockRecommendButton(recommendByVisitedBtn);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

            }
        });


        Call<Boolean> checkScores = ServiceGenerator.createService(ScoreService.class).isUserHasScores(AuthorizationHelper.getUserProfile().getId());
        checkScores.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if(response.code() == 200 && response.body() != null){
                    if(response.body()){
                        Log.d(TAG, "Find some scores of user");
                        recommendByScoredBtn.setOnClickListener(ForYouActivity.this::openScoredRecommender);
                        recommendByOthersBtn.setOnClickListener(ForYouActivity.this::openUsersRecommender);
                    }else {
                        blockRecommendButton(recommendByOthersBtn);
                        blockRecommendButton(recommendByScoredBtn);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {

            }
        });

        recommendByGPSBtn.setOnClickListener(this::openNearestRecommender);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openNearestRecommender(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("show_map_mode", ShowMapMode.NEAREST.ordinal());
        startActivity(intent);
    }

    public void openVisitedRecommender(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("show_map_mode", ShowMapMode.RECOMMENDED_VISITED.ordinal());
        startActivity(intent);
    }

    public void openScoredRecommender(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("show_map_mode", ShowMapMode.RECOMMENDED_SCORED.ordinal());
        startActivity(intent);
    }

    public void openUsersRecommender(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("show_map_mode", ShowMapMode.RECOMMENDED_USERS.ordinal());
        startActivity(intent);
    }

    //Блокирует кнопку вызова рекомендации в случае, если не хватает данных
    private void blockRecommendButton(ImageButton imageButton){
        imageButton.setClickable(false);
        imageButton.setImageResource(R.drawable.not_available_recommend);
    }

}
