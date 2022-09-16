package com.example.recipe_app.Model;

import static com.example.recipe_app.Util.ServerAPI.BASE_URL;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeModel  implements Serializable {
    @SerializedName("recipe_id")
    String recipe_id;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("username")
    String username;
    @SerializedName("title")
    String title;
    @SerializedName("description")
    String description;
    @SerializedName("category")
    String category;
    @SerializedName("servings")
    String servings;
    @SerializedName("duration")
    String duration;
    @SerializedName("ingredients")
    String ingredients;
    @SerializedName("steps")
    String steps;
    @SerializedName("upload_date")
    String upload_date;
    @SerializedName("upload_time")
    String upload_time;
    @SerializedName("image")
    String image;
    @SerializedName("status")
    String status;
    @SerializedName("ratings")
    String ratings;
    @SerializedName("likes")
    String likes;
    @SerializedName("photo_profile")
    String photo_profile;
    @SerializedName("note")
    String note;
    @SerializedName("email")
    String email;
    @SerializedName("code")
    String success;
    @SerializedName("message")
    String message;
    @SerializedName("user_id_notif")
    String user_id_notif;

    public RecipeModel(String recipe_id, String user_id, String username, String title, String description, String category, String servings, String duration, String ingredients,
                       String steps, String upload_date, String upload_time, String image, String status,
                       String ratings, String likes, String photo_profile, String note, String email, String success, String message, String user_id_notif) {
        this.recipe_id = recipe_id;
        this.user_id = user_id;
        this.username = username;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.servings = servings;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.status = status;
        this.upload_date = upload_date;
        this.upload_time = upload_time;
        this.image = image;
        this.likes = likes;
        this.ratings = ratings;
        this.photo_profile = photo_profile;
        this.note = note;
        this.email = email;
        this.success = success;
        this.message = message;
        this.user_id_notif = user_id_notif;



    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getImage() {
        return BASE_URL + "recipe_images/" + image;
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

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getPhoto_profile() {
        return BASE_URL + "photo_profile/" + photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id_notif() {
        return user_id_notif;
    }

    public void setUser_id_notif(String user_id_notif) {
        this.user_id_notif = user_id_notif;
    }
}
