package ru.nsu.fit.wheretogo.model.entity.ors;

import com.google.gson.annotations.SerializedName;

public class Feature {

    @SerializedName("properties")
    private Properties properties;

    @SerializedName("geometry")
    private Geometry geometry;

    public Properties getProperties() {
        return properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }
}
