package ru.nsu.fit.wheretogo.activities.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.R;
import ru.nsu.fit.wheretogo.activities.map.LocationHistoryActivity;
import ru.nsu.fit.wheretogo.activities.map.MapsActivity;
import ru.nsu.fit.wheretogo.util.ObscuredSharedPreferences;
import ru.nsu.fit.wheretogo.util.helper.AuthorizationHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailText;
    private EditText loginPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmailText = findViewById(R.id.loginEmailText);
        loginPasswordText = findViewById(R.id.loginPasswordText);
    }

    public void login(View view) {
        var email = loginEmailText.getText().toString();
        var password = loginPasswordText.getText().toString();
        if (email.isEmpty()) {
            showNotification(getString(R.string.emptyEmailMsg));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showNotification(getString(R.string.wrongEmailFormatMsg));
            return;
        }
        AuthorizationHelper.login(
                email,
                password,
                successResponse -> {
                    saveAuthData(AuthorizationHelper.getEmail(), AuthorizationHelper.getPassword());
                    openLocationHistoryAdvice();
                },
                failResponse -> showNotification(getString(R.string.wrongCredentialsMsg)),
                () -> showNotification(getString(R.string.unexpectedErrorMsg))
        );
    }

    private void openMap() {
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void openLocationHistoryAdvice() {
        finish();
        var intent = new Intent(this, LocationHistoryActivity.class);
        intent.putExtra("advice", "location_history");
        startActivity(intent);
    }

    private void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void saveAuthData(String email, String password) {
//        SharedPreferences sharedPreferences = getSharedPreferences("AUTH_DATA", MODE_PRIVATE);

        SharedPreferences sharedPreferences = new ObscuredSharedPreferences(
                this, this.getSharedPreferences("AUTH_DATA", MODE_PRIVATE)
        );

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }
}