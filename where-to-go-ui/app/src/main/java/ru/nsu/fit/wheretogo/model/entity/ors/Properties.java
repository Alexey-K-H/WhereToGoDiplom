package ru.nsu.fit.wheretogo.model.entity.ors;

import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("summary")
    private Summary summary;

    public Summary getSummary() {
        return summary;
    }
}
