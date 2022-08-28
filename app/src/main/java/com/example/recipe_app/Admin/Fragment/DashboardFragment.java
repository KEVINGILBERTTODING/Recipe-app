package com.example.recipe_app.Admin.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.google.android.material.snackbar.Snackbar;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    CardView card_user;
    TextView tv_total_users, tv_dashboard, tv_username, tv_greeting;

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
        tv_dashboard = view.findViewById(R.id.tv_dashboard);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_username = view.findViewById(R.id.tv_username);
        tv_greeting = view.findViewById(R.id.tv_greeting);


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
            tv_greeting.setText("Good night 👋");

        }


        // count users
        countUser();

        // get admin info
        getAdminInfo();


        tv_dashboard.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Dashboard", Toast.LENGTH_SHORT).show();
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





        return view;
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
                Toast.makeText(getContext(), "no connection", Toast.LENGTH_SHORT).show();

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
                Snackbar.make(getView(), "No connection", Snackbar.LENGTH_SHORT).show();

            }
        });
    }
}