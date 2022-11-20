package ru.nsu.fit.wheretogo.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("avatarLink")
    @Expose
    private String avatarLink;
    @SerializedName("avatarThumbnailLink")
    @Expose
    private String avatarThumbnailLink;

    public User() {
    }

    public User(Long id, String email, String username, String avatarLink, String avatarThumbnailLink) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.avatarLink = avatarLink;
        this.avatarThumbnailLink = avatarThumbnailLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public String getAvatarThumbnailLink() {
        return avatarThumbnailLink;
    }

    public void setAvatarThumbnailLink(String avatarThumbnailLink) {
        this.avatarThumbnailLink = avatarThumbnailLink;
    }
}
