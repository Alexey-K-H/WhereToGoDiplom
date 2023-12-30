package ru.nsu.fit.wheretogo.util.tracker;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.fit.wheretogo.activities.LocationHistoryActivity;
import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.model.ServiceGenerator;
import ru.nsu.fit.wheretogo.service.StayPointService;

public class LocationTrackerService extends Service {
    private static final String TAG = "LocationTracker";

    /**
     * Временной интервал обновления местоположения пользователя (мс)
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = (15 * 60 * 1000);
    /**
     * Радиус области вокруг геоточки
     */
    private static final double AREA_RADIUS = 200.00;
    private boolean locationPermissionGranted;

    private LocationRequest locationRequest;
    private final HandlerThread locationUpdateThread = new HandlerThread(TAG);
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (mLocation == null) {
                mLocation = locationResult.getLastLocation();
            }
            double distance = Objects.requireNonNull(locationResult.getLastLocation()).distanceTo(mLocation);
            if (distance <= AREA_RADIUS) {
                Log.d(TAG, "Найдена геоточка дальше " + AREA_RADIUS + " от текущей");
                countSameFindings += 1;
            } else {
                countSameFindings = 0;
                mLocation = locationResult.getLastLocation();
            }

            if (countSameFindings == 2) {
                Log.d(TAG, "Найден кандидат на новую точку останова");
                countSameFindings = 0;

                Call<String> call = ServiceGenerator.createService(StayPointService.class).addStayPoint(
                        mLocation.getLatitude(),
                        mLocation.getLongitude());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.d(TAG, "Успешно добавлена новая точка останова");
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });

            }
        }
    };

    private long countSameFindings = 0;

    private Location mLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(TrackerConstants.TRACKER_SERVICE_START)) {
                    startLocationService();
                } else if (action.equals(TrackerConstants.TRACKER_SERVICE_STOP)) {
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
        Log.d(TAG, "Запись истории перемещений отключена.");
        super.onDestroy();
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this, LocationHistoryActivity.class);
        resultIntent.putExtra("from", "notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_MUTABLE
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );

        builder.setSmallIcon(R.drawable.add_to_visited);
        builder.setContentText("Запущена запись истории ваших перемещений");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (notificationManager != null
                && notificationManager.getNotificationChannel(channelId) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId,
                    "Location Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel.setDescription("This channel is used by location service");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        getLocationPermission();
        if (locationPermissionGranted) {
            locationUpdateThread.start();
        }

        startForeground(TrackerConstants.LOCATION_SERVICE_ID, builder.build());
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();
        locationUpdateThread.quit();
    }

}
