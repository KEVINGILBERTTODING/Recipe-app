package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Admin.Adapter.ReportUserAdapter;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Admin.Model.UserReportModel;
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

public class ReportUserFragment extends Fragment {
    ShimmerRecyclerView rv_user;
    List<UserReportModel> userReportModelList;
    ReportUserAdapter reportUserAdapter;
    LinearLayoutManager linearLayoutManager;
    TabLayout tabLayout;
    SearchView searchView;
    TextView tvNoReport;
    ImageButton btnBack;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_user, container, false);

        rv_user = view.findViewById(R.id.rv_user);
        tabLayout = view.findViewById(R.id.tab_layout);
        searchView = view.findViewById(R.id.search_bar);
        tvNoReport = view.findViewById(R.id.tv_no_report);
        btnBack = view.findViewById(R.id.btn_back);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });

        // change color swiper refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllReport(1);

            }
        });


        getAllReport(1);

        tabLayout.addTab(tabLayout.newTab().setText("Reported User"));
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
                            getAllReport(1);
                        }
                    });
                    getAllReport(1);
                }

                // get accepted report
                else if  (tab.getPosition() == 1) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getAllReport(2);
                        }
                    });

                    getAllReport(2);
                }

                // get rejcted report
                else if(tab.getPosition() == 2) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getAllReport(0);
                        }
                    });
                    getAllReport(0);
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


        return view;
    }

    private void filter(String newText) {
        ArrayList<UserReportModel> filteredList = new ArrayList<>();
        for (UserReportModel item : userReportModelList) {
            if (item.getUsername1().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        reportUserAdapter.filterList(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            reportUserAdapter.filterList(filteredList);
        }
    }


    // get all report
    private void getAllReport(Integer status){
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllReport(status).enqueue(new Callback<List<UserReportModel>>() {
            @Override
            public void onResponse(Call<List<UserReportModel>> call, Response<List<UserReportModel>> response) {


                    if (response.isSuccessful()) {
                        userReportModelList = response.body();
                        reportUserAdapter = new ReportUserAdapter(getContext(), userReportModelList);
                        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
                        rv_user.setLayoutManager(linearLayoutManager);
                        rv_user.setAdapter(reportUserAdapter);
                        rv_user.setHasFixedSize(true);
                        tvNoReport.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                        rv_user.showShimmer();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rv_user.hideShimmer();
                            }
                        }, 1200);

                        if (userReportModelList.size() == 0) {
                            tvNoReport.setVisibility(View.VISIBLE);
                            rv_user.hideShimmer();
                            swipeRefreshLayout.setRefreshing(false);

                        }


                    } else {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                

            }

            @Override
            public void onFailure(Call<List<UserReportModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                if (tabLayout.getSelectedTabPosition() == 0) {
                    getAllReport(1);

                } else if(tabLayout.getSelectedTabPosition() == 1) {
                    getAllReport(2);

                } else {
                    getAllReport(0);
                }



            }
        });




    }

    @Override
    public void onResume() {
        rv_user.showShimmer();
        super.onResume();
    }

    @Override
    public void onPause() {
        rv_user.hideShimmer();
        super.onPause();
    }
}