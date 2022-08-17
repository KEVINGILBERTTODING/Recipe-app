package com.example.recipe_app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipe_app.Adapter.RecipeShowAllAdapter;
import com.example.recipe_app.MainActivity;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRecipesFragment extends Fragment {

    ShimmerRecyclerView shimmerRecyclerView;
    SearchView searchView;

    private List<RecipeModel> recipeModelList;
    private InterfaceRecipe interfaceRecipe;
    RecipeShowAllAdapter recipeShowAllAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    ImageButton btn_scan;



    public AllRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_all_recipes, container, false);

        shimmerRecyclerView = view.findViewById(R.id.recycler_recipe_all);
        searchView = view.findViewById(R.id.search_all_recipes);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        btn_scan = view.findViewById(R.id.btn_scan);

        // catch data from searchbar

        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("searchText");
            searchView.setQuery(title, false);
        }

        searchView.requestFocus();

        setShimmer();
        getAllRecipe();

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
        getAllRecipe();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setShimmer() {
        shimmerRecyclerView.setAdapter(recipeShowAllAdapter);
        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shimmerRecyclerView.setItemViewType((type, position) -> {
            switch (type) {
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
                            ? R.layout.template_data_show_all
                            : R.layout.template_data_show_all;

                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_data_show_all
                            : R.layout.template_data_show_all;
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
                recipeShowAllAdapter = new RecipeShowAllAdapter(getContext(), recipeModelList);
                // Make it Horizontal recycler view
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                shimmerRecyclerView.setLayoutManager(gridLayoutManager);
                shimmerRecyclerView.setAdapter(recipeShowAllAdapter);
                shimmerRecyclerView.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(getView(), "No conection", Snackbar.LENGTH_SHORT);
                snackbar.show();
                snackbar.setBackgroundTint(getResources().getColor(R.color.main));
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

    private void filter(String newText) {

        ArrayList<RecipeModel> filteredList = new ArrayList<>();

        for (RecipeModel item : recipeModelList) {
            if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);

            }
        }


        recipeShowAllAdapter.filterList(filteredList);


        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
        } else {
            recipeShowAllAdapter.filterList(filteredList);
        }


    }

}