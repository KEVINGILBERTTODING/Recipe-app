package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Admin.Adapter.ReportBugAdapter;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.BugReportModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportBugFragment extends Fragment {
    ShimmerRecyclerView rv_report;
    LinearLayoutManager linearLayoutManager;
    ReportBugAdapter reportBugAdapter;
    TabLayout tabLayout;
    TextView tv_no_report;
    SearchView searchView;
    List<BugReportModel> bugReportModelList;
    ImageButton btnBack;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report_bug, container, false);

        rv_report = root.findViewById(R.id.rv_bug);
        tabLayout = root.findViewById(R.id.tab_layout);
        tv_no_report = root.findViewById(R.id.tv_no_report);
        searchView = root.findViewById(R.id.search_bar);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        btnBack = root.findViewById(R.id.btn_back);

        // when refreshimg not in tab layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllReport(1);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.main);

        btnBack.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });

        tabLayout.addTab(tabLayout.newTab().setText("Bug Reported"));
        tabLayout.addTab(tabLayout.newTab().setText("In Progress"));
        tabLayout.addTab(tabLayout.newTab().setText("Done"));
        tabLayout.addTab(tabLayout.newTab().setText("Rejected"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //get all report
                if (tab.getPosition() == 0) {
                    // when refreshimg not in tab layout
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

                // get done report
                else if(tab.getPosition() == 2) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getAllReport(3);
                        }
                    });
                    getAllReport(3);
                }

                // get reject report
                else if(tab.getPosition() == 3) {
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

        getAllReport(1);

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




        return root;
    }

    private void filter(String newText) {
        ArrayList<BugReportModel> filteredList = new ArrayList<>();
        filteredList = null;


        for (BugReportModel item : bugReportModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        reportBugAdapter.filterList(filteredList);

        if (filteredList.isEmpty()) {
            Toasty.error(getContext(), "Not found", Toasty.LENGTH_SHORT).show();
        } else {
            rv_report.hideShimmer();
            reportBugAdapter.filterList(filteredList);
        }
    }


    // get all report bug
    private void getAllReport(Integer status){
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllBugReport(status).enqueue(new Callback<List<BugReportModel>>() {
            @Override
            public void onResponse(Call<List<BugReportModel>> call, Response<List<BugReportModel>> response) {
                bugReportModelList = response.body();
                if (bugReportModelList.size() != 0) {
                    reportBugAdapter = new ReportBugAdapter(getContext(), bugReportModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_report.setLayoutManager(linearLayoutManager);
                    rv_report.setAdapter(reportBugAdapter);
                    rv_report.setHasFixedSize(true);
                    tv_no_report.setVisibility(View.GONE);
                    rv_report.setVisibility(View.VISIBLE);
                    swipeRefreshLayout .setRefreshing(false);


                    rv_report.showShimmer();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rv_report.hideShimmer();
                        }
                    }, 1200);


                } else {
                    rv_report.setVisibility(View.GONE);
                    tv_no_report.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                    Log.d("failed", "onResponse: ");
                    Log.e("failed", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BugReportModel>> call, Throwable t) {
                Toasty.error(getContext(), "Error no connection", Toasty.LENGTH_SHORT).show();
                Log.e("MyApp", t.getMessage());
                swipeRefreshLayout.setRefreshing(true);

                if (tabLayout.getSelectedTabPosition() == 0) {
                    getAllReport(1);

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    getAllReport(2);

                } else if (tabLayout.getSelectedTabPosition() == 2 ) {
                    getAllReport(3);

                } else {
                    getAllReport(0);

                }

            }
        });
    }


    @Override
    public void onResume() {
        rv_report.showShimmer();
        super.onResume();
    }


    @Override
    public void onPause() {
        rv_report.hideShimmer();
        super.onPause();
    }




}