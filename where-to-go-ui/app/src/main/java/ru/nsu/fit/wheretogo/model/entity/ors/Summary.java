package ru.nsu.fit.wheretogo.model.entity.ors;

import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("distance")
    private String distance;

    @SerializedName("duration")
    private String duration;

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }
}
