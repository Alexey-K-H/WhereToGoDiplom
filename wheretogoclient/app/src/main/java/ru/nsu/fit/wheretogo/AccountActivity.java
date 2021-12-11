package ru.nsu.fit.wheretogo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        back = (ImageButton) findViewById(R.id.back_user_prefs);
        back.setOnClickListener(this::closeSettings);
    }

    private void closeSettings(View view){
        finish();
    }
}
