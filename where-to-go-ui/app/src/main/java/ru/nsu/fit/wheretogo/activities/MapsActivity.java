package ru.nsu.fit.wheretogo.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.databinding.ActivityMapBinding;
import ru.nsu.fit.wheretogo.model.ClusterMarker;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.ShowMapMode;
import ru.nsu.fit.wheretogo.model.entity.Category;
import ru.nsu.fit.wheretogo.model.entity.Place;
import ru.nsu.fit.wheretogo.model.entity.Score;
import ru.nsu.fit.wheretogo.model.entity.ors.OrsDirectionResponse;
import ru.nsu.fit.wheretogo.service.CategoryService;
import ru.nsu.fit.wheretogo.service.ORSService;
import ru.nsu.fit.wheretogo.service.PlaceService;
import ru.nsu.fit.wheretogo.service.RecommenderService;
import ru.nsu.fit.wheretogo.service.ScoreService;
import ru.nsu.fit.wheretogo.service.UserService;
import ru.nsu.fit.wheretogo.util.ClusterManagerRenderer;
import ru.nsu.fit.wheretogo.util.PictureLoader;
import ru.nsu.fit.wheretogo.util.helper.AuthorizationHelper;
import ru.nsu.fit.wheretogo.util.helper.RouteCallsHandler;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapsSdkInitializedCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int DEFAULT_ZOOM = 13;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String KEY_LOCATION = "location";
    private static final String LAST_LOCATION_STR = "lastLocation";

    private final LatLng defaultLocation = new LatLng(55.008883, 82.938344);
    private final ArrayList<Place> places = new ArrayList<>();
    private final ArrayList<ClusterMarker> clusterMarkers = new ArrayList<>();

    private Map<Integer, CategoryNameChosen> categories;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private ClusterManager<ClusterMarker> clusterManager;
    private ClusterManagerRenderer clusterManagerRenderer;
    private ClusterMarker selectedPlace;
    private GoogleMap map;
    private SlidingPaneLayout mapSlidingPane;
    private LinearLayout filtersLayout;
    private ShowMapMode showMapMode;

    BroadcastReceiver broadcastReceiver;

    private final Callback<List<Place>> placesCallsCallback = new Callback<List<Place>>() {
        @Override
        public void onResponse(@NonNull Call<List<Place>> call, Response<List<Place>> response) {
            Log.d(TAG, "Получен успешный ответ на запрос списка мест:" + response);
            List<Place> placeList = response.body();
            places.clear();
            if (placeList != null) {
                places.addAll(placeList);
            }
            updateMapMarkers();
        }

        @Override
        public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
            Log.d(TAG, "При получении данных произошла ошибка:" + t.getMessage());
        }
    };

    private final Callback<OrsDirectionResponse> routeCallsCallback = new Callback<OrsDirectionResponse>() {
        @Override
        public void onResponse(@NonNull Call<OrsDirectionResponse> call, @NonNull Response<OrsDirectionResponse> response) {
            Log.d(TAG, "Получен успешный ответ на запрос маршрута");
            OrsDirectionResponse route = response.body();
            places.clear();

            if (route != null && route.getFeatures() != null) {
                Log.d(TAG, "Получен маршрут со следующими свойствами:\n" +
                        "Дистанция:" + route.getFeatures().get(0).getProperties().getSummary().getDistance() +
                        "\nВремя передвижения:" + route.getFeatures().get(0).getProperties().getSummary().getDuration());

                List<LatLng> geometry = RouteCallsHandler.decodePolyline(route);
                Log.d(TAG, "Количество геоточек:" + geometry.size());
                drawRoute(geometry);
            } else {
                Log.d(TAG, "Не найдены данные пути");
                showNotification("При получении данных произошла ошибка. Попробуйте повторить попытку позже");
            }
        }

        @Override
        public void onFailure(@NonNull Call<OrsDirectionResponse> call, @NonNull Throwable t) {
            Log.d(TAG, "При получении данных произошла ошибка:" + t.getMessage());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);
        Log.i(TAG, "Инициализация списка категорий");
        getCategories(null, null);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION, Location.class);
            Log.d(TAG, String.format("Последняя известная геопозиция [lat:%s, lon:%s]",
                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
        }

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.d(TAG, "Нет дополнительных параметров, используется модуль отображения \"ALL\"");
            showMapMode = ShowMapMode.ALL;
        } else {
            String mode = extras.getString("show_map_mode");
            if (mode == null) {
                showMapMode = ShowMapMode.ALL;
            }

            showMapMode = ShowMapMode.valueOf(mode);

            if (showMapMode == ShowMapMode.NEAREST) {
                lastKnownLocation = extras.getParcelable(LAST_LOCATION_STR, Location.class);
                Log.d(TAG, "Последняя известная геопозиция: "
                        + lastKnownLocation.getLatitude()
                        + ", "
                        + lastKnownLocation.getLongitude() + ")");
            }

        }


        ru.nsu.fit.wheretogo.databinding.ActivityMapBinding binding =
                ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapSlidingPane = findViewById(R.id.mapSlidingPane);
        mapSlidingPane.setLockMode(SlidingPaneLayout.LOCK_MODE_LOCKED_CLOSED);
        mapSlidingPane.addPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(@NonNull View panel, float slideOffset) {
                //do nothing
            }

            @Override
            public void onPanelOpened(@NonNull View panel) {
                Log.d(TAG, "Открыта панель выбора категорий");
            }

            @Override
            public void onPanelClosed(@NonNull View panel) {
                Log.d(TAG, "Категории выбраны, переход на карту");
                sendPlacesRequest();
            }
        });
        filtersLayout = findViewById(R.id.filtersLayout);


        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        Places.createClient(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        ImageButton recommenderButton = findViewById(R.id.for_you_btn);

        if (showMapMode != ShowMapMode.ALL) {
            recommenderButton.setImageResource(R.drawable.back_from_rec_map);
        }

        ImageButton favoritesButton = findViewById(R.id.favorites_btn);
        ImageButton visitedButton = findViewById(R.id.visited_btn_map);
        ImageButton filters = findViewById(R.id.filters_btn);
        ImageButton userPrefs = findViewById(R.id.user_settings_btn);

        if (showMapMode != ShowMapMode.ALL) {
            recommenderButton.setOnClickListener(view -> onBackPressed());
            userPrefs.setVisibility(View.GONE);
        } else {
            userPrefs.setOnClickListener(this::openUserPrefs);
            recommenderButton.setOnClickListener(this::openRecommenders);
        }

        favoritesButton.setOnClickListener(this::openFavourites);
        visitedButton.setOnClickListener(this::openVisited);

        if (showMapMode != ShowMapMode.ALL && showMapMode != ShowMapMode.NEAREST) {
            filters.setEnabled(false);
            filters.setVisibility(View.INVISIBLE);
        } else {
            filters.setOnClickListener(this::openFilters);
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action!= null && action.equals("logout")) {
                    finish();
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("logout"));
    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (map != null) {
            Log.d(TAG, "Сохранение последней локации и положения экрана");
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.map = map;

        try {
            this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.getApplicationContext(),
                    R.raw.map_without_labels_style));
            Log.d(TAG, "Стиль карты загружен успешно");
        } catch (Exception e) {
            Log.e(TAG, "Не удалось загрузить данные стиля карты из файла JSON");
        }

        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(@NonNull Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        sendPlacesRequest();
    }

    private void sendPlacesRequest() {
        RecommenderService recommenderService = ServiceGenerator.createService(RecommenderService.class);
        ORSService routeService = ServiceGenerator.createService(ORSService.class);

        if (categories.values().stream().noneMatch(categoryNameChosen -> categoryNameChosen.chosen)) {
            categories.forEach((id, categoryNameChosen) -> categoryNameChosen.chosen = true);
        }

        Log.d(TAG, "Список выбранных категорий: "
                + categories.values().stream().map(c -> c.name).collect(Collectors.toList()));

        Call<List<Place>> placesCall = null;
        Call<OrsDirectionResponse> routeCall = null;
        switch (showMapMode) {
            case NEAREST:
                placesCall = recommenderService.getNearestPlacesByCategory(
                        lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude(),
                        1.0,
                        2.0,
                        5,
                        categories.entrySet().stream()
                                .filter(entry -> entry.getValue().chosen)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList()));
                break;
            case RECOMMENDED_VISITED:
                placesCall = recommenderService.getRecommendByVisited();
                break;
            case RECOMMENDED_SCORED:
                placesCall = recommenderService.getRecommendByScores();
                break;
            case RECOMMENDED_USERS:
                placesCall = recommenderService.getRecommendByUsers();
                break;
            case RECOMMENDED_ROUTE:
                routeCall = routeService.getRouteByDriving();
                break;
            default:
                placesCall = recommenderService.getPlaceList(
                        categories.entrySet().stream()
                                .filter(entry -> entry.getValue().chosen)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList()));
                break;
        }

        if (placesCall != null) {
            placesCall.enqueue(placesCallsCallback);
        }

        if (routeCall != null) {
            routeCall.enqueue(routeCallsCallback);
        }
    }

    private void getDeviceLocation() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d(TAG, "GPS недоступен, выставлено значение по умолчанию");
                map.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                map.getUiSettings().setMyLocationButtonEnabled(false);
//                sendPlacesRequest();
            } else {
                if (locationPermissionGranted) {
                    Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            }
                        } else {
                            Log.d(TAG, "Не удалось получить местоположение, используем позицию по умолчанию");
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    });
                } else {
                    Log.d(TAG, "Нет доступа к GPS, используется место по умолчанию.");
                    map.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                    map.getUiSettings().setMyLocationButtonEnabled(false);
//                    sendPlacesRequest();
                }
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            locationPermissionGranted = true;
            updateLocationUI();
        }
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Ошибка при обновлении ui: %s", e.getMessage());
        }
    }

    private void updateMapMarkers() {
        if (map != null) {
            if (clusterManager == null) {
                clusterManager = new ClusterManager<>(this.getApplicationContext(), map);
                clusterManager.setOnClusterItemClickListener(item -> {
                    selectedPlace = item;
                    openShortPlaceInfo();
                    return false;
                });
            }
            if (clusterManagerRenderer == null) {
                clusterManagerRenderer = new ClusterManagerRenderer(
                        this.getApplicationContext(),
                        map,
                        clusterManager
                );
                clusterManager.setRenderer(clusterManagerRenderer);
            }

            clusterManager.clearItems();
            clusterMarkers.clear();
            clusterManager.cluster();
            for (Place place : places) {
                Log.d(TAG, "Добавлен маркер на карту. Позиция:[ "
                        + place.getLatitude().toString()
                        + place.getLongitude().toString() + "]");
                try {
                    PictureLoader.loadPlaceThumbnail(this, place,
                            clusterMarkers, clusterManager);
                } catch (Exception e) {
                    Log.e(TAG, "Ошибка при добавлении маркера:\n" + e.getMessage());
                }
            }
        }
    }

    private void drawRoute(List<LatLng> geometry) {
        PolylineOptions lineOptions = new PolylineOptions()
                .addAll(geometry)
                .width(10f)
                .color(Color.BLUE)
                .geodesic(true)
                .visible(true);

        Log.d(TAG, "Отображение пути на карте");
        Polyline polyline = map.addPolyline(lineOptions);
        Log.d(TAG, String.valueOf(polyline.isVisible()));
    }

    private void openShortPlaceInfo() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MapsActivity.this, R.style.ButtonSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.activity_map_bottom_sheet,
                        findViewById(R.id.bottomSheetContainer)
                );

        fillShortPlaceInfo(bottomSheetView, selectedPlace);

        bottomSheetView.findViewById(R.id.back_btn).setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetView.findViewById(R.id.more_info_btn).setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            openFullPlaceInfo(view);
        });

        bottomSheetView.findViewById(R.id.visited_btn).setOnClickListener(this::addVisitedPlace);

        ImageButton liked = bottomSheetView.findViewById(R.id.favour_btn);
        liked.setOnClickListener(view -> {
            liked.setActivated(true);
            addFavoritePlace(view);
        });

        ImageButton visited = bottomSheetView.findViewById(R.id.visited_btn);
        visited.setOnClickListener(view -> {
            visited.setActivated(true);
            addVisitedPlace(view);
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void fillShortPlaceInfo(View view, ClusterMarker item) {
        TextView placeName = view.findViewById(R.id.place_name);
        placeName.setText(item.getTitle());
        ImageView placeImage = view.findViewById(R.id.place_image);
        placeImage.setImageBitmap(item.getIconPicture());

        ImageButton visited = view.findViewById(R.id.visited_btn);
        ImageButton favourite = view.findViewById(R.id.favour_btn);

        Context context = this;

        PlaceService placeService = ServiceGenerator.createService(PlaceService.class);
        Call<Place> placeCall = placeService.getPlace(item.getId());
        placeCall.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(@NonNull Call<Place> call, @NonNull Response<Place> response) {
                TextView placeDescription = view.findViewById(R.id.place_description);
                if (response.isSuccessful() && response.body() != null) {
                    String descriptionText = response.body().getDescription();
                    descriptionText = descriptionText.substring(0, 1).toUpperCase()
                            + descriptionText.substring(1);
                    placeDescription.setText(descriptionText);

                    selectedPlace.getPlace().setDescription(descriptionText);
                    selectedPlace.setId(item.getId());
                } else {
                    Toast.makeText(context, R.string.unexpectedErrorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Place> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        Call<Boolean> visitedCheckCall = placeService.findVisitedById(item.getPlace().getId());
        visitedCheckCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (Boolean.TRUE.equals(response.body())) {
                        visited.setImageResource(R.drawable.add_to_visited);
                    } else {
                        visited.setImageResource(R.drawable.unknown);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        Call<Boolean> favouriteCheckCall = placeService.findFavouriteById(item.getPlace().getId());
        favouriteCheckCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (Boolean.TRUE.equals(response.body())) {
                        favourite.setImageResource(R.drawable.add_to_favour);
                    } else {
                        favourite.setImageResource(R.drawable.add_to_favour_not_active);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        RatingBar placeScore = view.findViewById(R.id.ratingBar_short_info);
        ScoreService scoreService = ServiceGenerator.createService(ScoreService.class);
        Call<Score> scoreCall = scoreService.getUserPlaceScore(
                AuthorizationHelper.getUserProfile().getId(),
                item.getId());
        scoreCall.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(@NonNull Call<Score> call, @NonNull Response<Score> response) {
                if (response.code() == 200 && response.body() != null) {
                    Long userMark = response.body().getScoreValue();
                    placeScore.setRating(userMark);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Score> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        placeScore.setOnRatingBarChangeListener((ratingBar, v, b) -> {

            Call<Score> call = ServiceGenerator.createService(ScoreService.class)
                    .setScore(AuthorizationHelper.getUserProfile().getId(),
                            selectedPlace.getId(),
                            (int) v);

            call.enqueue(new Callback<Score>() {
                @Override
                public void onResponse(@NonNull Call<Score> call, @NonNull Response<Score> response) {
                    if (response.code() == 200 && response.body() != null) {
                        ratingBar.setRating(response.body().getScoreValue());
                        Call<Boolean> placeInVisited = ServiceGenerator
                                .createService(UserService.class)
                                .isPlaceInUserVisited(selectedPlace.getId());
                        placeInVisited.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(
                                    @NonNull Call<Boolean> call,
                                    @NonNull Response<Boolean> response) {
                                if (response.code() == 200 && response.body() != null
                                        && (Boolean.FALSE.equals(response.body()))) {
                                    addVisitedPlace(view);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                                Log.e(TAG, t.getMessage());
                            }
                        });
                    } else if (response.code() != 400) {
                        Toast.makeText(context, R.string.unexpectedErrorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Score> call, @NonNull Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        });
    }

    public void openFavourites(View view) {
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
    }

    public void openRecommenders(View view) {
//        locationUpdateThread.quit();
        Intent intent = new Intent(this, ForYouActivity.class);
        intent.putExtra(LAST_LOCATION_STR, lastKnownLocation);
        startActivity(intent);
    }

    private void openUserPrefs(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void openFullPlaceInfo(View view) {
        Intent intent = new Intent(this, PlaceActivity.class);
        intent.putExtra("id", selectedPlace.getId().toString());
        intent.putExtra("title", selectedPlace.getTitle());
        intent.putExtra("desc", selectedPlace.getPlace().getDescription());
        intent.putExtra("link", selectedPlace.getPlace().getThumbnailLink());
        startActivity(intent);
    }

    public void addFavoritePlace(View view) {
        Call<String> call = ServiceGenerator.createService(PlaceService.class)
                .addFavoritePlace(selectedPlace.getId());
        Context context = this;
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    ImageButton favour = view.findViewById(R.id.favour_btn);
                    favour.setImageResource(R.drawable.add_to_favour);
                    Toast.makeText(context, "Добавлено в избранное",
                            Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    deleteFavouritePlace(selectedPlace.getId());
                    ImageButton favour = view.findViewById(R.id.favour_btn);
                    favour.setImageResource(R.drawable.add_to_favour_not_active);
                } else {
                    Toast.makeText(context, R.string.unexpectedErrorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void addVisitedPlace(View view) {
        Call<String> call = ServiceGenerator.createService(PlaceService.class)
                .addVisitedPlace(selectedPlace.getId());
        Context context = this;
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    ImageButton visited = view.findViewById(R.id.visited_btn);
                    visited.setImageResource(R.drawable.add_to_visited);
                    Toast.makeText(context, "Добавлено в посещенное",
                            Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    deleteVisitedPlace(selectedPlace.getId());
                    ImageButton visited = view.findViewById(R.id.visited_btn);
                    visited.setImageResource(R.drawable.unknown);
                } else {
                    Toast.makeText(context, R.string.unexpectedErrorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void deleteVisitedPlace(Long placeId) {
        Call<String> call = ServiceGenerator.createService(PlaceService.class)
                .deleteVisited(placeId);
        Context context = this;
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Удалено из посещенного",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.unexpectedErrorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void deleteFavouritePlace(Long placeId) {
        Call<String> call = ServiceGenerator.createService(PlaceService.class)
                .deleteFavourite(placeId);
        Context context = this;
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "Удалено из избранного",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.unexpectedErrorMsg,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void openVisited(View view) {
        Intent intent = new Intent(this, VisitedActivity.class);
        startActivity(intent);
    }

    public void openFilters(View view) {
        filtersLayout.removeAllViews();
        getCategories(() -> {
            List<Integer> categoryIds = new ArrayList<>();
            categories.forEach((id, categoryNameChosen) -> categoryIds.add(id));
            categoryIds.sort(Integer::compareTo);
            categoryIds.forEach(id -> {
                CategoryNameChosen categoryNameChosen = categories.get(id);
                assert categoryNameChosen != null;
                filtersLayout.addView(
                        categoryView(id, categoryNameChosen.name, categoryNameChosen.chosen));
            });
        }, mapSlidingPane::close);
        mapSlidingPane.open();
    }

    private View categoryView(int id, String name, boolean checked) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(checked);
        checkBox.setText(name);
        checkBox.setTextSize(20);
        checkBox.setPadding(10, 10, 10, 10);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked)
                -> categories.put(id, new CategoryNameChosen(name, isChecked)));
        return checkBox;
    }

    private void getCategories(@Nullable Runnable onSuccess, @Nullable Runnable onFail) {
        BooleanWrapper firstFilling = new BooleanWrapper(false);
        if (categories == null) {
            categories = new HashMap<>();
            firstFilling.value = true;
        }
        CategoryService categoryService = ServiceGenerator.createService(CategoryService.class);
        categoryService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call,
                                   @NonNull Response<List<Category>> response) {
                if (response.body() == null) {
                    Log.e(TAG, "Тело запроса категорий пустое");
                    if (onFail != null) {
                        onFail.run();
                    }
                } else {
                    response.body().forEach(category -> {
                        if (firstFilling.value) {
                            categories.put(category.getId(),
                                    new CategoryNameChosen(category.getName(), true));
                        } else {
                            categories.putIfAbsent(category.getId(),
                                    new CategoryNameChosen(category.getName(), true));
                        }
                    });
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Log.e(TAG, "Could not load categories (" + t.getMessage() + ")");
                if (onFail != null) {
                    onFail.run();
                }
            }
        });
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d(TAG, "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d(TAG, "The legacy version of the renderer is used.");
                break;
        }
    }

    private static class BooleanWrapper {
        private boolean value;

        public BooleanWrapper(boolean value) {
            this.value = value;
        }
    }

    private static class CategoryNameChosen {
        private final String name;
        private boolean chosen;

        public CategoryNameChosen(String name, boolean chosen) {
            this.name = name;
            this.chosen = chosen;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (showMapMode != ShowMapMode.ALL) {
            finish();
            Intent intent = new Intent(this, ForYouActivity.class);
            intent.putExtra(LAST_LOCATION_STR, lastKnownLocation);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Завершение работы карты");
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "Уничтожение страницы с картой");

        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }

        super.onDestroy();
    }

    private void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}