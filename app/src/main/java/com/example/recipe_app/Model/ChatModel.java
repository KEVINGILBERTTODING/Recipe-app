package com.example.recipe_app.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatModel implements Serializable {

    @SerializedName("room_id")
    Integer roomId;
    @SerializedName("user_id")
    String userId;
    @SerializedName("user_id1")
    String userId1;
    @SerializedName("user_id2")
    String userId2;
    @SerializedName("chat_image")
    String chatImage;
    @SerializedName("messages")
    String message;
    @SerializedName("success")
    Integer success;
    @SerializedName("date")
    String date;
    @SerializedName("chat_id")
    String chatId;
    @SerializedName("time")
    String time;
    @SerializedName("remove")
    Integer remove;
    @SerializedName("username1")
    String username1;
    @SerializedName("username2")
    String username2;

    public ChatModel(
            Integer roomId, String userId1, String userId2, String chatImage, String message, Integer success,
            String date, String time, String userId, String chatId, Integer remove, String username1, String username2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.chatImage = chatImage;
        this.message = message;
        this.success = success;
        this.date = date;
        this.time = time;
        this.roomId = roomId;
        this.userId = userId;
        this.chatId = chatId;
        this.remove = remove;
        this.username1 = username1;
        this.username2 = username2;
    }

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setStatus(Integer success) {
        this.success = success;
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

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Integer getRemove() {
        return remove;
    }

    public void setRemove(Integer remove) {
        this.remove = remove;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }
}
