package ru.nsu.fit.wheretogo;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VisitedActivity extends AppCompatActivity {
    private final String[] names = {"Речкуновка", "Корабль", "island_part", "islandia"};
    private final int[] favouriteImages = {
            R.drawable.rechkunova, R.drawable.ship,
            R.drawable.island_part, R.drawable.islandia
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        RecyclerView recycleView = findViewById(R.id.visited_recycler);

        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recycleView.setLayoutManager(layoutManager);
        RecyclerView.Adapter<?> adapter = new RecyclerViewAdapter(this, names,
                favouriteImages, height);
        recycleView.setAdapter(adapter);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}