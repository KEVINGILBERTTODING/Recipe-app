package com.example.recipe_app.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatInterface {

    @GET("get_list_chat.php")
    Call<List<ChatModel>> getRoomChat(
            @Query("user_id") String userId
    );
}
