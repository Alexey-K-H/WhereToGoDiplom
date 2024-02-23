package ru.nsu.fit.wheretogo.model.recommender;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.nsu.fit.wheretogo.model.ors.Direction;
import ru.nsu.fit.wheretogo.model.place.Place;

public class RouteRecommenderResponse {

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
