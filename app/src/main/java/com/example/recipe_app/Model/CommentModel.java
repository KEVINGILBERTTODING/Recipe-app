package com.example.recipe_app.Model;

import static com.example.recipe_app.data.remote.ServerAPI.BASE_URL;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommentModel implements Serializable {

    @SerializedName("comment_id")
    String comment_id;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("recipe_id")
    String recipe_id;
    @SerializedName("comment")
    String comment;
    @SerializedName("comment_date")
    String comment_date;
    @SerializedName("comment_time")
    String comment_time;
    @SerializedName("photo_profile")
    String photo_profile;
    @SerializedName("username")
    String username;
    @SerializedName("success")
    String success;
    @SerializedName("edited")
    String edited;
    @SerializedName("verified")
    String verified;
    @SerializedName("status")
    Integer status;
    @SerializedName("likes")
    Integer likes;
    @SerializedName("reply_id")
    String replyId;

    public CommentModel(String comment_id, String user_id, String recipe_id, String comment, String comment_date,
                        String comment_time, String photo_profile, String username, String success, String edited,
                        String verified, Integer status, Integer likes, String replyId) {

        this.comment_id = comment_id;
        this.user_id = user_id;
        this.recipe_id = recipe_id;
        this.comment = comment;
        this.photo_profile = photo_profile;
        this.username = username;
        this.comment_time = comment_time;
        this.comment_date = comment_date;
        this.success = success;
        this.edited  = edited;
        this.verified = verified;
        this.status = status;
        this.likes = likes;
        this.replyId = replyId;


    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;

    }

    public String getPhoto_profile() {
        return BASE_URL + "photo_profile/" + photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }
}
