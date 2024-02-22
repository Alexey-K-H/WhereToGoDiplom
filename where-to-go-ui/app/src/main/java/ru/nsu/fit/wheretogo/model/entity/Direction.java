package ru.nsu.fit.wheretogo.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.nsu.fit.wheretogo.model.entity.ors.Feature;

public class Direction {

    @SerializedName("features")
    private List<Feature> features;

    public List<Feature> getFeatures() {
        return features;
    }
}
