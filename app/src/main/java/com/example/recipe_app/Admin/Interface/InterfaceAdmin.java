package com.example.recipe_app.Admin.Interface;

import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Admin.Model.BugReportModel;
import com.example.recipe_app.Admin.Model.RecipeReportmodel;
import com.example.recipe_app.Admin.Model.UserReportModel;
import com.example.recipe_app.Model.AppModel;

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

    // enable user
    @FormUrlEncoded
    @POST("admin/enable_user.php")
    Call<AdminModel> enableUser(
            @Field("user_id") String user_id
    );

    // get all report user

    @GET("admin/get_report_user.php")
    Call<List<UserReportModel>> getAllReport(
            @Query("status") Integer status
    );

    // delete reeport user
    @FormUrlEncoded
    @POST("admin/delete_report.php")
    Call<UserReportModel> deleteUserReport(
            @Field("report_id") String report_id
    );

    // action user report
    @FormUrlEncoded
    @POST("admin/action_report.php")
    Call<UserReportModel> actionReportUser(
            @Field("report_id") String report_id,
            @Field("status") Integer status
    );

    // geta all report recipe
    @GET("admin/report_recipe/get_all_report.php")
    Call<List<RecipeReportmodel>> getAllReportRecipe(
            @Query("status") Integer status
    );


    // delete recipe report
    @FormUrlEncoded
    @POST("admin/report_recipe/delete_report.php")
    Call<RecipeReportmodel> deleteRecipeReport(
            @Field("report_id") String report_id
    );

    // action report recipe

    @FormUrlEncoded
    @POST("admin/report_recipe/action_report.php")
    Call<RecipeReportmodel> actionReportRecipe(
            @Field("report_id") String report_id,
            @Field("status") Integer status
    );

    @FormUrlEncoded
    @POST("admin/report_recipe/action_recipe.php")
    Call<RecipeReportmodel> actionRecipe(
            @Field("recipe_id") String recipe_id,
            @Field("status") String status
    );

    // get all report bug
    @GET("admin/report_bug/get_all_report.php")
    Call<List<BugReportModel>> getAllBugReport(
            @Query("status") Integer status
    );

    // delete bug report
    @FormUrlEncoded
    @POST("admin/report_bug/delete_report.php")
    Call<BugReportModel> deleteBugReport(
            @Field("report_id") String report_id
    );

    // action bug report
    @FormUrlEncoded
    @POST("admin/report_bug/action_report.php")
    Call<BugReportModel> actionBugReport(
            @Field("report_id") String report_id,
            @Field("status") String status
    );

    // get app version
    @GET("view_about.php")
    Call<List<AppModel>> viewAbout();





}
