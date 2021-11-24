package com.example.startupapp.model.place;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class PlaceLocation {
    private int id;
    private LatLng latLng;
    private Place place;

    public PlaceLocation(int id, LatLng latLng, Place place) {
        this.id = id;
        this.latLng = latLng;
        this.place = place;
    }

    public PlaceLocation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceLocation{" +
                "id=" + id +
                ", latitude=" + latLng.latitude +
                ", longitude=" + latLng.longitude +
                ", place_id=" + place.getPlace_id() +
                '}';
    }
}
