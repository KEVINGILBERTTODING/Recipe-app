package com.example.recipe_app.Admin.Model;

import com.example.recipe_app.Util.DataApi;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerificationModel  implements Serializable {

    @SerializedName("user_id")
    Integer user_id;
    @SerializedName("id")
    Integer id;
    @SerializedName("username")
    String username;
    @SerializedName("full_name")
    String fullname;
    @SerializedName("photo_profile")
    String photo_profile;
    @SerializedName("doc_type")
    String docType;
    @SerializedName("category")
    String category;
    @SerializedName("region")
    String region;
    @SerializedName("type")
    String type;
    @SerializedName("url")
    String url;
    @SerializedName("image")
    String image;
    @SerializedName("email")
    String email;
    @SerializedName("date")
    String date;
    @SerializedName("time")
    String time;
    @SerializedName("status")
    Integer status;
    @SerializedName("message")
    String message;
    @SerializedName("rejected")
    String rejected;
    @SerializedName("verified")
    Integer verified;

    public VerificationModel(Integer user_id, Integer id, String username, String fullname, String photo_profile, String docType, String category, String region, String type, String url,
                             String image, String date, String time, Integer status, String message, String email, String  rejected, Integer verified) {
        this.user_id = user_id;
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.photo_profile = photo_profile;
        this.docType = docType;
        this.category = category;
        this.region = region;
        this.type = type;
        this.url = url;
        this.image = image;
        this.date = date;
        this.time = time;
        this.status = status;
        this.message = message;
        this.email = email;
        this.rejected = rejected;
        this.verified = verified;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoto_profile() {
        return DataApi.BASE_URL + "photo_profile/" + photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return DataApi.BASE_URL + "document/" + image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public Integer getVerified() {
        return verified;
    }
}
