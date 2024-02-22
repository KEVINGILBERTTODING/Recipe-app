package com.example.recipe_app.data.remote;

import android.media.Ringtone;

import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.Model.ReplyCommentModel;

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
    Call<List<CommentModel>> getComment(
            @Query("recipe_id") String recipe_id
    );

    // method for add comment
    @FormUrlEncoded
    @POST("post_comment.php")
    Call<CommentModel> createComment(
            @Field("comment_id") String commentId,
            @Field("user_id") String user_id,
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


    // Action like comment
    @FormUrlEncoded
    @POST("action_like_comment.php")
    Call<CommentModel> actionLikeComment(
            @Field("comment_id") String commentId,
            @Field("user_id") String user_id,
            @Field("code") Integer code,
            @Field("recipe_id") String recipeId,
            @Field("user_id_notif") String userIdNotif,
            @Field("comment") String comment
    );



    // Count like commment
    @GET("count_like_comment.php")
    Call<List<CommentModel>> countLikeComment(
            @Query("comment_id") String commentId
    );



    // Check apakah user telah like comment atau tidak
    @GET("check_like_comment.php")
    Call<List<CommentModel>> checkLikeComment(
            @Query("comment_id") String commentId,
            @Query("user_id") String userId
    );


    // Get all like comment
    @GET("get_like_comment.php")
    Call<List<CommentModel>> getLikeComment(
            @Query("comment_id") String commentId
    );






}
