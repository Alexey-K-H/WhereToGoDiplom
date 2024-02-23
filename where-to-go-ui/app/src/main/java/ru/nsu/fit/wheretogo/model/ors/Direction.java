package ru.nsu.fit.wheretogo.model.ors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Direction {

    @SerializedName("features")
    private List<Feature> features;

    public List<Feature> getFeatures() {
        return features;
    }
}
