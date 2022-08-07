package com.example.recipe_app.Util;

import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceProfile {
    // Get All Recipe
    @GET("get_profile.php")
    Call<List<ProfileModel>> getProfile(
            @Query("user_id") String user_id
    );
}
