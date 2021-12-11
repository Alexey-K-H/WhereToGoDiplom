package ru.nsu.fit.wheretogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.nsu.fit.wheretogo.map.MapsActivity;


public class ForYouActivity extends AppCompatActivity {

    ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_you);

        back = (ImageButton) findViewById(R.id.back_recomm);
        back.setOnClickListener(this::closeRecommender);
    }

    private void openMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void closeRecommender(View view){
        finish();
        openMap(view);
    }

}
