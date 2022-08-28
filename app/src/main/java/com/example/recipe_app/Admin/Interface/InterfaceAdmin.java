package com.example.recipe_app.Admin.Interface;

import com.example.recipe_app.Admin.Model.AdminModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceAdmin {

    @GET("admin/count_user.php")
    Call<List<AdminModel>> countUsers();


}
