package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;
import static com.example.recipe_app.Util.ServerAPI.BASE_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Adapter.RecipeAllAdapter;
import com.example.recipe_app.Adapter.RecipeCategoryPopular;
import com.example.recipe_app.Adapter.RecipeShowAllAdapter;
import com.example.recipe_app.Adapter.RecipeTrandingAdapter;
import com.example.recipe_app.MainActivity;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.ShowAllRecipesActivity;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    String username, userid;
    TextView tv_username, txt_userid;
    ShimmerRecyclerView shimmerRecyclerView, shimmerRecipeCategoryPopular, shimmerRecipeTrending;
    private List<RecipeModel> recipeModelList;
    private InterfaceRecipe interfaceRecipe;
    RecipeAllAdapter recipeAllAdapter;
    RelativeLayout layoutHeader;
    RecipeCategoryPopular recipeCategoryPopular;
    RecipeTrandingAdapter recipeTrandingAdapter;
    TabLayout tabLayout;
    ImageView img_profile;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout lyt_see_all, lyt_see_all_category, lyt_see_all_trending;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        shimmerRecyclerView = view.findViewById(R.id.recycler_recipe_all);
        shimmerRecipeCategoryPopular = view.findViewById(R.id.recycler_recipe_category);
        shimmerRecipeTrending = view.findViewById(R.id.recycler_recipe_trending);
        tv_username = view.findViewById(R.id.tv_username);
        img_profile = view.findViewById(R.id.img_profile);
        tabLayout = view.findViewById(R.id.tab_layout);
        searchView = view.findViewById(R.id.search_barr);
        layoutHeader = view.findViewById(R.id.layout_header);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        lyt_see_all = (LinearLayout) view.findViewById(R.id.layout_see_all);
        lyt_see_all_category = (LinearLayout) view.findViewById(R.id.layout_see_all_catgories);

        // add tab recipe category item
        tabLayout.addTab(tabLayout.newTab().setText("Vegetables"));
        tabLayout.addTab(tabLayout.newTab().setText("Meat"));
        tabLayout.addTab(tabLayout.newTab().setText("Drinks"));
        tabLayout.addTab(tabLayout.newTab().setText("Noodle"));
        tabLayout.addTab(tabLayout.newTab().setText("Others"));

        // [TEST] image profile
        Glide.with(this).load(BASE_URL + "photo_profile/default.png").into(img_profile);

        tv_username.setText("Hi, "+username);

        // show shimmer recyclerview
        setShimmerAllRecipe();
        setShimmerCategoryRecipe();
        setShimmerTrendingRecipe();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
        });

        // Call method get recipe
        getAllRecipe();
        getCategory("Vegetables", 1);
        getRecipeTranding(1,1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getCategory("Vegetables", 1);
                } else if (tab.getPosition() == 1) {
                    getCategory("Meat", 1);
                } else if (tab.getPosition() == 2) {
                    getCategory("Drinks", 1);
                } else if (tab.getPosition() == 3) {
                    getCategory("Noodle", 1);
                } else if (tab.getPosition() == 4) {
                    getCategory("Others", 1);
                } else {
                    getCategory("Meat", 1);
                    Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // button see all recipe
        lyt_see_all.setOnClickListener(View ->{
            Intent intent = new Intent(getContext(), ShowAllRecipesActivity.class);
            intent.putExtra("all", "all");
            startActivity(intent);
        });
        lyt_see_all_category.setOnClickListener(View ->{
            Intent intent = new Intent(getContext(), ShowAllRecipesActivity.class);
            intent.putExtra("kategori", "kategori");
            startActivity(intent);
        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String querry) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return true;
//            }
//        });


        return view;
    }

//    // Method untuk realtime searchview
//
//    private void filter(String newText) {
//
//        ArrayList<RecipeModel> filteredList = new ArrayList<>();
//
//        for (RecipeModel item : recipeModelList) {
//            if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
//                filteredList.add(item);
//
//            }
//        }
//
//
//        RecipeShowAllAdapter.filterList(filteredList);
//
//
//        if (filteredList.isEmpty()) {
//            Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
//        } else {
//            barangAdapter.filterList(filteredList);
//        }
//
//
//    }


    private void refreshItem() {
        getAllRecipe();
        getCategory("Vegetables", 1);
        getRecipeTranding(1,1);
        swipeRefreshLayout.setRefreshing(false);

    }

    private void getRecipeTranding(Integer status, Integer likes) {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getRecipeTranding(status, likes);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipeTrandingAdapter = new RecipeTrandingAdapter(getContext(), recipeModelList);
                // Make it Horizontal recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                shimmerRecipeTrending.setLayoutManager(linearLayoutManager);
                shimmerRecipeTrending.setAdapter(recipeTrandingAdapter);
                shimmerRecipeTrending.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Periksa koneksi anda", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }


        });
    }


    private void getCategory(String category, Integer status) {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getRecipeCategory(category, status);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipeCategoryPopular = new RecipeCategoryPopular(getContext(), recipeModelList);
                // Make it Horizontal recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                shimmerRecipeCategoryPopular.setLayoutManager(linearLayoutManager);
                shimmerRecipeCategoryPopular.setAdapter(recipeCategoryPopular);
                shimmerRecipeCategoryPopular.setHasFixedSize(true);

                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Periksa koneksi anda", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);

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
                recipeAllAdapter = new RecipeAllAdapter(getContext(), recipeModelList);
                // Make it Horizontal recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                shimmerRecyclerView.setLayoutManager(linearLayoutManager);
                shimmerRecyclerView.setAdapter(recipeAllAdapter);
                shimmerRecyclerView.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Periksa koneksi anda", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);

            }


        });
    }

    private void setShimmerAllRecipe(){
        shimmerRecyclerView.setAdapter(recipeAllAdapter);
        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        shimmerRecipeCategoryPopular.setLayoutManager(new LinearLayoutManager(getContext()));
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

    private void setShimmerTrendingRecipe(){
        shimmerRecipeTrending.setAdapter(recipeTrandingAdapter);
        shimmerRecipeTrending.setLayoutManager(new LinearLayoutManager(getContext()));
        shimmerRecipeTrending.setItemViewType((type, position) -> {
            switch (type) {
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
                            ? R.layout.template_list_data_recipe_tranding
                            : R.layout.template_list_data_recipe_tranding;

                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_list_data_recipe_tranding
                            : R.layout.template_list_data_recipe_tranding;
            }
        });
        shimmerRecipeTrending.showShimmer();     // to start showing shimmer

    }
}