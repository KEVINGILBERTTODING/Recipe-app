package com.example.recipe_app.Admin.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Admin.Adapter.ReportRecipeAdapter;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.RecipeReportmodel;
import com.example.recipe_app.Admin.Model.UserReportModel;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportRecipeFragment extends Fragment {
    ImageButton btnBack;
    ShimmerRecyclerView rv_recipe;
    List<RecipeReportmodel> recipeReportmodelList;
    ReportRecipeAdapter reportRecipeAdapter;
    TabLayout tabLayout;
    LinearLayoutManager linearLayoutManager;
    SearchView searchView;
    TextView tv_no_data;
    SwipeRefreshLayout swipeRefreshLayout;
    ConnectivityManager conMgr;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report_recipe, container, false);
        btnBack = root.findViewById(R.id.btn_back);
        rv_recipe = root.findViewById(R.id.rv_recipe);
        tabLayout = root.findViewById(R.id.tab_layout);
        tv_no_data = root.findViewById(R.id.tv_no_report);
        searchView = root.findViewById(R.id.search_bar);
        swipeRefreshLayout =  root.findViewById(R.id.swipe_refresh);
        btnBack.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });

        getRecipeReport(1);


        tabLayout.addTab(tabLayout.newTab().setText("Reported Recipe"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Rejected"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //get all report
                if (tab.getPosition() == 0) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getRecipeReport(1);
                        }
                    });
                    getRecipeReport(1);
                }

                // get accepted report
                else if  (tab.getPosition() == 1) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getRecipeReport(2);
                        }
                    });
                    getRecipeReport(2);
                }

                // get rejcted report
                else if(tab.getPosition() == 2) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getRecipeReport(0);
                        }
                    });
                    getRecipeReport(0);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });


        // swipe refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecipeReport(1);
            }
        });





        return root;
    }

    private void filter(String newText) {
        ArrayList<RecipeReportmodel> filteredList = new ArrayList<>();
        for (RecipeReportmodel item : recipeReportmodelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        reportRecipeAdapter.filterList(filteredList);
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
        } else {
           reportRecipeAdapter.filterList(filteredList);
        }
    }

    // get all report
    private void getRecipeReport(Integer status){
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllReportRecipe(status).enqueue(new Callback<List<RecipeReportmodel>>() {
            @Override
            public void onResponse(Call<List<RecipeReportmodel>> call, Response<List<RecipeReportmodel>> response) {
                recipeReportmodelList = response.body();

                if (recipeReportmodelList.size() > 0) {
                    reportRecipeAdapter = new ReportRecipeAdapter(getContext(), recipeReportmodelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_recipe.setAdapter(reportRecipeAdapter);
                    rv_recipe.setLayoutManager(linearLayoutManager);
                    rv_recipe.setHasFixedSize(true);
                    tv_no_data.setVisibility(View.GONE);
                    rv_recipe.setVisibility(View.VISIBLE);

                    swipeRefreshLayout.setRefreshing(false);
                    rv_recipe.showShimmer();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rv_recipe.hideShimmer();
                        }
                    }, 1200);

                    
                } else  {
                    rv_recipe.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<RecipeReportmodel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                if (tabLayout.getSelectedTabPosition() == 0) {
                    swipeRefreshLayout.setRefreshing(true);
                    getRecipeReport(1);
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    getRecipeReport(2);
                } else {
                    getRecipeReport(0);
                }






            }
        });
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
        checkConnection();
        rv_recipe.showShimmer();
        super.onResume();
    }

    @Override
    public void onPause() {
        rv_recipe.hideShimmer();
        super.onPause();
    }
}