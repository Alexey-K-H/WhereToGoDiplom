package ru.nsu.fit.wheretogo.model.service.location_track;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.LocationHistoryActivity;
import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.model.service.StayPointService;

public class LocationTrackerService extends Service {
    private static final String TAG = "LocationTracker";

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 2;//2 seconds
    private static final int HALF_OUR_SEC = 1800;
    private boolean locationPermissionGranted;

    private LocationRequest locationRequest;
    private final HandlerThread locationUpdateThread = new HandlerThread("LocationTracker");
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            //Получаем текущее метосположение пользователя
            if (locationResult != null) {
                //Сравниваем его с предыдущим местоположением
                if(mLocation == null){
                    mLocation = locationResult.getLastLocation();
                }
                double distance = locationResult.getLastLocation().distanceTo(mLocation);
                //Если расстояние между ними меньше 200м, то прибавляем к счетчику время
                //TODO:Изменить расстояние на то, которое используется при поиске ближаших мест в процедуре на сервисе
                if(distance <= 200.00){
                    Log.d(TAG, "Find point inside 200-meters area");
                    timeCounter += 2;
                }
                else {
                    //Сбрасываем счетчик
                    timeCounter = 0;
                    //Обновляем местоположение
                    mLocation = locationResult.getLastLocation();
                }

                //Если счетчик переполнился, то мы нашли stay-point
                if(timeCounter == HALF_OUR_SEC){
                    Log.d(TAG, "Find new stay-point candidate");
                    timeCounter = 0;

                    //Добавляем его в базу данных
                    Call<String> call = ServiceGenerator.createService(StayPointService.class).addStayPoint(
                            mLocation.getLatitude(),
                            mLocation.getLongitude());

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            Log.d(TAG, "Add new stay point");
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                        }
                    });

                }
            }
        }
    };

    //Заглшуки для фиксирования stay-point-ов
    private long timeCounter = 0;

    private Location mLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            if(action != null){
                if(action.equals(Constants.TRACKER_SERVICE_START)){
                    startLocationService();
                }else if(action.equals(Constants.TRACKER_SERVICE_STOP)){
                    stopLocationService();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    locationUpdateThread.getLooper());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "WhereToGo: Запись истории перемещений отключена.");
        super.onDestroy();
    }

    private void startLocationService(){
        String channelId = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this, LocationHistoryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );

        //TODO:Если будет время добавить на уведомление кнопку остановки работы сервиса
        builder.setSmallIcon(R.drawable.add_to_visited);
        builder.setContentText("Запущена запись истории ваших перемещений");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if(notificationManager != null
                    && notificationManager.getNotificationChannel(channelId) == null){
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId,
                    "Location Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel.setDescription("This channel is used by location service");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //Configure updates of location
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        getLocationPermission();
        if(locationPermissionGranted){
            locationUpdateThread.start();
        }

        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());
    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
        locationUpdateThread.quit();
    }

}
