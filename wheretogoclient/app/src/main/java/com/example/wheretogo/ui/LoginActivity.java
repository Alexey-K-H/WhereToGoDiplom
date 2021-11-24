package com.example.wheretogo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wheretogo.R;
import com.example.wheretogo.ui.map.MapsActivity;

//TODO:описать класс входа, добавить функцию перенапралвения на карту мест
public class LoginActivity extends AppCompatActivity {

    public void openMap(View view){
        //TODO:дописать класс отображения карты
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
