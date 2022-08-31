package com.example.recipe_app.Admin.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeReportmodel implements Serializable {
    @SerializedName("user_id")
    String user_id;
    @SerializedName("report_id")
    String report_id;
    @SerializedName("recipe_id")
    String recipe_id;
    @SerializedName("username")
    String username;
    @SerializedName("photo_profile")
    String photo_profile;
    @SerializedName("email")
    String email;
    @SerializedName("title")
    String title;
    @SerializedName("report")
    String  report;
    @SerializedName("status")
    String status;
    @SerializedName("date")
    String date;
    @SerializedName("time")
    String time;
    @SerializedName("image")
    String image;
    @SerializedName("success")
    String succes;


    public RecipeReportmodel (String user_id, String report_id, String recipe_id, String username, String photo_profile, String email,
                              String title, String report, String status, String date, String time, String image, String succes) {
        this.user_id = user_id;
        this.report_id = report_id;
        this.recipe_id = recipe_id;
        this.username = username;
        this.photo_profile = photo_profile;
        this.email = email;
        this.title = title;
        this.report = report;
        this.status = status;
        this.date = date;
        this.time = time;
        this.image = image;
        this.succes = succes;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto_profile() {
        return photo_profile;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getImage() {
        return image;
    }

    public String getSucces() {
        return succes;
    }

    public void setSucces(String succes) {
        this.succes = succes;
    }

    public void setImage(String image) {
        this.image = image;



    }
}
