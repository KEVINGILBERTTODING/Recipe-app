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
}
