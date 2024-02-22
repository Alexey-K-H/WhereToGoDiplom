package ru.nsu.fit.wheretogo.model.entity.ors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrsDirectionResponse {

    @SerializedName("features")
    private List<Feature> features;

    public List<Feature> getFeatures() {
        return features;
    }
}
