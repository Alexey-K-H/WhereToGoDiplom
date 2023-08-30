package ru.nsu.fit.wheretogo.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.util.tracker.TrackerConstants;
import ru.nsu.fit.wheretogo.util.tracker.LocationTrackerService;
import ru.nsu.fit.wheretogo.util.helper.AuthorizationHelper;
import ru.nsu.fit.wheretogo.util.ObscuredSharedPreferences;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = AccountActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TextView nameLabel;
        ImageButton editProfileBtn;
        ImageButton logoutBtn;
        ImageButton locationHistoryBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        locationHistoryBtn = findViewById(R.id.location_history);
        logoutBtn = findViewById(R.id.logout);
        editProfileBtn = findViewById(R.id.edit_profile);

        locationHistoryBtn.setOnClickListener(this::openLocationHistoryManager);
        editProfileBtn.setOnClickListener(this::openAccountEditor);
        logoutBtn.setOnClickListener(this::logout);

        nameLabel = findViewById(R.id.name_label);
        nameLabel.setText(AuthorizationHelper.getUserProfile().getUsername());
    }

    public void openLocationHistoryManager(View view) {
        Intent intent = new Intent(this, LocationHistoryActivity.class);
        startActivity(intent);
    }

    public void openAccountEditor(View view) {
        Intent intent = new Intent(this, AccountEditActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        AuthorizationHelper.logout(
                successResponse -> {
                    Log.d(TAG, "Clear shared preferences");
                    SharedPreferences sharedPreferences = new ObscuredSharedPreferences(
                            this, this.getSharedPreferences("AUTH_DATA", MODE_PRIVATE)
                    );

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", "");
                    editor.putString("password", "");
                    editor.apply();

                    Log.d(TAG, "Stop service location tracker if it runs...");
                    if (isServiceRunning()) {
                        Log.d(TAG, "Need to stop running service");
                        stopLocationTracker();
                    }

                    Intent intentToMap = new Intent("logout");
                    sendBroadcast(intentToMap);

                    finish();
                    Intent intent = new Intent(this, AuthorizationActivity.class);
                    startActivity(intent);
                },
                failResponse -> showNotification("Произошла ошибка при выходе"),
                () -> showNotification(getString(R.string.unexpectedErrorMsg))
        );
    }

    private void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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

    private void stopLocationTracker() {
        Intent intent = new Intent(getApplicationContext(), LocationTrackerService.class);
        intent.setAction(TrackerConstants.TRACKER_SERVICE_STOP);
        startService(intent);
        Log.d(TAG, "Запись истории перемещений отключена");
    }
}
