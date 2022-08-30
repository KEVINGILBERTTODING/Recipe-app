package com.example.recipe_app.Admin.Model;

import com.example.recipe_app.Util.DataApi;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserReportModel implements Serializable {
    @SerializedName("report_id")
    String report_id;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("username1")
    String username1;
    @SerializedName("photo_profile1")
    String photo_profile1;
    @SerializedName("email1")
    String email1;
    @SerializedName("username2")
    String username2;
    @SerializedName("photo_profile2")
    String photo_profile2;
    @SerializedName("email2")
    String email2;
    @SerializedName("report")
    String report;
    @SerializedName("date")
    String date;
    @SerializedName("time")
    String time;
    @SerializedName("image")
    String image;
    @SerializedName("status")
    String status;
    @SerializedName("user_id_report")
    String user_id_report;
    @SerializedName("title")
    String title;

    public UserReportModel(String user_id, String username1, String photo_profile1, String email1, String username2,
                           String photo_profile2, String email2, String report, String date, String time,
                           String image, String status, String user_id_report, String title, String report_id) {
        this.user_id = user_id;
        this.username1 = username1;
        this.photo_profile1 = photo_profile1;
        this.email1 = email1;
        this.username2 = username2;
        this.photo_profile2 = photo_profile2;
        this.email2 = email2;
        this.report = report;
        this.date = date;
        this.time = time;
        this.image = image;
        this.status = status;
        this.user_id_report = user_id_report;
        this.title = title;
        this.report_id = report_id;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getPhoto_profile1() {
        return DataApi.BASE_URL + "photo_profile/" +  photo_profile1;
    }

    public void setPhoto_profile1(String photo_profile1) {
        this.photo_profile1 = photo_profile1;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getPhoto_profile2() {
        return DataApi.BASE_URL + "photo_profile/" + photo_profile2;
    }

    public void setPhoto_profile2(String photo_profile2) {
        this.photo_profile2 = photo_profile2;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
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
        return DataApi.BASE_URL + "img_report_user/" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id_report() {
        return user_id_report;
    }

    public void setUser_id_report(String user_id_report) {
        this.user_id_report = user_id_report;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }
}
