package com.example.wheretogo.model.place;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class Place implements Serializable {

    private static final String TAG = Place.class.getSimpleName();
    private int place_id;
    private String name;
    private String description;
    private String thumbnail;
    private Date upload_at;

    //TODO:потом это нужно убрать, так как картинки должны храниться на сервере
    private String avatar;
    public String getAvatar() {
        return avatar;
    }

    public Place(int place_id,
                 String name,
                 String description,
                 String thumbnail,
                 String upload_at,
                 String avatar) {
        this.place_id = place_id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.upload_at = FromStr2Date(upload_at);

        this.avatar = avatar;
    }

    public Place() {}

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getUpload_at() {
        return upload_at;
    }

    public void setUpload_at(Date upload_at) {
        this.upload_at = upload_at;
    }

    private Date FromStr2Date(String str){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date date;
        try{
            date = formatter.parse(str);
            return date;
        }
        catch (ParseException e){
            Log.d(TAG, "Wrong format for date, set null");
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Place{" +
                "place_id='" + place_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", upload_at='" + upload_at.toString() + '\'' +
                '}';
    }
}
