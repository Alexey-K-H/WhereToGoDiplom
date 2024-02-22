package ru.nsu.fit.wheretogo.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
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
import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.ShowMapMode;
import ru.nsu.fit.wheretogo.service.ScoreService;
import ru.nsu.fit.wheretogo.service.StayPointService;
import ru.nsu.fit.wheretogo.service.UserService;
import ru.nsu.fit.wheretogo.util.helper.AuthorizationHelper;

public class ForYouActivity extends AppCompatActivity {
    private static final String TAG = ForYouActivity.class.getSimpleName();
    private static final String LAST_LOCATION_STRING = "lastLocation";
    private static final String SHOW_MAP_MODE_STRING = "show_map_mode";

    private ImageButton recommendByVisitedBtn;
    private ImageButton recommendByScoredBtn;
    private ImageButton recommendByOthersBtn;
    private ImageButton recommendByGPSBtn;
    private ImageButton recommendRoute;
    private Location lastLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_you);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lastLocation = extras.getParcelable(LAST_LOCATION_STRING, Location.class);
            Log.d(TAG, "Последнее местоположение:["
                    + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + "]");
        }

        recommendByVisitedBtn = (ImageButton) findViewById(R.id.by_visited_btn);
        recommendByScoredBtn = (ImageButton) findViewById(R.id.by_scored_btn);
        recommendByOthersBtn = (ImageButton) findViewById(R.id.by_others_btn);
        recommendByGPSBtn = (ImageButton) findViewById(R.id.by_gps_btn);
        recommendRoute = (ImageButton) findViewById(R.id.make_path_btn);

        Call<Boolean> checkStayPoints = ServiceGenerator.createService(StayPointService.class).isUserHasStayPoints();
        checkStayPoints.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (Boolean.TRUE.equals(response.body())) {
                        Log.d(TAG, "Найдены точки останова");
                        recommendByVisitedBtn.setOnClickListener(ForYouActivity.this::openVisitedRecommender);
                    } else {
                        Call<Boolean> checkVisited = ServiceGenerator.createService(UserService.class).isUserHasVisited();
                        checkVisited.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                                if (response.code() == 200 && response.body() != null) {
                                    if (Boolean.TRUE.equals(response.body())) {
                                        Log.d(TAG, "Find some visited places");
                                        recommendByVisitedBtn.setOnClickListener(ForYouActivity.this::openVisitedRecommender);
                                    } else {
                                        blockRecommendButton(recommendByVisitedBtn);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                                Log.e(TAG, t.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });


        Call<Boolean> checkScores = ServiceGenerator.createService(ScoreService.class).isUserHasScores(AuthorizationHelper.getUserProfile().getId());
        checkScores.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (Boolean.TRUE.equals(response.body())) {
                        Log.d(TAG, "Find some scores of user");
                        recommendByScoredBtn.setOnClickListener(ForYouActivity.this::openScoredRecommender);
                        recommendByOthersBtn.setOnClickListener(ForYouActivity.this::openUsersRecommender);
                    } else {
                        blockRecommendButton(recommendByScoredBtn);
                        Call<Boolean> checkFavourites = ServiceGenerator.createService(UserService.class).isUserHasFavourites();
                        checkFavourites.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                                if (response.code() == 200 && response.body() != null) {
                                    if (Boolean.TRUE.equals(response.body())) {
                                        Log.d(TAG, "Find some favourites places");
                                        recommendByOthersBtn.setOnClickListener(ForYouActivity.this::openUsersRecommender);
                                    } else {
                                        blockRecommendButton(recommendByOthersBtn);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                                Log.e(TAG, t.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            blockRecommendButton(recommendByGPSBtn);
        } else {
            recommendByGPSBtn.setOnClickListener(this::openNearestRecommender);
        }

        recommendRoute.setOnClickListener(this::openRouteRecommender);
    }

    public void openNearestRecommender(View view) {
        recommendByGPSBtn.setImageResource(R.drawable.nearest_rec_btn_selected);
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(SHOW_MAP_MODE_STRING, ShowMapMode.NEAREST.name());
        intent.putExtra(LAST_LOCATION_STRING, lastLocation);
        startActivity(intent);
    }

    public void openVisitedRecommender(View view) {
        recommendByVisitedBtn.setImageResource(R.drawable.btn_by_places_selected);
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(SHOW_MAP_MODE_STRING, ShowMapMode.RECOMMENDED_VISITED.name());
        intent.putExtra(LAST_LOCATION_STRING, lastLocation);
        startActivity(intent);
    }

    public void openScoredRecommender(View view) {
        recommendByScoredBtn.setImageResource(R.drawable.btn_by_prefs_selected);
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(SHOW_MAP_MODE_STRING, ShowMapMode.RECOMMENDED_SCORED.name());
        intent.putExtra(LAST_LOCATION_STRING, lastLocation);
        startActivity(intent);
    }

    public void openUsersRecommender(View view) {
        recommendByOthersBtn.setImageResource(R.drawable.btn_by_users_selected);
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(SHOW_MAP_MODE_STRING, ShowMapMode.RECOMMENDED_USERS.name());
        intent.putExtra(LAST_LOCATION_STRING, lastLocation);
        startActivity(intent);
    }

    public void openRouteRecommender(View view) {
        Log.d(TAG, "Запуск службы по рекомендации маршрута");
        recommendRoute.setImageResource(R.drawable.make_path_btn_selected);

        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(SHOW_MAP_MODE_STRING, ShowMapMode.RECOMMENDED_ROUTE.name());
        intent.putExtra(LAST_LOCATION_STRING, lastLocation);
        startActivity(intent);
    }

    /**
     * Блокирует кнопку вызова рекомендации в случае, если не хватает данных
     * @param imageButton кнопка вызова рекомендации
     */
    private void blockRecommendButton(ImageButton imageButton) {
        imageButton.setClickable(false);
        imageButton.setImageResource(R.drawable.not_available_recommend);
    }

}
