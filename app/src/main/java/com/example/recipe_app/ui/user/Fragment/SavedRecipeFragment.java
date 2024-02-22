package com.example.recipe_app.ui.user.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.ui.activities.auth.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.ui.activities.auth.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.ui.user.Adapter.SavedRecipeAdapter;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.data.remote.DataApi;
import com.example.recipe_app.data.remote.InterfaceRecipe;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;

public class SavedRecipeFragment extends Fragment {

    List<RecipeModel> recipeModelList;
    SavedRecipeAdapter savedRecipeAdapter;
    ShimmerRecyclerView rv_saved_recipe;
    String username, userid;
    SearchView searchView;
    TextView tv_notfound;
    SwipeRefreshLayout swipeRefreshLayout;
    ConnectivityManager conMgr;
    Context context;



    public SavedRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_recipe, container, false);
        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        rv_saved_recipe = view.findViewById(R.id.recycler_saved_recipe);
        searchView = view.findViewById(R.id.search_barr);
        tv_notfound = view.findViewById(R.id.tv_notfound);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        context = getContext();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                rv_saved_recipe.setItemViewType((type, position) -> {
                    switch (type) {
                        default:
                        case ShimmerRecyclerView.LAYOUT_LIST:
                            return position == 0 || position % 2 == 0
                                    ? R.layout.template_data_saved_recipe
                                    : R.layout.template_data_saved_recipe;
                    }
                });

                rv_saved_recipe.showShimmer();     // to start showing shimmer

                final Handler mHandler = new Handler();

                // To stimulate long running work using android.os.Handler
                mHandler.postDelayed((Runnable) () -> {
                    rv_saved_recipe.hideShimmer(); // to hide shimmer
                }, 1000);


                filter(newText);
                return false;
            }
        });

        // swipe for refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSavedRecipe(userid);
            }
        });

        // style load bar swipe refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);




        return view;
    }

    private void filter(String newText) {
        if (recipeModelList != null && recipeModelList.size() > 0) {
            ArrayList<RecipeModel> filteredList = new ArrayList<>();

            for (RecipeModel item : recipeModelList) {
                if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);

                }
            }


            savedRecipeAdapter.filterList(filteredList);


            if (filteredList.isEmpty()) {
                Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
            } else {
                savedRecipeAdapter.filterList(filteredList);
            }
        }
    }

    private void getSavedRecipe(String user_id) {

        DataApi.getClient().create(InterfaceRecipe.class).getSavedRecipe(user_id).enqueue(new retrofit2.Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, retrofit2.Response<List<RecipeModel>> response) {

                if (response.body().size() > 0) {
                    recipeModelList = response.body();
                    savedRecipeAdapter = new SavedRecipeAdapter(getContext(), recipeModelList);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    rv_saved_recipe.setLayoutManager(linearLayoutManager);
                    rv_saved_recipe.setAdapter(savedRecipeAdapter);
                    rv_saved_recipe.setHasFixedSize(true);
                    tv_notfound.setVisibility(View.GONE);
                    rv_saved_recipe.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                    rv_saved_recipe.setItemViewType((type, position) -> {
                        switch (type) {
                            default:
                            case ShimmerRecyclerView.LAYOUT_LIST:
                                return position == 0 || position % 2 == 0
                                        ? R.layout.template_data_saved_recipe
                                        : R.layout.template_data_saved_recipe;
                        }
                    });

                    rv_saved_recipe.showShimmer();     // to start showing shimmer

                    final Handler mHandler = new Handler();

                    // To stimulate long running work using android.os.Handler
                    mHandler.postDelayed((Runnable) () -> {
                        rv_saved_recipe.hideShimmer(); // to hide shimmer
                    }, 1000);


                } else {
                    tv_notfound.setVisibility(View.VISIBLE);
                    tv_notfound.setText("No recipe found");
                    rv_saved_recipe.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);




                }




            }
            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();
                tv_notfound.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(true);
                getSavedRecipe(userid);


            }
        });
    }

    private void setShimmer(){
        rv_saved_recipe.setLayoutManager(new LinearLayoutManager(getContext()),
                R.layout.template_data_saved_recipe);

        rv_saved_recipe.setAdapter(savedRecipeAdapter);
        rv_saved_recipe.setItemViewType((type, position) -> {
            switch (type) {
                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_data_saved_recipe
                            : R.layout.template_data_saved_recipe;
            }
        });

        rv_saved_recipe.showShimmer();     // to start showing shimmer

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
        getSavedRecipe(userid);
        checkConnection();

        // show shimer
        setShimmer();
        super.onResume();
    }

    @Override
    public void onPause() {
        rv_saved_recipe.hideShimmer();
        super.onPause();
    }
}