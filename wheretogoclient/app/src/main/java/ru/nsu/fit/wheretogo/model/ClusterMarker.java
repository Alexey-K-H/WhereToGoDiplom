package ru.nsu.fit.wheretogo.model;


import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

import ru.nsu.fit.wheretogo.model.entity.Place;

public class ClusterMarker implements Serializable, ClusterItem {
    private int id;
    private LatLng position;
    private String title;
    private String snippet;
    private Bitmap iconPicture;
    private Place place;

    public ClusterMarker(int id, LatLng position, String title, String snippet, Bitmap iconPicture, Place place) {
        this.id = id;
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.iconPicture = iconPicture;
        this.place = place;
    }

    public ClusterMarker() {
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Bitmap getIconPicture() {
        return iconPicture;
    }

    public void setIconPicture(Bitmap iconPicture) {
        this.iconPicture = iconPicture;
    }


}
