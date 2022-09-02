package com.example.recipe_app.Admin.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Admin.Model.BugReportModel;
import com.example.recipe_app.Admin.Model.RecipeReportmodel;
import com.example.recipe_app.Admin.Model.UserReportModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    CardView card_user, rl_report_user, rl_report_recipe, rl_report_bug;
    TextView tv_total_users, tv_dashboard, tv_username, tv_greeting, tv_total_report_user, tv_total_report_recipe,
                tv_total_bug_report;

    String username, userid;
    ImageView iv_profile;
    Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        card_user = view.findViewById(R.id.rl_users);
        tv_total_users = view.findViewById(R.id.tv_total_users);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_username = view.findViewById(R.id.tv_username);
        tv_greeting = view.findViewById(R.id.tv_greeting);
        rl_report_user = view.findViewById(R.id.rl_report_user);
        tv_total_report_user = view.findViewById(R.id.tv_report_user);
        rl_report_recipe = view.findViewById(R.id.rl_report_recipe);
        tv_total_report_recipe = view.findViewById(R.id.tv_report_recipe);
        tv_total_bug_report = view.findViewById(R.id.tv_report_bug);
        rl_report_bug = view.findViewById(R.id.rl_report_bug);


        // set greeting
        calendar = Calendar.getInstance();
        int time_of_day = calendar.get(Calendar.HOUR_OF_DAY);
        if (time_of_day >= 0 && time_of_day < 12) {
            tv_greeting.setText("Good morning 👋");

        } else if (time_of_day >= 12 && time_of_day < 15) {
            tv_greeting.setText("Good afternoon 👋");

        } else if (time_of_day >= 15 && time_of_day < 18) {
            tv_greeting.setText("Good evening 👋");

        } else if (time_of_day >= 18 && time_of_day < 24) {
            tv_greeting.setText("Good night 🌖");

        }

        iv_profile.setOnClickListener(view1 -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, new AdminProfileFragment());
            ft.commit();
        });


        // count users
        countUser();

        // get admin info
        getAdminInfo();

        // count all report user
        countAllReport();

        // count all report recipe
        countReportRecipe();

        // count total bug report
        countBugReport();



        // Card user is clicked
        card_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction  fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_admin, new UserFragment());
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        // menu report user is clicked
        rl_report_user.setOnClickListener(view1 -> {
            FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_admin, new ReportUserFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });

        // menu report recipe
        rl_report_recipe.setOnClickListener(view1 ->{
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, new ReportRecipeFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        rl_report_bug.setOnClickListener(view1 -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, new ReportBugFragment());
            ft.addToBackStack(null);
            ft.commit();

        });





        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAdminInfo();
    }

    // method untuk menghitung total user
    private void countUser() {
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.countUsers().enqueue(new Callback<List<AdminModel>>() {
            @Override
            public void onResponse(Call<List<AdminModel>> call, Response<List<AdminModel>> response) {
                if (response.isSuccessful()) {
                    List<AdminModel> adminModelList = response.body();
                    Log.d("count", "onResponse: " + adminModelList.size());
                    tv_total_users.setText(adminModelList.size() + " Users");

                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {

            }
        });
    }


    // untuk mendapatkan informasi Admin
    private void getAdminInfo() {

        InterfaceAdmin ia = DataApi.getClient().create(InterfaceAdmin.class);
        ia.getAdminInfo(userid).enqueue(new Callback<List<AdminModel>>() {
            @Override
            public void onResponse(Call<List<AdminModel>> call, Response<List<AdminModel>> response) {
                List<AdminModel> adminModelList = response.body();
                if (adminModelList.size() > 0) {
                    tv_username.setText(adminModelList.get(0).getUsername());
                    // load image profile
                    Glide.with(getContext())
                            .load(adminModelList.get(0).getPhoto_profile())
                            .thumbnail(0.5f)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                            .fitCenter()
                            .centerCrop()
                            .override(200, 200)
                            .into(iv_profile);

                } else {
                    Toast.makeText(getContext(), "Failed load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "No connection", Snackbar.LENGTH_LONG).show();

            }
        });
    }


    // count total report recipe
    private void countAllReport(){
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllReport(1).enqueue(new Callback<List<UserReportModel>>() {
            @Override
            public void onResponse(Call<List<UserReportModel>> call, Response<List<UserReportModel>> response) {
                List<UserReportModel> userReportModelList = response.body();
                if (response.isSuccessful()) {
                  
                    tv_total_report_user.setText(userReportModelList.size() + " Account Report");
                } else{
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserReportModel>> call, Throwable t) {


            }
        });
    }

    // count total report recipe
    private void countReportRecipe(){
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllReportRecipe(1).enqueue(new Callback<List<RecipeReportmodel>>() {
            @Override
            public void onResponse(Call<List<RecipeReportmodel>> call, Response<List<RecipeReportmodel>> response) {
                List<RecipeReportmodel> recipeReportmodelList = response.body();
                if (response.isSuccessful()) {
                    tv_total_report_recipe.setText(recipeReportmodelList.size() + " Recipe Report");

                } else{
                    Toast.makeText(getContext(), "Failed get total report recipe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecipeReportmodel>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed get total report recipe", Toast.LENGTH_SHORT).show();


            }
        });
    }

    // count total bug report
    private void countBugReport() {
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllBugReport(1).enqueue(new Callback<List<BugReportModel>>() {
            @Override
            public void onResponse(Call<List<BugReportModel>> call, Response<List<BugReportModel>> response) {
                List<BugReportModel> bugReportModelList = response.body();
                if (bugReportModelList.size() != 0) {
                    tv_total_bug_report.setText(bugReportModelList.size() + " Bug Report");

                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BugReportModel>> call, Throwable t) {

            }
        });
    }
}