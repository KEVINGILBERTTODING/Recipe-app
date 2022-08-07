package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Adapter.RecipeTrandingAdapter;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileFrgament extends Fragment {
    String username, userid;
    ImageView iv_profile;
    TextView tv_username, tv_email, tv_biography;

    List<ProfileModel> profileModelList;
    ProfileModel profileModel;
    InterfaceProfile interfaceProfile;

    public MyProfileFrgament() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        iv_profile = view.findViewById(R.id.iv_profile);
        tv_username = view.findViewById(R.id.tv_username);
        tv_email = view.findViewById(R.id.tv_email);
        tv_biography = view.findViewById(R.id.tv_biography);

        getProfile(userid);


        return view;
    }

//    private void getProfile(String user_id) {
//        interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
//        Call<List<ProfileModel>> call = interfaceProfile.getProfile(user_id);
//        call.enqueue(new Callback<List<ProfileModel>>() {
//
//
//            @Override
//            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
//                profileModelList = response.body();
//                for (int i = 0; i < profileModelList.size(); i++) {
//                    profileModel = profileModelList.get(i);
//                    tv_username.setText(profileModel.getUsername());
//                    tv_email.setText(profileModel.getEmail());
//                    tv_biography.setText(profileModel.getBiography());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
//                Toast.makeText(getContext(), "Periksa koneksi anda", Toast.LENGTH_SHORT).show();
//
//            }
//
//
//        });
//    }



    private void getProfile(String user_id) {
        DataApi.getClient().create(InterfaceProfile.class).getProfile(userid).enqueue(new retrofit2.Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, retrofit2.Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                for (int i = 0; i < profileModelList.size(); i++) {
                    profileModel = profileModelList.get(i);
                    tv_username.setText(profileModel.getUsername());
                    tv_email.setText(profileModel.getEmail());
                    tv_biography.setText(profileModel.getBiography());
                    Glide.with(getContext())
                            .load(profileModel.getPhoto_profile())
                            .thumbnail(0.5f)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                            .fitCenter()
                            .centerCrop()
                            .placeholder(R.drawable.template_img)
                            .override(1024, 768)
                            .into(iv_profile);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Snackbar.make(getView(), "Check your connection", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}