package com.example.recipe_app.Model;

import com.example.recipe_app.Util.DataApi;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationModel implements Serializable {


    @SerializedName("notif_id")
    String notif_id;
      @SerializedName("user_id")
    String user_id;
      @SerializedName("user_id_notif")
    String user_id_notif;
      @SerializedName("recipe_id")
    String recipe_id;
      @SerializedName("type")
    String type;
      @SerializedName("date")
    String date;
      @SerializedName("time")
    String time;
      @SerializedName("status")
    Integer status;
      @SerializedName("username")
    String username;
      @SerializedName("photo_profile")
    String photo_profile;
     @SerializedName("title")
    String title;
     @SerializedName("image")
    String image;
     @SerializedName("comment")
     String comment;
     @SerializedName("success")
     String success;



    public NotificationModel(String notif_id, String user_id, String user_id_notif, String recipe_id, String type, String date,
                             String time, Integer status, String username, String photo_profile,
                             String title, String image, String comment, String success) {
        this.notif_id = notif_id;
        this.user_id = user_id;
        this.user_id_notif = user_id_notif;
        this.recipe_id = recipe_id;
        this.type = type;
        this.date = date;
        this.time = time;
        this.status = status;
        this.username = username;
        this.photo_profile = photo_profile;
        this.title = title;
        this.image = image;
        this.comment =comment;
        this.success = success;
    }

    public String getNotif_id() {
        return notif_id;
    }

    public void setNotif_id(String notif_id) {
        this.notif_id = notif_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id_notif() {
        return user_id_notif;
    }

    public void setUser_id_notif(String user_id_notif) {
        this.user_id_notif = user_id_notif;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto_profile() {
        return DataApi.BASE_URL +"photo_profile/" + photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return DataApi.BASE_URL + "recipe_images/" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
