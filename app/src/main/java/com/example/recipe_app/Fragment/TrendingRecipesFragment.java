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

import com.example.recipe_app.Adapter.RecipeShowAllAdapter;
import com.example.recipe_app.Adapter.RecipeTrandingAdapter;
import com.example.recipe_app.Adapter.RecipeTrandingAdapter2;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingRecipesFragment extends Fragment {

    ShimmerRecyclerView shimmerRecyclerView;
    SearchView searchView;

    private List<RecipeModel> recipeModelList;
    private InterfaceRecipe interfaceRecipe;
    RecipeTrandingAdapter2 recipeTrandingAdapter2;
    SwipeRefreshLayout swipeRefreshLayout;



    public TrendingRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_trending_recipes, container, false);

        shimmerRecyclerView = view.findViewById(R.id.recycler_recipe_trendings);
        searchView = view.findViewById(R.id.search_all_recipes);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        setShimmer();
        getRecipeTranding(1, 1);

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
        getRecipeTranding(1, 1);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setShimmer() {
        shimmerRecyclerView.setAdapter(recipeTrandingAdapter2);
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

    private void getRecipeTranding(Integer status, Integer likes) {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getRecipeTranding(status, likes);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipeTrandingAdapter2 = new RecipeTrandingAdapter2(getContext(), recipeModelList);
                // Make it Vertical and have 2 grid in a row

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

                shimmerRecyclerView.setLayoutManager(gridLayoutManager);
                shimmerRecyclerView.setAdapter(recipeTrandingAdapter2);
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

    private void filter(String newText) {

        ArrayList<RecipeModel> filteredList = new ArrayList<>();

        for (RecipeModel item : recipeModelList) {
            if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);

            }
        }


        recipeTrandingAdapter2.filterList(filteredList);


        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
        } else {
            recipeTrandingAdapter2.filterList(filteredList);
        }


    }

}