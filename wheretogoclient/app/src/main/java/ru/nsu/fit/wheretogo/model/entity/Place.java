package ru.nsu.fit.wheretogo.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Place {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("coords")
    @Expose
    private Coords coords;
    @SerializedName("averageScore")
    @Expose
    private Double averageScore;
    @SerializedName("thumbnailLink")
    @Expose
    private String thumbnailLink;
    @SerializedName("uploadedAt")
    @Expose
    private String uploadedAt;
    @SerializedName("categories")
    @Expose
    private List<Category> categories;
    @SerializedName("picturesLinks")
    @Expose
    private List<Object> picturesLinks;

    public Place(Integer id, String name, String description,
                 Coords coords, Double averageScore, String thumbnailLink,
                 String uploadedAt, List<Category> categories,
                 List<Object> picturesLinks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coords = coords;
        this.averageScore = averageScore;
        this.thumbnailLink = thumbnailLink;
        this.uploadedAt = uploadedAt;
        this.categories = categories;
        this.picturesLinks = picturesLinks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Object> getPicturesLinks() {
        return picturesLinks;
    }

    public void setPicturesLinks(List<Object> picturesLinks) {
        this.picturesLinks = picturesLinks;
    }

    public Double getLatitude() {
        return coords.getLatitude();
    }

    public void setLatitude(Double latitude) {
        this.coords.setLatitude(latitude);
    }

    public Double getLongitude() {
        return coords.getLongitude();
    }

    public void setLongitude(Double longitude) {
        this.coords.setLatitude(longitude);
    }

}