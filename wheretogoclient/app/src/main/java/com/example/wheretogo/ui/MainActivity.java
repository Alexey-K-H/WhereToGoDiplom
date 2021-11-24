package com.example.wheretogo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wheretogo.R;
import com.example.wheretogo.ui.map.MapsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMap(View view){
        //TODO:дописать класс отображения карты
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openLogin(View view){
        //open login window
        //TODO:дописать класс отображения окна входа
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openRegister(View view){
        //open register window
        //TODO:дописать класс отображения окна регистрации
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}