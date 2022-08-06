package com.example.recipe_app.Util;

import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceComment {
    @GET("get_comment.php")
    Call<List<CommentModel>> getComment(@Query("recipe_id") String recipe_id);
    @FormUrlEncoded
    @POST("post_comment.php")
    Call<CommentModel> createComment(@Field("user_id") String user_id,
                                     @Field("recipe_id") String recipe_id,
                                     @Field("comment") String comment);


    @FormUrlEncoded
    @POST("delete_comment.php")
    Call<CommentModel> deleteComment(@Field("comment_id") String comment_id);



}
