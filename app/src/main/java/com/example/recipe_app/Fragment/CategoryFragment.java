package com.example.recipe_app.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recipe_app.Adapter.RecipeCategoryPopular;
import com.example.recipe_app.Adapter.RecipeShowAllAdapter;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {

    ShimmerRecyclerView shimmerRecyclerView;
    SearchView searchView;

    private List<RecipeModel> recipeModelList;
    private InterfaceRecipe interfaceRecipe;
    RecipeCategoryPopular recipeCategoryPopular;
    TabLayout tabLayout;
    SwipeRefreshLayout swipeRefreshLayout;



    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_category, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        shimmerRecyclerView = view.findViewById(R.id.recycler_recipe_category);
        searchView = view.findViewById(R.id.search_recipes_category);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        // add tab recipe category item
        tabLayout.addTab(tabLayout.newTab().setText("Vegetables"));
        tabLayout.addTab(tabLayout.newTab().setText("Meat"));
        tabLayout.addTab(tabLayout.newTab().setText("Drinks"));
        tabLayout.addTab(tabLayout.newTab().setText("Noodle"));
        tabLayout.addTab(tabLayout.newTab().setText("Others"));


        setShimmerCategoryRecipe();

        getCategory("Vegetables", 1);

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

        // when refresh swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
        });



        return view;
    }

    private void refreshItem() {
        swipeRefreshLayout.setRefreshing(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getCategory("Vegetables", 1);
                    swipeRefreshLayout.setRefreshing(false);
                } else if (tab.getPosition() == 1) {
                    getCategory("Meat", 1);
                    swipeRefreshLayout.setRefreshing(false);
                } else if (tab.getPosition() == 2) {
                    getCategory("Drinks", 1);
                    swipeRefreshLayout.setRefreshing(false);
                } else if (tab.getPosition() == 3) {
                    getCategory("Noodle", 1);
                } else if (tab.getPosition() == 4) {
                    getCategory("Others", 1);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    getCategory("Meat", 1);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
                }

                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
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
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

                shimmerRecyclerView.setLayoutManager(gridLayoutManager);
                shimmerRecyclerView.setAdapter(recipeCategoryPopular);
                shimmerRecyclerView.setHasFixedSize(true);

                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Periksa koneksi anda", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);

            }


        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String querry) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void setShimmerCategoryRecipe(){
        shimmerRecyclerView.setAdapter(recipeCategoryPopular);
        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shimmerRecyclerView.setItemViewType((type, position) -> {
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
        shimmerRecyclerView.showShimmer();     // to start showing shimmer

    }

    private void filter(String newText) {

        ArrayList<RecipeModel> filteredList = new ArrayList<>();

        for (RecipeModel item : recipeModelList) {
            if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);

            }
        }


        recipeCategoryPopular.filterList(filteredList);


        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
        } else {
            recipeCategoryPopular.filterList(filteredList);
        }


    }



}