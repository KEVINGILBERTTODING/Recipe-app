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

    // Get All Recipe
    @GET("view_recipe.php")
    Call<List<RecipeModel>> getAllRecipe(
            @Query("status") Integer status
    );

    // Get Recipe By Category
    @GET("view_recipe.php")
    Call<List<RecipeModel>> getRecipeCategory(
            @Query("category") String category,
            @Query("status") Integer status
    );

    // Get Recipe By Likes
    @GET("view_recipe.php")
    Call<List<RecipeModel>> getRecipeTranding(
            @Query("status") Integer status,
            @Query("likes") Integer likes
    );

    // Get Recipe By Likes
    @GET("get_my_recipe.php")
    Call<List<RecipeModel>> getMyRecipe(
            @Query("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("post_recipe.php")
    Call<RecipeModel> createRecipe(@Field("user_id") String user_id,
                                 @Field("title") String title,
                                 @Field("description") String description,
                                 @Field("category") String category,
                                 @Field("servings") String servings,
                                 @Field("duration") String duration,
                                 @Field("ingredients") String ingredients,
                                 @Field("steps") String steps,
                                 @Field("image") String image,
                                 @Field("status") String status,
                                 @Field("note") String notes);
    @DELETE("recipe/")
    Call<RecipeModel> deleteRecipe(@Query("recipe_id") String recipe_id);
}
