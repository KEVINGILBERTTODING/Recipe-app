package com.example.recipe_app.Util;

import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.ReplyCommentModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceReplyComment {

    // Get all comment reply
    @GET("get_comment_reply.php")
    Call<List<ReplyCommentModel>> getReplyComment(
            @Query("comment_id") String commentId

    );


    // Post reply comment
    @FormUrlEncoded
    @POST("post_reply_comment.php")
    Call<ReplyCommentModel> postCommentReply(
            @Field("reply_id") String reply_id,
            @Field("user_id") String user_id,
            @Field("comment_id") String commentId,
            @Field("recipe_id") String recipe_id,
            @Field("user_id_notif") String user_id_notif,
            @Field("comment") String comment
    );

    // Delete comment reply
    @FormUrlEncoded
    @POST("delete_comment_reply.php")
    Call<ReplyCommentModel> deleteCommentReply(
            @Field("reply_id") String replyId
    );

    // Action like comment
    @FormUrlEncoded
    @POST("action_like_reply_comment.php")
    Call<ReplyCommentModel> actionLikeComment(
            @Field("comment_id") String commentId,
            @Field("reply_id") String replyId,
            @Field("user_id") String user_id,
            @Field("code") Integer code,
            @Field("recipe_id") String recipeId,
            @Field("user_id_notif") String userIdNotif,
            @Field("comment") String comment
    );



    // Count like commment
    @GET("count_like_comment_reply.php")
    Call<List<ReplyCommentModel>> countLikeCommentReply(
            @Query("reply_id") String replyId
    );



    // Check apakah user telah like comment atau tidak
    @GET("check_like_reply_comment.php")
    Call<List<ReplyCommentModel>> checkLikeComment(
            @Query("reply_id") String replyId,
            @Query("user_id") String userId
    );

    // Get all like comment
    @GET("check_like_comment_reply.php")
    Call<List<ReplyCommentModel>> getAllLike(
            @Query("reply_id") String replyId
    );

}
