package com.example.recipe_app.Model;

import com.example.recipe_app.Util.DataApi;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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
    @SerializedName("photo_profile")
    String photoProfile;
    @SerializedName("username")
    String userName;
    @SerializedName("verified")
    String verified;
    @SerializedName("photo_profile1")
    String photoProfile1;
    @SerializedName("photo_profile2")
    String getPhotoProfile2;
    List<ChatModel> chatModelList;
    String chatModel;
    @SerializedName("verified1")
    String verified1;
    @SerializedName("verified2")
    String verified2;
    @SerializedName("status")
    String status;
    @SerializedName("archieve_1")
    Integer archieved1;
    @SerializedName("archieve_2")
    Integer Archieved2;



    public ChatModel(
            Integer roomId, String userId1, String userId2, String chatImage, String message, Integer success,
            String date, String time, String userId, String chatId, Integer remove, String username1, String username2,
            String photoProfile, String userName, String verified, String photoProfile1, String getPhotoProfile2, String chatModel,
            String verified1, String verified2, String status, Integer archieved1, Integer archieved2) {
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
        this.userName = userName;
        this.photoProfile = photoProfile;
        this.verified = verified;
        this.photoProfile1 = photoProfile1;
        this.getPhotoProfile2 = getPhotoProfile2;
        this.chatModel = chatModel;
        this.verified1 = verified1;
        this.verified2 = verified2;
        this.status = status;
        archieved1 = archieved1;
        archieved2 = archieved2;
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


    public String getPhotoProfile() {
        return DataApi.BASE_URL +"photo_profile/" + photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getPhotoProfile1() {
        return DataApi.BASE_URL +"photo_profile/" + photoProfile1;
    }

    public void setPhotoProfile1(String photoProfile1) {
        this.photoProfile1 = photoProfile1;
    }

    public String getGetPhotoProfile2() {
        return DataApi.BASE_URL +"photo_profile/" + getPhotoProfile2;
    }

    public void setGetPhotoProfile2(String getPhotoProfile2) {
        this.getPhotoProfile2 = getPhotoProfile2;


    }

    public List<ChatModel> getChatModelList() {
        return chatModelList;
    }

    public void setChatModelList(List<ChatModel> chatModelList) {
        this.chatModelList = chatModelList;
    }

    public String getChatModel() {
        return chatModel;
    }

    public void setChatModel(String chatModel) {
        this.chatModel = chatModel;
    }

    public String getVerified1() {
        return verified1;
    }

    public void setVerified1(String verified1) {
        this.verified1 = verified1;
    }

    public String getVerified2() {
        return verified2;
    }

    public void setVerified2(String verified2) {
        this.verified2 = verified2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getArchieved1() {
        return archieved1;
    }

    public void setArchieved1(Integer archieved1) {
        this.archieved1 = archieved1;
    }

    public Integer getArchieved2() {
        return Archieved2;
    }

    public void setArchieved2(Integer archieved2) {
        Archieved2 = archieved2;
    }
}
