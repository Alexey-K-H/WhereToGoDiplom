package ru.nsu.fit.wheretogo.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Score {

    @SerializedName("author")
    @Expose
    private Long userId;

    @SerializedName("place")
    @Expose
    private Long placeId;

    @SerializedName("score")
    @Expose
    private Long scoreValue;

    public Score(Long userId, Long placeId, Long scoreValue) {
        this.userId = userId;
        this.placeId = placeId;
        this.scoreValue = scoreValue;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Long scoreValue) {
        this.scoreValue = scoreValue;
    }
}
