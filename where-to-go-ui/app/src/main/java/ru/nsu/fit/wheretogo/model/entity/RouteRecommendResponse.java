package ru.nsu.fit.wheretogo.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteRecommendResponse {

    @SerializedName("direction")
    private Direction direction;

    @SerializedName("routePlaces")
    private List<Place> routePlaces;

    public Direction getDirection() {
        return direction;
    }

    public List<Place> getRoutePlaces() {
        return routePlaces;
    }
}
