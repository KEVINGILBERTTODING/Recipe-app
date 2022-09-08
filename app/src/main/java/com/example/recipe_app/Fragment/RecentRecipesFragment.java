package com.example.recipe_app.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipe_app.Adapter.RecipeShowAllAdapter;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentRecipesFragment extends Fragment {

    ShimmerRecyclerView rv_recipe;
    List<RecipeModel> recipeModelList;
    RecipeShowAllAdapter recipeShowAllAdapter;
    ImageButton btn_back;
    SearchView searchView;
    GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  root = inflater.inflate(R.layout.fragment_recent_recipes, container, false);

        rv_recipe = root.findViewById(R.id.recycler_recipe_all);
        btn_back = root.findViewById(R.id.btn_back);
        searchView = root.findViewById(R.id.search_all_recipes);

        // show shimmer
        setShimmer();

        // get data from api
        getAllRecipe();


        // searchview
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rv_recipe.setItemViewType((type, position) -> {
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
                rv_recipe.showShimmer();     // to start showing shimmer
                final Handler handler = new Handler();
                handler.postDelayed((Runnable) () -> {
                    rv_recipe.hideShimmer(); // to hide shimmer
                }, 1000);
                filter(newText);
                return true;
            }
        });


        // btn back
        btn_back.setOnClickListener(view -> {

                FragmentManager fm =  getFragmentManager();
                fm.popBackStack();
        });



        return root;
    }

    private void filter(String newText) {
        ArrayList<RecipeModel> filteredList = new ArrayList<>();

        for (RecipeModel item : recipeModelList) {
            if (item.getTitle().toLowerCase().contains(newText.toLowerCase())){
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

    // Get all recipe
    private void getAllRecipe(){
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.getAllRecipe(1).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {

                recipeModelList = response.body();
                if (recipeModelList.size() > 0) {
                    recipeShowAllAdapter = new RecipeShowAllAdapter(getContext(), recipeModelList);
                    gridLayoutManager = new GridLayoutManager(getContext(), 2);
                    rv_recipe.setLayoutManager(gridLayoutManager);
                    rv_recipe.setAdapter(recipeShowAllAdapter);
                    rv_recipe.setHasFixedSize(true);

                    rv_recipe.setItemViewType((type, position) -> {
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
                    rv_recipe.showShimmer();     // to start showing shimmer
                    final Handler handler = new Handler();
                    handler.postDelayed((Runnable) () -> {
                        rv_recipe.hideShimmer(); // to hide shimmer
                    }, 1000);

                }

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toasty.error(getContext(), "Check your connection").show();

            }
        });
    }

    // set shimmer
    private void setShimmer(){
        rv_recipe.setAdapter(recipeShowAllAdapter);
        rv_recipe.setHasFixedSize(true);

        rv_recipe.setItemViewType((type, position) -> {
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
        rv_recipe.showShimmer();     // to start showing shimmer

    }
}