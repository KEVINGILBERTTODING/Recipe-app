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


    // get if recipe is saved

    @GET("get_saved_recipe.php")
    Call<List<RecipeModel>> getSavedRecipe(
            @Query("user_id") String user_id
    );

    // get if recipe is like
    @GET("get_liked_recipe.php")
    Call<List<RecipeModel>> getLikeRecipe(
            @Query("user_id") String user_id
    );

    // Get Recipe By Likes
    @GET("view_recipe.php")
    Call<List<RecipeModel>> getRecipeTranding(
            @Query("status") Integer status,
            @Query("likes") Integer likes
    );

    // Get my SAVE recipe
    @GET("get_my_recipe.php")
    Call<List<RecipeModel>> getMyRecipe(
            @Query("user_id") String user_id
    );

    // Get my LIKE recipe
    @GET("get_liked_recipe.php")
    Call<List<RecipeModel>> getMyLikeRecipe(
            @Query("user_id") String user_id
    );

    // delete recipe from saved recipe
    @FormUrlEncoded
    @POST("delete_saved_recipe.php")
    Call<RecipeModel> deleteSavedRecipe(
            @Field("recipe_id") String recipe_id,
            @Field("user_id") String user_id
    );


    // delete recipe from liked recipe
    @FormUrlEncoded
    @POST("delete_like_recipe.php")
    Call<RecipeModel> deleteLikedRecipe(
            @Field("recipe_id") String recipe_id,
            @Field("user_id") String user_id
    );



    // save saved recipe
    @FormUrlEncoded
    @POST("save_saved_recipe.php")
    Call<RecipeModel> saveSavedRecipe(
            @Field("recipe_id") String recipe_id,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("save_like_recipe.php")
    Call<RecipeModel> saveLikeRecipe(
            @Field("recipe_id") String recipe_id,
            @Field("user_id") String user_id
    );


    // post recipe
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
   @FormUrlEncoded
    @POST("count_like_recipe.php")
    Call<RecipeModel> countLikeRecipe(@Field("recipe_id") String recipe_id,
                                      @Field("code") Integer code);
}
