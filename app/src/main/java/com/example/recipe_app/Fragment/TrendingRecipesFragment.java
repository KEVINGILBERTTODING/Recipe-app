package com.example.recipe_app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipe_app.ui.Adapter.RecipeTrandingAdapter2;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
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
    ImageButton btn_back;
    ConnectivityManager conMgr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_trending_recipes, container, false);

        shimmerRecyclerView = view.findViewById(R.id.recycler_recipe_trendings);
        searchView = view.findViewById(R.id.search_all_recipes);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        btn_back = view.findViewById(R.id.btn_back);

        // Change color swipe refresh icon
        swipeRefreshLayout.setColorSchemeResources(R.color.main);



        // when refresh swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
        });

        // button back listener
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
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
        shimmerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2), R.layout.template_list_data_recipe_trending);
        shimmerRecyclerView.setItemViewType((type, position) -> {
            switch (type) {
                default:
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
                            ? R.layout.template_data_show_all
                            : R.layout.template_data_show_all;



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

                shimmerRecyclerView.setItemViewType((type, position) -> {
                    switch (type) {
                        default:
                        case ShimmerRecyclerView.LAYOUT_GRID:
                            return position % 2 == 0
                                    ? R.layout.template_data_show_all
                                    : R.layout.template_data_show_all;

                    }
                });
                shimmerRecyclerView.showShimmer();     // to start showing shimmer

                final Handler handler =  new Handler();
                handler.postDelayed((Runnable) () -> {
                    shimmerRecyclerView.hideShimmer();
                }, 1000);


            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                getRecipeTranding(1, 1);
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

    // method check connection
    private void checkConnection() {
        conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    &&
                    conMgr.getActiveNetworkInfo().isAvailable()
                    &&
                    conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        setShimmer();
        getRecipeTranding(1, 1);
//        checkConnection();
        super.onResume();
    }

    @Override
    public void onPause() {
        shimmerRecyclerView.hideShimmer();
        super.onPause();
    }
}