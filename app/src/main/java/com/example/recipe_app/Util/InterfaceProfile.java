package com.example.recipe_app.Util;

import android.media.MediaCrypto;

import com.airbnb.lottie.L;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceProfile {
 // Get All Recipe
 @GET("get_profile.php")
 Call<List<ProfileModel>> getProfile(
         @Query("user_id") String user_id
 );

 // Check old pass

 @FormUrlEncoded
 @POST("check_password.php")
 Call<ProfileModel>checkOldPassword(
         @Field("user_id") String user_id,
         @Field("password") String password
 );

 // create new password
 @FormUrlEncoded
 @POST("update_password.php")
 Call<ProfileModel>updatePassword(
         @Field("user_id") String user_id,
         @Field("password") String password
 );

 @FormUrlEncoded
 @POST("update_email.php")
 Call<ProfileModel>updateEmail(
         @Field("user_id") String user_id,
         @Field("email") String email
 );

 // method untuk mengubah photo profile
 @FormUrlEncoded
 @POST("update_image_profile.php")
 Call<ProfileModel>updateImageProfile(
         @Field("user_id") String user_id,
         @Field("image") String image
 );

 // Method untuk send report bug
 @FormUrlEncoded
 @POST("report_bug.php")
 Call<ProfileModel>reportBug(
         @Field("user_id") String user_id,
         @Field("chat") String chat,
         @Field("title") String title,
         @Field("image") String image
 );

 @FormUrlEncoded
 @POST("update_bio.php")
 Call<ProfileModel> updateBio(
         @Field("user_id") String user_id,
         @Field("bio") String bio
 );

 @FormUrlEncoded
 @POST("check_login_admin.php")
 Call<ProfileModel> checkLoginAdmin(
         @Field("username") String username,
         @Field("password") String password
 );

 @FormUrlEncoded
 @POST("report_user.php")
 Call<RecipeModel> reportUser(
         @Field("user_id") String user_id,
         @Field("user_id_report") String user_id_report,
         @Field("report") String report,
         @Field("image") String image,
         @Field("title") String title

 );

 // update username
 @FormUrlEncoded
 @POST("update_username.php")
 Call<ProfileModel> updateUsername(
         @Field("user_id") String user_id,
         @Field("username") String username
 );

 // get all followers
 @GET("get_all_followers.php")
 Call<List<ProfileModel>> getAllFollowers(
         @Query("user_id") String user_id
 );

 @GET("get_all_following.php")
 Call<List<ProfileModel>> getAllFollowing(
         @Query("user_id") String user_id
 );

 // check following
 @GET("check_following.php")
 Call<List<ProfileModel>> checkFollowing(
         @Query("user_id") String user_id,
         @Query("following_id") String following_id
 );

 // check followers
 @GET("check_followers.php")
 Call<List<ProfileModel>> checkFollowers(
         @Query("user_id") String user_id,
         @Query("followers_id") String followers_id

 );

 @FormUrlEncoded
 @POST("action_following.php")
 Call<ProfileModel> unfollAccount(
         @Field("user_id") String userid,
         @Field("following_id") String following_id
 );

 @FormUrlEncoded
 @POST("action_follow.php")
 Call<ProfileModel> followAccount(
         @Field("user_id") String user_id,
         @Field("following_id") String following_id
 );

 // REMOVE FOLLOWERS
 @FormUrlEncoded
 @POST("remove_followers.php")
 Call<ProfileModel> removeFollowers(
         @Field("id") String id,
         @Field("user_id") String user_id
 );

 // REMOVE FOLLOWING
 @FormUrlEncoded
 @POST("remove_following.php")
 Call<ProfileModel> removeFollowing(
         @Field("id") String id,
         @Field("user_id") String user_id
 );


}
