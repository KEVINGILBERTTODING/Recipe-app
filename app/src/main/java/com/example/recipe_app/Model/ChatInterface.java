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
            @Query("user_id") String userId
    );
@GET("get_list_chat.php")
    Call<ChatModel> getRoomChat2(
            @Query("user_id") String userId
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
}
