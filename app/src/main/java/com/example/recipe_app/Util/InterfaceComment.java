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

    // Method for get comment
    @GET("get_comment.php")
    Call<List<CommentModel>> getComment(@Query("recipe_id") String recipe_id);

    // method for add comment
    @FormUrlEncoded
    @POST("post_comment.php")
    Call<CommentModel> createComment(@Field("user_id") String user_id,
                                     @Field("recipe_id") String recipe_id,
                                     @Field("user_id_notif") String user_id_notif,
                                     @Field("comment") String comment);

    // method for edit comment
    @FormUrlEncoded
    @POST("edit_comment.php")
    Call<CommentModel> editComment(@Field("comment_id") String comment_id,
                                   @Field("comment") String comment,
                                   @Field("recipe_id") String recipe_id,
                                   @Field("user_id") String user_id,
                                   @Field("user_id_notif") String user_id_notif,
                                   @Field("date") String date,
                                   @Field("time") String time)
    ;


    // method for delete comment
    @FormUrlEncoded
    @POST("delete_comment.php")
    Call<CommentModel> deleteComment(
            @Field("comment_id") String comment_id,
            @Field("recipe_id") String recipe_id,
            @Field("user_id") String user_id,
            @Field("user_id_notif") String user_id_notif,
            @Field("date") String date,
            @Field("time") String time
    );



}
