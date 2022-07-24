package com.example.recipe_app.Util;

import com.example.recipe_app.Model.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceRecipe {
    @GET("view_recipe.php")
    Call<List<RecipeModel>> getRecipe(
            @Query("status") Integer status
    );
    @FormUrlEncoded
    @POST("recipe/")
    Call<RecipeModel> postRecipe(@Field("user_id") String user_id,
                                 @Field("username") String username,
                                 @Field("title") String title,
                                 @Field("description") String description,
                                 @Field("category") String category,
                                 @Field("servings") String servings,
                                 @Field("duration") String duration,
                                 @Field("ingredients") String ingredients,
                                 @Field("steps") String steps,
                                 @Field("upload_date") String upload_date,
                                 @Field("upload_time") String upload_time,
                                 @Field("image") String image,
                                 @Field("status") String status,
                                 @Field("ratings") String ratings,
                                 @Field("likes") String likes);
    @DELETE("recipe/")
    Call<RecipeModel> deleteRecipe(@Query("recipe_id") String recipe_id);
}
