package ru.nsu.fit.wheretogo.model.ors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geometry {

    @SerializedName("coordinates")
    private List<List<String>> coordinates;

    public List<List<String>> getCoordinates() {
        return coordinates;
    }
}
