package ru.nsu.fit.wheretogo.recommenders;

import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.nsu.fit.wheretogo.RecyclerViewAdapter;
import ru.nsu.fit.wheretogo.model.entity.Place;
import ru.nsu.fit.wheretogo.util.PictureLoader;

//Абстрактный класс для описнаия рекомендателя по тому или иному критерию
public abstract class Recommender extends AppCompatActivity {
    private RecyclerView recyclerView;

    private void initView(List<Place> places) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, height);
        recyclerView.setAdapter(adapter);
        PictureLoader.loadRecyclerPictures(this, places, adapter);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
