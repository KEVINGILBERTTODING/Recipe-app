package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Admin.Adapter.ReqVerifiedAdapter;
import com.example.recipe_app.Admin.Interface.InterfaceVerified;
import com.example.recipe_app.Admin.Model.VerificationModel;
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

public class VerifiedFragment extends Fragment {

    ImageButton btnBack;
    ShimmerRecyclerView rvUser;
    private List<VerificationModel> verificationModelList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ReqVerifiedAdapter reqVerifiedAdapter;
    TabLayout tabLayout;
    SearchView searchView;
    TextView tvNoRequest;
    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_verified, container, false);

        btnBack = root.findViewById(R.id.btn_back);
        rvUser = root.findViewById(R.id.rv_user);
        tabLayout = root.findViewById(R.id.tab_layout);
        searchView = root.findViewById(R.id.search_bar);
        tvNoRequest = root.findViewById(R.id.tv_no_request);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);



        tabLayout.addTab(tabLayout.newTab().setText("All request"));
        tabLayout.addTab(tabLayout.newTab().setText("Rejected"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // btn back listener
        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        // function search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvUser.showShimmer();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvUser.hideShimmer();
                    }
                },1200);
                filter(newText);
                return false;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // set selected tab position
                if (tab.getPosition() == 0) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getReqVerified();
                        }
                    });
                    getReqVerified();
                } else if (tabLayout.getSelectedTabPosition() == 1){
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getAllRejected();
                        }
                    });
                    getAllRejected();


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        // swipe listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReqVerified();
            }
        });

        getReqVerified();







        return  root;
    }

    // Method search data from adapter
    private void filter(String newText) {
        ArrayList<VerificationModel>filteredList = new ArrayList<>();
        for (VerificationModel item : verificationModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }

        }
        reqVerifiedAdapter.filterlist(filteredList);

        if (filteredList.isEmpty()) {
            Toasty.error(getContext(), "Not found",Toasty.LENGTH_SHORT).show();
        } else {
            reqVerifiedAdapter.filterlist(filteredList);
        }
    }

    // Method GET ALL REQUEST VERIFIED
    private void getReqVerified() {
        InterfaceVerified interfaceVerified = DataApi.getClient().create(InterfaceVerified.class);
        interfaceVerified.getAllRequest().enqueue(new Callback<List<VerificationModel>>() {
            @Override
            public void onResponse(Call<List<VerificationModel>> call, Response<List<VerificationModel>> response) {
                verificationModelList = response.body();
                if (verificationModelList.size() > 0) {
                    reqVerifiedAdapter = new ReqVerifiedAdapter(getContext(), verificationModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvUser.setAdapter(reqVerifiedAdapter);
                    rvUser.setLayoutManager(linearLayoutManager);
                    rvUser.setHasFixedSize(true);
                    swipeRefreshLayout.setRefreshing(false);
                    rvUser.setVisibility(View.VISIBLE);
                    tvNoRequest.setVisibility(View.GONE);
                    reqVerifiedAdapter.notifyDataSetChanged();


                    rvUser.showShimmer();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvUser.hideShimmer();

                        }
                    }, 1200);
                    



                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    tvNoRequest.setVisibility(View.VISIBLE);
                        rvUser.setVisibility(View.GONE);
                        rvUser.hideShimmer();

                }

            }

            @Override
            public void onFailure(Call<List<VerificationModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
               getReqVerified();

            }
        });
    }

    // METHOD GET REQUEST REJECTED
    private void getAllRejected() {
        InterfaceVerified interfaceVerified = DataApi.getClient().create(InterfaceVerified.class);
        interfaceVerified.getReqRejected().enqueue(new Callback<List<VerificationModel>>() {
            @Override
            public void onResponse(Call<List<VerificationModel>> call, Response<List<VerificationModel>> response) {

                verificationModelList = response.body();
                if (verificationModelList.size() > 0 ) {

                    reqVerifiedAdapter = new ReqVerifiedAdapter(getContext(), verificationModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvUser.setAdapter(reqVerifiedAdapter);
                    rvUser.setLayoutManager(linearLayoutManager);
                    rvUser.setHasFixedSize(true);

                    tvNoRequest.setVisibility(View.GONE);

                    swipeRefreshLayout.setRefreshing(false);
                    rvUser.setVisibility(View.VISIBLE);
                    tvNoRequest.setVisibility(View.GONE);
                    reqVerifiedAdapter.notifyDataSetChanged();


                    rvUser.showShimmer();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvUser.hideShimmer();

                        }
                    }, 1200);


                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    tvNoRequest.setVisibility(View.VISIBLE);
                    rvUser.setVisibility(View.GONE);
                    rvUser.hideShimmer();

                }

            }

            @Override
            public void onFailure(Call<List<VerificationModel>> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                getAllRejected();

            }
        });
    }


    @Override
    public void onResume() {
        rvUser.showShimmer();
        // change color swiperefresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);
        super.onResume();
    }

    @Override
    public void onPause() {
        rvUser.hideShimmer();
        super.onPause();
    }
}