package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Adapter.MyRecipeAdapter;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProfileFragment extends Fragment {
    String user_id;
    ImageView iv_profile;
    TextView tv_username, tv_email, tv_biography, tv_date, tv_time;

    List<ProfileModel> profileModelList;
    ProfileModel profileModel;
    InterfaceProfile interfaceProfile;
    RecyclerView rv_recipe;
    MyRecipeAdapter myRecipeAdapter;
    List<RecipeModel> recipeModelList;
    ImageButton btnBack;

    TabLayout tabLayout;


    public ShowProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_profile, container, false);


        iv_profile = view.findViewById(R.id.iv_profile);
        tv_username = view.findViewById(R.id.tv_username);
        tv_email = view.findViewById(R.id.tv_email);
        tv_biography = view.findViewById(R.id.tv_biography);
        tv_date = view.findViewById(R.id.tv_date);
        tv_time = view.findViewById(R.id.tv_time);
        tabLayout = view.findViewById(R.id.tab_layout);
        rv_recipe = view.findViewById(R.id.recycler_recipe);
        btnBack = view.findViewById(R.id.btn_back);

        // mengambil data dari adapter menggunakan bundle
        user_id = getArguments().getString("user_id");

        // Mengambil data profile dari API
        getProfile(user_id);

        // mengambil data recipe dari API
        getRecipe(user_id);


        // create tablayout
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_layout));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_love2));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getRecipe(user_id);
                } else if (tab.getPosition() == 1) {
                    getLikeRecipe(user_id);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getRecipe(user_id);

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });


        return view;
    }


    //get profile
    private void getProfile(String user_id) {
        DataApi.getClient().create(InterfaceProfile.class).getProfile(user_id).enqueue(new retrofit2.Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, retrofit2.Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                for (int i = 0; i < profileModelList.size(); i++) {
                    profileModel = profileModelList.get(i);
                    tv_username.setText(profileModel.getUsername());
                    tv_email.setText(profileModel.getEmail());
                    tv_biography.setText(profileModel.getBiography());
                    tv_date.setText(profileModel.getDate());
                    tv_time.setText(profileModel.getTime());
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

    private void getRecipe(String user_id) {

        DataApi.getClient().create(InterfaceRecipe.class).getMyRecipe(user_id).enqueue(new retrofit2.Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, retrofit2.Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                myRecipeAdapter = new MyRecipeAdapter(getContext(), recipeModelList);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                rv_recipe.setLayoutManager(gridLayoutManager);
                rv_recipe.setAdapter(myRecipeAdapter);
                rv_recipe.setHasFixedSize(true);


            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Snackbar.make(getView(), "Check your connection", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    private void getLikeRecipe(String user_id) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getMyLikeRecipe(user_id);
        call.enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                myRecipeAdapter = new MyRecipeAdapter(getContext(), recipeModelList);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                rv_recipe.setLayoutManager(gridLayoutManager);
                rv_recipe.setAdapter(myRecipeAdapter);
                rv_recipe.setHasFixedSize(true);
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Snackbar.make(getView(), "Check your connection", Snackbar.LENGTH_SHORT).show();
            }


        });
    }
}