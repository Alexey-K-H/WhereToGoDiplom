package ru.nsu.fit.wheretogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.util.AuthorizationHelper;

public class RegistrationActivity extends AppCompatActivity {
    private EditText registerNameText;
    private EditText registerEmailText;
    private EditText registerPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registerNameText = findViewById(R.id.registerNameText);
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
                        loginSuccessResponse -> openMap(),
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

    private void showUnexpectedErrorMessage() {
        showNotification(getString(R.string.unexpectedErrorMsg));
    }

    private void showNotification(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}