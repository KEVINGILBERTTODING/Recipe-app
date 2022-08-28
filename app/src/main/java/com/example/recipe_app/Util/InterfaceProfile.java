package com.example.recipe_app.Util;

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


}
