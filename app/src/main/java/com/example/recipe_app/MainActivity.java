package com.example.recipe_app;

import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;
import static com.example.recipe_app.Util.ServerAPI.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Adapter.RecipeCategoryPopular;
import com.example.recipe_app.Adapter.RecipePublicAdapter;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String username, userid;
    TextView  tv_username, txt_userid;
    ShimmerRecyclerView shimmerRecyclerView, shimmerRecipeCategoryPopular;
    private List<RecipeModel> recipeModelList;
    private InterfaceRecipe interfaceRecipe;
    RecipePublicAdapter recipePublicAdapter;
    RecipeCategoryPopular recipeCategoryPopular;
    TabLayout tabLayout;
    ImageView img_profile;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         shimmerRecyclerView = findViewById(R.id.recycler_recipe_all);
         shimmerRecipeCategoryPopular = findViewById(R.id.recycler_recipe_category);
         tv_username = findViewById(R.id.tv_username);
         img_profile = findViewById(R.id.img_profile);
         tabLayout = findViewById(R.id.tab_layout);
         searchView = findViewById(R.id.search_barr);

         tabLayout.addTab(tabLayout.newTab().setText("Noodle"));
            tabLayout.addTab(tabLayout.newTab().setText("Popular"));
            tabLayout.addTab(tabLayout.newTab().setText("Category"));
            tabLayout.addTab(tabLayout.newTab().setText("Favorite"));
            tabLayout.addTab(tabLayout.newTab().setText("My Recipe"));
        tabLayout.addTab(tabLayout.newTab().setText("resep rahasia"));


        // Disable searchview request focus

        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchView.onActionViewCollapsed();
        }

//         tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//             @Override
//             public void onTabSelected(TabLayout.Tab tab) {
//                 if (tab.getPosition() == 0) {
//                     shimmerRecyclerView.showShimmerAdapter();
//                     shimmerRecipeCategoryPopular.hideShimmerAdapter();
//                 } else {
//                     shimmerRecipeCategoryPopular.showShimmerAdapter();
//                     shimmerRecyclerView.hideShimmerAdapter();
//                 }
//             }

//             @Override
//             public void onTabUnselected(TabLayout.Tab tab) {
//
//             }
//
//             @Override
//             public void onTabReselected(TabLayout.Tab tab) {
//
//             }
//         });

        Glide.with(MainActivity.this).load(BASE_URL + "photo_profile/default.png").into(img_profile);


         // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        tv_username.setText("Hi, "+username);


        // show shimmer recyclerview
        setShimmerAllRecipe();
        setShimmerCategoryRecipe();

        // Call method get recipe
        getAllRecipe();
        getCategory();



    }

    private void getCategory() {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getAllRecipe(1);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipeCategoryPopular = new RecipeCategoryPopular(MainActivity.this, recipeModelList);
                // Make it Horizontal recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

                shimmerRecipeCategoryPopular.setLayoutManager(linearLayoutManager);
                shimmerRecipeCategoryPopular.setAdapter(recipeCategoryPopular);
                shimmerRecipeCategoryPopular.setHasFixedSize(true);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Periksa koneksi anda", Toast.LENGTH_SHORT).show();

            }


        });
    }

    private void getAllRecipe() {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getAllRecipe(1);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipePublicAdapter = new RecipePublicAdapter(MainActivity.this, recipeModelList);
                // Make it Horizontal recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

                shimmerRecyclerView.setLayoutManager(linearLayoutManager);
                shimmerRecyclerView.setAdapter(recipePublicAdapter);
                shimmerRecyclerView.setHasFixedSize(true);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Periksa koneksi anda", Toast.LENGTH_SHORT).show();

            }


        });
    }

    private void setShimmerAllRecipe(){
        shimmerRecyclerView.setAdapter(recipePublicAdapter);
        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shimmerRecyclerView.setItemViewType((type, position) -> {
            switch (type) {
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
                            ? R.layout.template_list_data_recipe_1
                            : R.layout.template_list_data_recipe_1;

                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_list_data_recipe_1
                            : R.layout.template_list_data_recipe_1;
            }
        });
        shimmerRecyclerView.showShimmer();     // to start showing shimmer

    }

    private void setShimmerCategoryRecipe(){
        shimmerRecipeCategoryPopular.setAdapter(recipeCategoryPopular);
        shimmerRecipeCategoryPopular.setLayoutManager(new LinearLayoutManager(this));
        shimmerRecipeCategoryPopular.setItemViewType((type, position) -> {
            switch (type) {
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
                            ? R.layout.template_lits_data_recipe_category
                            : R.layout.template_lits_data_recipe_category;

                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_lits_data_recipe_category
                            : R.layout.template_lits_data_recipe_category;
            }
        });
        shimmerRecipeCategoryPopular.showShimmer();     // to start showing shimmer

    }
}