package com.example.recipe_app.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatModel implements Serializable {

    @SerializedName("user_id1")
    String userId1;
    @SerializedName("user_id2")
    String userId2;
    @SerializedName("chat_image")
    String chatImage;
    @SerializedName("chat")
    String chat;

    public ChatModel(String userId1, String userId2, String chatImage, String chat) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.chatImage = chatImage;
        this.chat = chat;
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

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
