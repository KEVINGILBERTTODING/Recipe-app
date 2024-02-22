package com.example.recipe_app.Model;

import com.example.recipe_app.data.remote.ServerAPI;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileModel implements Serializable {
    @SerializedName("id")
    String id;
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
    @SerializedName("message")
    String status;
    @SerializedName("status")
    String message;
    @SerializedName("success")
    String success;
    @SerializedName("following_id")
    Integer following_id;
    @SerializedName("followers_id")
    Integer followers_id;
    @SerializedName("verified")
    String verified;

    public ProfileModel(String user_id, String username, String photo_profile, String email, String biography,
                        String date, String time, String status, String message, String success, Integer following_id,
                        Integer followers_id, String id, String verified) {
        this.user_id = user_id;
        this.username = username;
        this.photo_profile = photo_profile;
        this.email = email;
        this.biography = biography;
        this.date = date;
        this.time = time;
        this.status = status;
        this.message = message;
        this.success = success;
        this.followers_id = followers_id;
        this.following_id = following_id;
        this.id = id;
        this.verified = verified;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Integer getFollowing_id() {
        return following_id;
    }

    public void setFollowing_id(Integer following_id) {
        this.following_id = following_id;
    }

    public Integer getFollowers_id() {
        return followers_id;
    }

    public void setFollowers_id(Integer followers_id) {
        this.followers_id = followers_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
}
