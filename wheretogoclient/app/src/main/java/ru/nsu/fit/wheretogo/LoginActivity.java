package ru.nsu.fit.wheretogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.nsu.fit.wheretogo.map.MapsActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}