package com.example.recipe_app.Model;

import static com.example.recipe_app.Util.DataApi.BASE_URL;

import com.example.recipe_app.Util.ServerAPI;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileModel implements Serializable {
    @SerializedName("user_id")
    String user_id;
    @SerializedName("username")
    String username;
    @SerializedName("photo_profile")
    String photo_profile;
    @SerializedName("email")
    String email;
    @SerializedName("biography")
    String biography;
    @SerializedName("date")
    String date;
    @SerializedName("time")
    String time;

    public ProfileModel(String user_id, String username, String photo_profile, String email, String biography,
                        String date, String time) {
        this.user_id = user_id;
        this.username = username;
        this.photo_profile = photo_profile;
        this.email = email;
        this.biography = biography;
        this.date = date;
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto_profile() {
        return ServerAPI.BASE_URL + "photo_profile/" + photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
