package ru.nsu.fit.wheretogo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.util.AuthorizationHelper;
import ru.nsu.fit.wheretogo.util.ObscuredSharedPreferences;

public class RegistrationActivity extends AppCompatActivity {
    private EditText registerNameText;
    private EditText registerEmailText;
    private EditText registerPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registerNameText = findViewById(R.id.curr_pwd);
        registerEmailText = findViewById(R.id.registerEmailText);
        registerPasswordText = findViewById(R.id.registerPasswordText);
    }

    public void register(View view) {
        String username = registerNameText.getText().toString();
        String email = registerEmailText.getText().toString();
        String password = registerPasswordText.getText().toString();
        if (username.isEmpty()) {
            showNotification(getString(R.string.emptyUsernameMsg));
            return;
        }
        if (email.isEmpty()) {
            showNotification(getString(R.string.emptyEmailMsg));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showNotification(getString(R.string.wrongEmailFormatMsg));
            return;
        }
        AuthorizationHelper.register(
                username,
                email,
                password,
                regSuccessResponse -> AuthorizationHelper.login(
                        email,
                        password,
                        loginSuccessResponse -> {
                            saveAuthData(AuthorizationHelper.getEmail(), AuthorizationHelper.getPassword());
                            openLocationHistoryAdvice();
                        },
                        loginFailResponse -> showUnexpectedErrorMessage(),
                        this::showUnexpectedErrorMessage
                ),
                regFailResponse -> {
                    try {
                        assert regFailResponse.errorBody() != null;
                        String errorMessage = new JSONObject(regFailResponse.errorBody().string()).getString("message");
                        switch (errorMessage) {
                            case "email_already_registered":
                                showNotification(getString(R.string.emailAlreadyRegisteredMsg));
                                break;
                            case "username_already_registered":
                                showNotification(getString(R.string.usernameAlreadyRegisteredMsg));
                                break;
                            default:
                                showUnexpectedErrorMessage();
                        }
                    } catch (JSONException | IOException | NullPointerException e) {
                        showUnexpectedErrorMessage();
                    }
                },
                this::showUnexpectedErrorMessage
        );
    }

    private void openMap() {
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void openLocationHistoryAdvice() {
        finish();
        Intent intent = new Intent(this, LocationHistoryActivity.class);
        intent.putExtra("advice", "location_history");
        startActivity(intent);
    }

    private void showUnexpectedErrorMessage() {
        showNotification(getString(R.string.unexpectedErrorMsg));
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