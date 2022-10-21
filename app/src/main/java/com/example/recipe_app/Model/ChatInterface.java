package com.example.recipe_app.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatInterface {

    // get list chat
    @GET("get_list_chat.php")
    Call<List<ChatModel>> getRoomChat(
            @Query("user_id") String userId,
            @Query("archieve") Integer archieve
    );


    // get message
    @GET("get_message.php")
    Call<List<ChatModel>> getMessage(
            @Query("room_id") Integer roomId
    );

    // Post Message
    @FormUrlEncoded
    @POST("post_message.php")
    Call<ChatModel> postMessage (
            @Field("room_id") Integer roomId,
            @Field("user_id") String userId,
            @Field("message") String message
    );

    // Delete message
    @FormUrlEncoded
    @POST("delete_message.php")
    Call<ChatModel> deleteMessage (
            @Field("chat_id") String chatId,
            @Field("code") Integer code
    );

    // Get new message
    @GET("new_message.php")
    Call<List<ChatModel>> getNewMessage(
            @Query("user_id1") String userId1,
            @Query("user_id2") String userId2
    );

    @FormUrlEncoded
    @POST("action_read_message.php")
    Call<ChatModel> actionReadMessage(
            @Field("room_id") Integer roomId,
            @Field("user_id") String userId

    );

    // get new message
    @GET("get_new_message.php")
    Call<List<ChatModel>> getNewMessage(
            @Query("room_id") Integer rooomId,
            @Query("user_id") String userId
    );

    // archieved room chat
    @FormUrlEncoded
    @POST("action_archieve_room.php")
    Call<ChatModel> archievedRoom(
            @Field("room_id") Integer roomId,
            @Field("code") Integer code
    );


}
