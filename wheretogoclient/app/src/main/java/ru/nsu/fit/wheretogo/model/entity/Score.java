package ru.nsu.fit.wheretogo.model.entity;

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
    private Long score;

    public Score(Long userId, Long placeId, Long score) {
        this.userId = userId;
        this.placeId = placeId;
        this.score = score;
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

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
