package com.example.recipe_app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipe_app.Adapter.RecipeCategoryPopular;
import com.example.recipe_app.Adapter.RecipeShowAllAdapter;
import com.example.recipe_app.MainActivity;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
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
    ImageButton btn_back;
    ConnectivityManager conMgr;



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
        btn_back = view.findViewById(R.id.btn_back);

        // add tab recipe category item
        tabLayout.addTab(tabLayout.newTab().setText("Vegetables"));
        tabLayout.addTab(tabLayout.newTab().setText("Meat"));
        tabLayout.addTab(tabLayout.newTab().setText("Drinks"));
        tabLayout.addTab(tabLayout.newTab().setText("Noodle"));
        tabLayout.addTab(tabLayout.newTab().setText("Others"));



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getCategory("Vegetables", 1);
                        }
                    });
                    getCategory("Vegetables", 1);
                } else if (tab.getPosition() == 1) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getCategory("Meat", 1);
                        }
                    });
                    getCategory("Meat", 1);
                } else if (tab.getPosition() == 2) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getCategory("Drinks", 1);
                        }
                    });
                    getCategory("Drinks", 1);
                } else if (tab.getPosition() == 3) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getCategory("Noodle", 1);
                        }
                    });
                    getCategory("Noodle", 1);
                } else if (tab.getPosition() == 4) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getCategory("Others", 1);
                        }
                    });
                    getCategory("Others", 1);
                } else {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getCategory("Meat", 1);
                        }
                    });
                    getCategory("Meat", 1);
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
                getCategory("Vegetables", 1);
            }
        });

        // change color swipe refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);



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

                final Handler handler = new Handler();
                handler.postDelayed((Runnable) () -> {
                    shimmerRecyclerView.hideShimmer();
                }, 1000);


            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(true);
                if (tabLayout.getSelectedTabPosition() == 0) {


                    getCategory("Vegetables", 1);
                } else if (tabLayout.getSelectedTabPosition() == 1) {

                    getCategory("Meat", 1);
                } else if (tabLayout.getSelectedTabPosition() == 2) {

                    getCategory("Drinks", 1);
                } else if (tabLayout.getSelectedTabPosition() == 3) {

                    getCategory("Noodle", 1);
                } else if (tabLayout.getSelectedTabPosition() == 4) {

                    getCategory("Others", 1);
                } else {

                    getCategory("Meat", 1);
                }


            }


        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String querry) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                shimmerRecyclerView.showShimmer();     // to start showing shimmer
                shimmerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2) );

                final Handler handler = new Handler();
                handler.postDelayed((Runnable) () -> {
                    shimmerRecyclerView.hideShimmer();
                }, 1000);
                filter(newText);
                return true;
            }
        });
    }

    private void setShimmerCategoryRecipe(){
        shimmerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2), R.layout.template_lits_data_recipe_category);
        shimmerRecyclerView.setItemViewType((type, position) -> {
            switch (type) {

                default:
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
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
        setShimmerCategoryRecipe();
        getCategory("Vegetables", 1);
        checkConnection();
        super.onResume();
    }

    @Override
    public void onPause() {
        shimmerRecyclerView.hideShimmer();
        super.onPause();
    }
}