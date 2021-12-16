package ru.nsu.fit.wheretogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.util.AuthorizationHelper;

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
        String email = loginEmailText.getText().toString();
        String password = loginPasswordText.getText().toString();
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
                successResponse -> openMap(),
                failResponse -> showNotification(getString(R.string.wrongCredentialsMsg)),
                () -> showNotification(getString(R.string.unexpectedErrorMsg))
        );
    }

    private void openMap() {
        //finish();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}