package ru.nsu.fit.wheretogo.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.util.tracker.TrackerConstants;
import ru.nsu.fit.wheretogo.util.tracker.LocationTrackerService;

public class LocationHistoryActivity extends AppCompatActivity {

    private static final String TAG = LocationHistoryActivity.class.getSimpleName();
    private ImageButton locationTrackerSwitcher;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private boolean advise = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_location_manager);

        ImageButton skipBtn = (ImageButton) findViewById(R.id.skip_btn_history);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String advice = extras.getString("advice");
            String from = extras.getString("from");
            if (advice != null && advice.equals("location_history")) {
                skipBtn.setOnClickListener(this::skip);
                advise = true;
            }
            if (from != null && from.equals("notification")) {
                Log.d(TAG, "Переход из уведомления в системном теере");
                skipBtn.setVisibility(View.GONE);
            }
        } else {
            skipBtn.setVisibility(View.GONE);
        }

        locationTrackerSwitcher = (ImageButton) findViewById(R.id.location_history_switcher);
        if (isServiceRunning()) {
            Log.d(TAG, "Трекер перемещений работает");
            locationTrackerSwitcher.setImageResource(R.drawable.start_stop_location_history);
        } else {
            Log.d(TAG, "Трекер перемещений не активен");
            locationTrackerSwitcher.setImageResource(R.drawable.start_stop_location_history_not_active);
        }
        locationTrackerSwitcher.setOnClickListener(this::switchLocationTracker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationTracker();
            } else {
                Toast.makeText(this, "Доступ к геоданным отклонен", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void switchLocationTracker(View view) {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (isServiceRunning()) {
            stopLocationTracker();
        } else {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        LocationHistoryActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION_PERMISSION
                );
            } else {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(this, "GPS отключен, запись истории невозможна." +
                            " Включите GPS в настройках устройства", Toast.LENGTH_LONG).show();
                } else {
                    startLocationTracker();
                    if (advise) {
                        finish();
                        Intent intent = new Intent(this, MapsActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    private void startLocationTracker() {
        Intent intent = new Intent(getApplicationContext(), LocationTrackerService.class);
        intent.setAction(TrackerConstants.TRACKER_SERVICE_START);
        startService(intent);
        Toast.makeText(this, "Запись истории перемещений включена", Toast.LENGTH_SHORT).show();
        locationTrackerSwitcher.setImageResource(R.drawable.start_stop_location_history);
        Log.d(TAG, "Запись истории перемещений включена");
    }

    private void stopLocationTracker() {
        Intent intent = new Intent(getApplicationContext(), LocationTrackerService.class);
        intent.setAction(TrackerConstants.TRACKER_SERVICE_STOP);
        startService(intent);
        Toast.makeText(this, "Запись истории перемещений отключена", Toast.LENGTH_SHORT).show();
        locationTrackerSwitcher.setImageResource(R.drawable.start_stop_location_history_not_active);
        Log.d(TAG, "Запись истории перемещений отключена");
    }

    @SuppressWarnings("deprecation")
    private boolean isServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationTrackerService.class.getName().equals(service.service.getClassName())
                    && (service.foreground)) {
                    return true;
            }
        }
        return false;
    }

    public void skip(View view) {
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
