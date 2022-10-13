package com.example.recipe_app.Fragment;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.recipe_app.Adapter.NewChatAdapter;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newChatFragment extends Fragment {

    ShimmerRecyclerView rvUser;
    ImageButton btnBack;
    private NewChatAdapter newChatAdapter;
    private List<ProfileModel> profileModelList;
    LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    TextView tvSearchUsername;
    private LinearLayout rlSearchUser;

    private ConnectivityManager conMgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.fragment_new_chat, container, false);

        // initilize
        btnBack = root.findViewById(R.id.btnBack);
        rvUser = root.findViewById(R.id.rvUser);
        searchView = root.findViewById(R.id.searchView);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        tvSearchUsername = root.findViewById(R.id.tvSearhUsername);
        rlSearchUser = root.findViewById(R.id.rlSearchUser);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllUser();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.main);


        // btn back listener
        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });






        // call method get all user
        rvUser.setVisibility(View.GONE);
        getAllUser();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                rlSearchUser.setVisibility(View.GONE);



                rvUser.showShimmer();
                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvUser.hideShimmer();
                        rvUser.setVisibility(View.VISIBLE);


                    }
                }, 1200);
                filter(newText);
                return false;
            }
        });

        return root;
    }

    private void filter(String newText) {

        ArrayList<ProfileModel>filteredList = new ArrayList<>();
        for (ProfileModel item : profileModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }

            newChatAdapter.filterList(filteredList);

            if (filteredList.isEmpty()) {
                rlSearchUser.setVisibility(View.VISIBLE);
                tvSearchUsername.setText("Username not found...");

            } else {
                newChatAdapter.filterList(filteredList);
                rlSearchUser.setVisibility(View.GONE);
            }
        }

    }

    // Method get all user
    private void getAllUser() {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getAllUser(1).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                if (profileModelList.size() > 0) {

                    newChatAdapter = new NewChatAdapter(getContext(), profileModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvUser.setAdapter(newChatAdapter);
                    rvUser.setLayoutManager(linearLayoutManager);
                    rvUser.setHasFixedSize(true);
                    swipeRefreshLayout.setRefreshing(false);

                    rvUser.showShimmer();

                    final Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvUser.hideShimmer();

                        }
                    }, 1200);

                } else {
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                    tvSearchUsername.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                getAllUser();
                swipeRefreshLayout.setRefreshing(true);

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
        rvUser.showShimmer();
        checkConnection();
        super.onResume();


    }
}