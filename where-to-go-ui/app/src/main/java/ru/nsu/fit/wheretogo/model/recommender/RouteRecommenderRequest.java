package ru.nsu.fit.wheretogo.model.recommender;

import com.google.gson.annotations.SerializedName;

import ru.nsu.fit.wheretogo.model.ors.ORSMode;

public class RouteRecommenderRequest {

    @SerializedName("timeLimit")
    private Integer timeLimit;

    @SerializedName("currentUserLocation")
    private LatLonData currentUserLocation;

    @SerializedName("mode")
    private ORSMode mode;

    public RouteRecommenderRequest(Integer timeLimit, LatLonData currentUserLocation, ORSMode mode) {
        this.timeLimit = timeLimit;
        this.currentUserLocation = currentUserLocation;
        this.mode = mode;
    }
}
