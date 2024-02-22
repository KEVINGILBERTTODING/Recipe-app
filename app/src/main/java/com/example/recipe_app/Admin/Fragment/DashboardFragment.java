package com.example.recipe_app.Admin.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.ui.activities.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.ui.activities.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Interface.InterfaceVerified;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Admin.Model.BugReportModel;
import com.example.recipe_app.Admin.Model.RecipeReportmodel;
import com.example.recipe_app.Admin.Model.UserReportModel;
import com.example.recipe_app.Admin.Model.VerificationModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;

import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    CardView card_user, rl_report_user, rl_report_recipe, rl_report_bug, rl_req_verified;
    TextView tv_total_users, tv_dashboard, tv_username, tv_greeting, tv_total_report_user, tv_total_report_recipe,
                tv_total_bug_report, tv_verified;

    String username, userid;
    ImageView iv_profile, icVerified;
    Calendar calendar;
    ConnectivityManager conMgr;
    SwipeRefreshLayout swipeRefreshLayout;

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
        rl_req_verified = view.findViewById(R.id.rl_verified);
        tv_verified = view.findViewById(R.id.tv_verified);
        icVerified = view.findViewById(R.id.img_verified);
        swipeRefreshLayout = view.findViewById(R.id.swipe_dashboard);


        // SET UCAPAN SALAM
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

        // IV PROFILE CLICK LISTENER
        iv_profile.setOnClickListener(view1 -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, new AdminProfileFragment());
            ft.commit();
        });


        // SWIPE REFRESH LISTENER
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

                // count total request verified
                countReqVerified();

            }
        });





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

        // Menu request verified
        rl_req_verified.setOnClickListener(view1 -> {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_admin, new VerifiedFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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
                    tv_total_users.setText(adminModelList.size() + " Users");
                    swipeRefreshLayout.setRefreshing(false);

                }else {
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
                countUser();

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

                    // show verified badge
                    if (response.body().get(0).getVerified() == 1) {
                        icVerified.setVisibility(View.VISIBLE);
                    } else {
                        icVerified.setVisibility(View.GONE);

                    }

                    swipeRefreshLayout.setRefreshing(false);


                } else {
                    swipeRefreshLayout.setRefreshing(false);

                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);

                getAdminInfo();


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
                    swipeRefreshLayout.setRefreshing(false);

                } else{
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<List<UserReportModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);

                countAllReport();


            }
        });
    }

    // COUNT TOTAL REQUEST VERIFIED
    private void countReqVerified(){
        InterfaceVerified iv = DataApi.getClient().create(InterfaceVerified.class);
        iv.getAllRequest().enqueue(new Callback<List<VerificationModel>>() {
            @Override
            public void onResponse(Call<List<VerificationModel>> call, Response<List<VerificationModel>> response) {
                if (response.body().size() > 0) {
                    swipeRefreshLayout.setRefreshing(false);

                    tv_verified.setText(response.body().size() + " Request Verified");
                } else {
                    swipeRefreshLayout.setRefreshing(false);

                    tv_verified.setText("0 Request Verified");
                }
            }

            @Override
            public void onFailure(Call<List<VerificationModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
                countReqVerified();

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
                    swipeRefreshLayout.setRefreshing(false);

                    tv_total_report_recipe.setText(recipeReportmodelList.size() + " Recipe Report");

                } else{
                    swipeRefreshLayout.setRefreshing(false);

                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecipeReportmodel>> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(true);
                countReportRecipe();

            }
        });
    }

    // method check connection
    private void checkConnection() {
        conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    &&
                    conMgr.getActiveNetworkInfo().isAvailable()
                    &&
                    conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        }
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
                
                    tv_total_bug_report.setText("0 Bug Report");
                }
            }

            @Override
            public void onFailure(Call<List<BugReportModel>> call, Throwable t) {
                countBugReport();

            }
        });
    }

    @Override
    public void onResume() {
        // Change color icon swipe refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);

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

        // count total request verified
        countReqVerified();


        checkConnection();
        super.onResume();
    }
}