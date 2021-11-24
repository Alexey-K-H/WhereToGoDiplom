package com.example.wheretogo.ui.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.wheretogo.R;
import com.example.wheretogo.databinding.ActivityMapBinding;
import com.example.wheretogo.model.marker.ClusterMarker;
import com.example.wheretogo.model.place.Place;
import com.example.wheretogo.model.place.PlaceLocation;
import com.example.wheretogo.util.ClusterManagerRenderer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

//TODO:описать класс карт, добавить маркеры в виде 6 фотографий
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = MapsActivity.class.getSimpleName();

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    //Default location (Novosibirsk Rechnoy Vokzal)
    private final LatLng defaultLocation = new LatLng(55.008883, 82.938344);
    private static final int DEFAULT_ZOOM = 10;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private GoogleMap map;

    //vars for markers
    private final ArrayList<Place> mPlaceList = new ArrayList<>();
    private final ArrayList<PlaceLocation> mPlaceLocations = new ArrayList<>();

    //adding markers on map
    private ClusterManager<ClusterMarker> mClusterManager;
    private ClusterManagerRenderer mClusterManagerRenderer;
    private final ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();

    //current_selected_place
    private ClusterMarker selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        com.example.wheretogo.databinding.ActivityMapBinding binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        // The entry point to the Places API.
        Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //Init massive of 6 places and their locations
        //TODO:продумать, как они будут заполняться в случае работы с сервером

        if(mPlaceLocations.size() == 0){
            //TODO:мы должны в идеале брать инфу о местах из json файлов и парсить ее в констурктор

            //create array of places
            mPlaceList.add(new Place(1,
                    "Кусочек Исландии",
                    "Побережье Новосибирского водохранилища c холмистой местностью" +
                            " напоминает сказочную природу Исландии и подходит для уютных пикников" +
                            " и семейных прогулок.",
                    "unknown",
                    "дата появления",
                    String.valueOf(R.drawable.island_part)));
            mPlaceList.add(new Place(2,
                    "Беловский водопад",
                    "Беловский водопад расположен в Искитимском районе — недалеко от" +
                            " села Белово. Бурлящий поток воды, срывающийся с пятиметровой высоты," +
                            " не свойственен для равнинной местности, поэтому после своего" +
                            " загадочного появления водопад быстро превратился в" +
                            " достопримечательность. Беловский водопад окружает живописный березовый" +
                            " лес и просторные поляны. Сказочным видом водопада можно любоваться как" +
                            " летом, наблюдая за стремительным потоком воды, так и зимой, рассматривая" +
                            " причудливые формы застывших волн.",
                    "https://static.tildacdn.com/tild6132-3661-4666-a331-333162643065/64.png",
                    "дата появления",
                    String.valueOf(R.drawable.waterfall)));
            mPlaceList.add(new Place(3,
                    "Мира Парк",
                    "Необычный парк расположился в 20 км от Новосибирска." +
                            " «Мира парк» — совершенно новое пространство для нашего города." +
                            " Он делится на два больших направления:" +
                            " «Парк познания» и «Парк активити».Первое — пространство для" +
                            " медитативного отдыха. Центром всего «Парка познания» является" +
                            " «монумент Истины», окруженный критским лабиринтом. " +
                            "Говорят, пройдя его от начала до конца, человек находит путь к себе. " +
                            "Активити-парк объединяет объекты, которые ведут к самопознанию " +
                            "через активность: веревочный и батутный парки, памп-трек параллельной" +
                            " гонки, цирковая трапеция, баланс-парк и другие зоны.",
                    "https://static.tildacdn.com/tild3636-3836-4431-a234-376666626565/15.png",
                    "дата появления",
                    String.valueOf(R.drawable.mirapark)));
            mPlaceList.add(new Place(4,
                    "Затопленный корабль",
                    "Более 150-ти барж, катеров, теплоходов и пароходов мертвым грузом" +
                            " лежат на дне и по берегам на огромном участке Обской акватории" +
                            " от Алтая до Салехарда. Вот и это заброшенное судно, отслужившее" +
                            " на воде более полувека, теперь навсегда приковано к берегу.",
                    "https://static.tildacdn.com/tild3735-6462-4737-a264-613638373635/___1.png",
                    "дата появления",
                    String.valueOf(R.drawable.ship)));
            mPlaceList.add(new Place(5,
                    "Заброшенная ГЭС",
                    "Место, поражающее своими масштабами." +
                            " Эта гидроэлектростанция была построена в 1947 году, однако сейчас она" +
                            " уже не выполняет своих функций. ГЭС забросили много лет назад, оставив" +
                            " только дамбу и руины кирпичных построек.",
                    "unknown",
                    "дата появления",
                    String.valueOf(R.drawable.gess)));
            mPlaceList.add(new Place(6,
                    "Речкуновка",
                    "Густой сосновый лес с одной стороны, с другой — завораживающий" +
                            " Бердский залив. В этом живописном месте расположились детские лагеря" +
                            " и бывший оздоровительный санаторий «Речкуновский». В советское время" +
                            " он считался лучшим в стране — сюда направляли за большие заслуги" +
                            " сотрудников промышленности и хозяйства. Дворец здоровья закрыт" +
                            " в 2004 году, а здания так и не нашли нового хозяина.",
                    "unknown",
                    "дата появления",
                    String.valueOf(R.drawable.rechkunova)));

            mPlaceList.add(new Place(7,
                    "Страшно красивое место",
                    "Новосибирский крематорий",
                    "https://static.tildacdn.com/tild3262-3264-4764-a133-363061626231/57.png",
                    "дата появления",
                    String.valueOf(R.drawable.crematori)
                    ));

            mPlaceList.add(new Place(8,
                    "Новое яркое пятно в городе",
                    "Новое колесо обозрения в Новосибирске",
                    "https://static.tildacdn.com/tild3835-6161-4134-b635-616661363765/104.png",
                    "дата появления",
                    String.valueOf(R.drawable.ring_view)));

            mPlaceList.add(new Place(9,
                    "Реконструкция старого города",
                    "Недостроенный исторический комплекс в Бердске",
                    "https://static.tildacdn.com/tild3564-3537-4533-b732-313634313034/58.png",
                    "дата появления",
                    String.valueOf(R.drawable.history_city)));

            //create array of place locations
            mPlaceLocations.add(new PlaceLocation(1,
                    new LatLng(54.550778,  82.614194),
                    mPlaceList.get(0)));
            mPlaceLocations.add(new PlaceLocation(2,
                    new LatLng(54.560744, 83.622340),
                    mPlaceList.get(1)));
            mPlaceLocations.add(new PlaceLocation(3,
                    new LatLng(55.1984817, 83.0831566),
                    mPlaceList.get(2)));
            mPlaceLocations.add(new PlaceLocation(4,
                    new LatLng(54.539667, 82.471889),
                    mPlaceList.get(3)));
            mPlaceLocations.add(new PlaceLocation(5,
                    new LatLng(54.850020, 84.865293),
                    mPlaceList.get(4)));
            mPlaceLocations.add(new PlaceLocation(6,
                    new LatLng(54.792613, 83.097182),
                    mPlaceList.get(5)));
            mPlaceLocations.add(new PlaceLocation(7,
                    new LatLng(55.07472, 83.063172),
                    mPlaceList.get(6)));
            mPlaceLocations.add(new PlaceLocation(8,
                    new LatLng(55.00626,  82.93973),
                    mPlaceList.get(7)));
        }
    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
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

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(@NonNull Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        //TODO:продумать, как это будет работать в случае с сервером
        //Display icons of the places
        addMapMarkers();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
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

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                updateLocationUI();
            }
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
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
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void addMapMarkers(){
        if(map != null){
            if(mClusterManager == null){
                mClusterManager = new ClusterManager<>(this.getApplicationContext(), map);
                mClusterManager.setOnClusterItemClickListener(item -> {
                    //TODO:вставить сюда код, который отвечает за реакцию на нажатие маркера
                    selectedPlace = item;
                    openShortPlaceInfo(selectedPlace);
                    return false;
                });
            }
            if(mClusterManagerRenderer == null){
                mClusterManagerRenderer = new ClusterManagerRenderer(
                        this.getApplicationContext(),
                        map,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            for(PlaceLocation placeLocation: mPlaceLocations){

                Log.d(TAG, "addMapMarkers: location: " + placeLocation.getLatLng().toString());
                try{
                    //TODO:подумать, что писать в подзаголоовк маркера (либо его убрать)
                    String snippet = "на основе вашего местоположения";

                    int avatar = R.drawable.unknown; // set the default avatar
                    try{
                        avatar = Integer.parseInt(placeLocation.getPlace().getAvatar());
                    }catch (NumberFormatException e){
                        Log.d(TAG, "addMapMarkers: no avatar for " + placeLocation.getPlace().getName() + ", setting default.");
                    }
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(placeLocation.getLatLng().latitude, placeLocation.getLatLng().longitude),
                            placeLocation.getPlace().getName(),
                            snippet,
                            avatar,
                            placeLocation.getPlace()
                    );
                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);
                }catch (NullPointerException e){
                    Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage() );
                }
            }
            mClusterManager.cluster();
        }
    }

    private void openShortPlaceInfo(ClusterMarker item){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MapsActivity.this, R.style.ButtonSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.activity_map_bottom_sheet,
                        findViewById(R.id.bottomSheetContainer)
                );

        fillShortPlaceInfo(bottomSheetView, item);

        bottomSheetView.findViewById(R.id.back_btn).setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetView.findViewById(R.id.more_info_btn).setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            openFullPlaceInfo(view);
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void fillShortPlaceInfo(View view, ClusterMarker item){
        TextView placeName = view.findViewById(R.id.place_name);
        placeName.setText(item.getTitle());
        ImageView placeImage = view.findViewById(R.id.place_image);
        placeImage.setImageResource(item.getIconPicture());
    }

    public void openFullPlaceInfo(View view){
        //TODO:продумать как делаются переходы на окно полной информации
        Intent intent = new Intent(this, PlaceActivity.class);
        intent.putExtra(Place.class.getSimpleName(), selectedPlace.getPlace());
        startActivity(intent);
    }

}
