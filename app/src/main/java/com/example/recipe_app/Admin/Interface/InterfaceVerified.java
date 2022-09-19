package com.example.recipe_app.Admin.Interface;

import com.example.recipe_app.Admin.Model.VerificationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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


}
