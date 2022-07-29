package com.example.recipe_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Adapter.RecipeShowAllAdapter;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.textfield.TextInputEditText;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    ShimmerRecyclerView shimmerRecyclerView;
    SearchView searchView;
    private List<RecipeModel> recipeModelList;
    private InterfaceRecipe interfaceRecipe;
    RecipeShowAllAdapter recipeShowAllAdapter;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String searchText = getIntent().getStringExtra("TITLE");

        shimmerRecyclerView = findViewById(R.id.recycler_recipe_all);
        searchView = findViewById(R.id.search_all_recipes);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        TextView tv_test = findViewById(R.id.test);

        tv_test.setText(searchText);

        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        setShimmer();
        getAllRecipe();
        searchView.setQuery(title, false);
        searchView.requestFocus();



        // when refresh swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {



                if (searchText.length() == 0) {
                    getAllRecipe();
                }

                filter(searchText);


                return true;
            }
        });
    }


    private void filter(String searchText) {

        ArrayList<RecipeModel> filteredList = new ArrayList<>();

        for (RecipeModel item : recipeModelList) {
            if (item.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);

            }
        }


        recipeShowAllAdapter.filterList(filteredList);




    }



    private void refreshItem() {
        getAllRecipe();
        swipeRefreshLayout.setRefreshing(false);
    }


    private void setShimmer() {
        shimmerRecyclerView.setAdapter(recipeShowAllAdapter);
        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
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

    private void getAllRecipe() {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getAllRecipe(1);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipeShowAllAdapter = new RecipeShowAllAdapter(SearchActivity.this, recipeModelList);
                // Make it Horizontal recycler view
                GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
                shimmerRecyclerView.setLayoutManager(gridLayoutManager);
                shimmerRecyclerView.setAdapter(recipeShowAllAdapter);
                shimmerRecyclerView.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Periksa koneksi anda", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);

            }


        });

    }
}