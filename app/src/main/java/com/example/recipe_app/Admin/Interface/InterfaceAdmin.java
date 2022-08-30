package com.example.recipe_app.Admin.Interface;

import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Admin.Model.UserReportModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceAdmin {

    // count total user
    @GET("admin/count_user.php")
    Call<List<AdminModel>> countUsers();

    // get all user
    @GET("admin/get_all_user.php")
    Call<List<AdminModel>> getAllUser(
            @Query("active") Integer active
    );

    // load use info in dashboard fragment
    @GET("get_profile.php")
    Call<List<AdminModel>> getAdminInfo (
            @Query("user_id") String user_id
    );

    // disable user
    @FormUrlEncoded
    @POST("admin/disable_user.php")
    Call<AdminModel> disableUser(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("admin/enable_user.php")
    Call<AdminModel> enableUser(
            @Field("user_id") String user_id
    );

    @GET("admin/get_report_user.php")
    Call<List<UserReportModel>> getAllReport(
            @Query("status") Integer status
    );




}
