package ru.nsu.fit.wheretogo.model.recommender;

import com.google.gson.annotations.SerializedName;

public class LatLonData {

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public LatLonData(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
