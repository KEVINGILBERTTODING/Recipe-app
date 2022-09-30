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
}
