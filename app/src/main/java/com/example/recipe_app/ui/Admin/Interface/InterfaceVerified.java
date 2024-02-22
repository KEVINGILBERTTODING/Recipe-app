package com.example.recipe_app.ui.Admin.Interface;

import com.example.recipe_app.ui.Admin.Model.VerificationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InterfaceVerified {


    // Get all request
    @GET("admin/verified/get_all_request.php")
    Call<List<VerificationModel>> getAllRequest(

    );

    // GET ALL REQUEST REJECTED
    @GET("admin/verified/get_all_rejected.php")
    Call<List<VerificationModel>> getReqRejected();

    // DELETE REQUEST
    @FormUrlEncoded
    @POST("admin/verified/delete_request.php")
    Call<VerificationModel> deleteRequestVerified(
            @Field("id") Integer id
    );



    // ACCEPT REQUEST
    @FormUrlEncoded
    @POST("admin/verified/accept_verification.php")
    Call<VerificationModel> acceptRequest(
            @Field("user_id") Integer user_id
    );


    // REJECT REQUEST
    @FormUrlEncoded
    @POST("admin/verified/reject_verification.php")
    Call<VerificationModel> rejectRequest(
            @Field("id") Integer id,
            @Field("user_id") Integer user_id
    );

    // GET ACCEPT REQUEST
    @GET("admin/verified/get_all_accept.php")
    Call<List<VerificationModel>> getAllAccept(
    );











}
