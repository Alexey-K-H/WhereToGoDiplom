package ru.nsu.fit.wheretogo.model.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Place {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("thumbnailLink")
    @Expose
    private String thumbnailLink;
    @SerializedName("uploadedAt")
    @Expose
    private String uploadedAt;
    @SerializedName("categories")
    @Expose
    private List<Category> categories;

    public Place(Long id, String name, String description,
                 Coordinates coordinates, String thumbnailLink,
                 String uploadedAt, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.coordinates = coordinates;
        this.thumbnailLink = thumbnailLink;
        this.uploadedAt = uploadedAt;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
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

    public Double getLatitude() {
        return coordinates.getLatitude();
    }

    public void setLatitude(Double latitude) {
        this.coordinates.setLatitude(latitude);
    }

    public Double getLongitude() {
        return coordinates.getLongitude();
    }

    public void setLongitude(Double longitude) {
        this.coordinates.setLatitude(longitude);
    }

    @NonNull
    @Override
    public String toString() {
        return "\nname: " + getName() + "\n" +
                "description: " + getDescription() + "\n" +
                "cords: " + getCoordinates().toString() + "\n" +
                "thumbnailLink: " + getThumbnailLink() + "\n" +
                "uploadedAt: " + getUploadedAt();
    }
}