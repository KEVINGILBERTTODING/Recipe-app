package com.example.recipe_app.Admin.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
//import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.recipe_app.Admin.Adapter.ListUsersAdapter;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    ShimmerRecyclerView rv_user;
    ListUsersAdapter listUsersAdapter;
    List<AdminModel> adminModelList;
    ImageButton btnBack;
    SearchView searchView;
    TabLayout tabLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_noReport;
    ConnectivityManager conMgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        rv_user = view.findViewById(R.id.rv_user);
        btnBack = view.findViewById(R.id.btn_back);
        searchView = view.findViewById(R.id.search_barr);
        tabLayout = view.findViewById(R.id.tab_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        tv_noReport = view.findViewById(R.id.tv_no_report);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_admin, new DashboardFragment());
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        // call method get data
        getAllUser(1);

        // add tab lst user activate and deactivate
        tabLayout.addTab(tabLayout.newTab().setText("Activate"));
        tabLayout.addTab(tabLayout.newTab().setText("Deactivate"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getAllUser(1);
                        }
                    });
                    getAllUser(1);
                }
                if (tab.getPosition() == 1) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getAllUser(0);
                        }
                    });
                    getAllUser(0);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        // search user
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

        swipeRefreshLayout.setColorSchemeResources(R.color.main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllUser(1);
            }
        });


        return view;
    }

    private void filter(String newText) {
        ArrayList<AdminModel> filteredList = new ArrayList<>();

        for (AdminModel item : adminModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);

            }
        }


        listUsersAdapter.filterList(filteredList);


        if (filteredList.isEmpty()) {
            Toasty.error(getContext(), "Not found", Toasty.LENGTH_SHORT).show();
        } else {
            listUsersAdapter.filterList(filteredList);
        }


    }


    // method for get all user data
    private void getAllUser(Integer active) {
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllUser(active).enqueue(new Callback<List<AdminModel>>() {
            @Override
            public void onResponse(Call<List<AdminModel>> call, Response<List<AdminModel>> response) {
                if (response.body().size() > 0){
                    adminModelList = response.body();
                    listUsersAdapter = new ListUsersAdapter(getContext(), adminModelList);
                    rv_user.setAdapter(listUsersAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_user.setLayoutManager(linearLayoutManager);
                    rv_user.setHasFixedSize(true);
                    rv_user.setVisibility(View.VISIBLE);

                    swipeRefreshLayout.setRefreshing(false);
                    tv_noReport.setVisibility(View.GONE);

                    rv_user.showShimmer();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rv_user.hideShimmer();
                        }
                    }, 1200);
                } else {
                    tv_noReport.setVisibility(View.VISIBLE);
                    rv_user.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);


                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);


                // get selected tablayout position
                if (tabLayout.getSelectedTabPosition() == 0) {
                    getAllUser(1);
                } else {
                    getAllUser(0);
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
        rv_user.showShimmer();
        super.onResume();

    }

    @Override
    public void onPause() {
        rv_user.hideShimmer();
        super.onPause();
    }
}