package ru.nsu.fit.wheretogo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.map.MapsActivity;
import ru.nsu.fit.wheretogo.util.AuthorizationHelper;
import ru.nsu.fit.wheretogo.util.ObscuredSharedPreferences;

public class AuthorizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        //Check auth data
//        SharedPreferences sharedPreferences = getSharedPreferences("AUTH_DATA", MODE_PRIVATE);

//        SharedPreferences sharedPreferences = new ObscuredSharedPreferences(
//                this, this.getSharedPreferences("AUTH_DATA", MODE_PRIVATE)
//        );
//
//        String email = sharedPreferences.getString("email", "");
//        String password = sharedPreferences.getString("password", "");
//        if(!email.equals("") && !password.equals("")){
//            AuthorizationHelper.login(
//                    email,
//                    password,
//                    successResponse -> {
//                        openMap();
//                    },
//                    failResponse -> showNotification(getString(R.string.wrongCredentialsMsg)),
//                    this::showUnexpectedErrorMessage
//            );
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void openLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openRegister(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
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