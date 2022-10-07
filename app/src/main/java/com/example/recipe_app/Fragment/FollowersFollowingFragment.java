package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import com.example.recipe_app.Adapter.FollowersAdapter;
import com.example.recipe_app.Adapter.FollowingAdapter;
import com.example.recipe_app.Adapter.MyFollowersAdapter;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersFollowingFragment extends Fragment {
    ImageButton btn_back;
    ShimmerRecyclerView rv_user;
    TabLayout tabLayout;
    List<ProfileModel> profileModelList;
    String userid;
    FollowersAdapter followersAdapter;
    LinearLayoutManager linearLayoutManager;
    FollowingAdapter followingAdapter;
    SearchView searchFollowing;
    ArrayList<ProfileModel> filteredList;
    TextView tv_username_bar;
    TextView tv_no_data;
    MyFollowersAdapter myFollowersAdapter;
    SwipeRefreshLayout swipeRefreshLayout;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_followers_following, container, false);
        btn_back = root.findViewById(R.id.btn_back);
        rv_user = root.findViewById(R.id.rv_user);
        searchFollowing = root.findViewById(R.id.search_following);
        tabLayout = root.findViewById(R.id.tab_layout);
        tv_username_bar = root.findViewById(R.id.tv_username_bar);
        tv_no_data = root.findViewById(R.id.tv_notfound);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);


        userid = getArguments().getString("user_id");
        tv_username_bar.setText(getArguments().getString("username"));


        // Create tablayout
        tabLayout.addTab(tabLayout.newTab().setText("Followers"));
        tabLayout.addTab(tabLayout.newTab().setText("Following"));



        // tab selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (getArguments().getString("my_profile") != null) {
                    if (tab.getPosition() == 0) {
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                getMyFollowers(userid);
                            }
                        });
                        getMyFollowers(userid);
                    } else if (tab.getPosition() == 1) {
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                getAllFollowing(userid);
                            }
                        });
                        getAllFollowing(userid);
                    }
                } else {

                    if (tab.getPosition() == 0) {
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                getAllFollowers(userid);
                            }
                        });
                        getAllFollowers(userid);
                    } else if (tab.getPosition() == 1) {
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                getAllFollowing(userid);
                            }
                        });
                        getAllFollowing(userid);

                    }
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        // button back
        btn_back.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });

        if (getArguments().getString("my_profile") != null) {
            getMyFollowers(userid);
        } else {
            getAllFollowers(userid);
        }

        // change icon color swipe refreshh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);




        // Search bar followers
        searchFollowing.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if (newText != null && !newText.isEmpty()) {
                    if (tabLayout.getSelectedTabPosition() == 0){

                        /* Shimmer layout view type depending on List / Gird */
                        rv_user.setItemViewType((type, position) -> {
                            switch (type) {


                                default:
                                case ShimmerRecyclerView.LAYOUT_LIST:
                                    return position == 0 || position % 2 == 0
                                            ? R.layout.template_list_user_search
                                            : R.layout.template_list_user_search;
                            }
                        });

                        rv_user.showShimmer();     // to start showing shimmer

                        // To stimulate long running work using android.os.Handler
                        final Handler handler = new Handler();

                        handler.postDelayed((Runnable) () -> {
                            rv_user.hideShimmer(); // to hide shimmer
                        }, 1000);
                        filter(newText);

                    } else {

                        /* Shimmer layout view type depending on List / Gird */
                        rv_user.setItemViewType((type, position) -> {
                            switch (type) {


                                default:
                                case ShimmerRecyclerView.LAYOUT_LIST:
                                    return position == 0 || position % 2 == 0
                                            ? R.layout.template_list_user_search
                                            : R.layout.template_list_user_search;
                            }
                        });

                        rv_user.showShimmer();     // to start showing shimmer

                        // To stimulate long running work using android.os.Handler
                        final Handler handler = new Handler();

                        handler.postDelayed((Runnable) () -> {
                            rv_user.hideShimmer(); // to hide shimmer
                        }, 1000);
                        filterFollowing(newText);
                    }


                } else {
                    if (tabLayout.getSelectedTabPosition() == 0) {
                        getAllFollowers(userid);
                    } else if (tabLayout.getSelectedTabPosition() == 1) {
                        getAllFollowing(userid);
                    }
                }



                return false;
            }
        });








        return root;
    }

    // get all followers
    private void getAllFollowers(String useridx){
        InterfaceProfile interfaceProfile1 = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile1.getAllFollowers(useridx).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                if (profileModelList.size() > 0) {
                    if (profileModelList.get(0).getUsername() != null) {
                        rv_user.setVisibility(View.VISIBLE);
                        followersAdapter = new FollowersAdapter(getContext(), profileModelList);
                        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rv_user.setLayoutManager(linearLayoutManager);
                        rv_user.setAdapter(followersAdapter);
                        rv_user.setHasFixedSize(true);
                        tv_no_data.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                        /* Shimmer layout view type depending on List / Gird */
                        rv_user.setItemViewType((type, position) -> {
                            switch (type) {


                                default:
                                case ShimmerRecyclerView.LAYOUT_LIST:
                                    return position == 0 || position % 2 == 0
                                            ? R.layout.template_list_user_search
                                            : R.layout.template_list_user_search;
                            }
                        });

                        rv_user.showShimmer();     // to start showing shimmer

                        // To stimulate long running work using android.os.Handler
                        final Handler handler = new Handler();

                        handler.postDelayed((Runnable) () -> {
                            rv_user.hideShimmer(); // to hide shimmer
                        }, 1000);
                    }



                } else {
                    rv_user.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error no connection", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                getAllFollowers(useridx);
                

            }
        });
    }


    // get My followers
    private void getMyFollowers(String useridx){
        InterfaceProfile interfaceProfile1 = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile1.getAllFollowers(useridx).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                if (profileModelList.size() > 0) {
                    if (profileModelList.get(0).getUsername() != null) {
                        rv_user.setVisibility(View.VISIBLE);
                        myFollowersAdapter = new MyFollowersAdapter(getContext(), profileModelList);
                        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rv_user.setLayoutManager(linearLayoutManager);
                        rv_user.setAdapter(myFollowersAdapter);
                        rv_user.setHasFixedSize(true);
                        tv_no_data.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                        /* Shimmer layout view type depending on List / Gird */
                        rv_user.setItemViewType((type, position) -> {
                            switch (type) {


                                default:
                                case ShimmerRecyclerView.LAYOUT_LIST:
                                    return position == 0 || position % 2 == 0
                                            ? R.layout.template_list_user_search
                                            : R.layout.template_list_user_search;
                            }
                        });

                        rv_user.showShimmer();     // to start showing shimmer

                        // To stimulate long running work using android.os.Handler
                        final Handler handler = new Handler();

                        handler.postDelayed((Runnable) () -> {
                            rv_user.hideShimmer(); // to hide shimmer
                        }, 1000);
                    }



                } else {
                    rv_user.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error no connection", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                getMyFollowers(useridx);


            }
        });
    }
    
    // get all following
    private void getAllFollowing(String useridx){
        InterfaceProfile interfaceProfile1 = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile1.getAllFollowing(useridx).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                if (profileModelList.size() > 0 ) {
                    rv_user.setVisibility(View.VISIBLE);

                    followingAdapter = new FollowingAdapter(getContext(), profileModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_user.setLayoutManager(linearLayoutManager);
                    rv_user.setAdapter(followingAdapter);
                    rv_user.setHasFixedSize(true);
                    tv_no_data.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    /* Shimmer layout view type depending on List / Gird */
                    rv_user.setItemViewType((type, position) -> {
                        switch (type) {


                            default:
                            case ShimmerRecyclerView.LAYOUT_LIST:
                                return position == 0 || position % 2 == 0
                                        ? R.layout.template_list_user_search
                                        : R.layout.template_list_user_search;
                        }
                    });

                    rv_user.showShimmer();     // to start showing shimmer

                    // To stimulate long running work using android.os.Handler
                    final Handler handler = new Handler();

                    handler.postDelayed((Runnable) () -> {
                        rv_user.hideShimmer(); // to hide shimmer
                    }, 1000);


                } else {
                    rv_user.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toast.makeText(getContext(), "no connection", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                getAllFollowing(useridx);

            }
        });
    }


    private void filterFollowing(String newText) {
        filteredList = null;
        filteredList = new ArrayList<>();
        for (ProfileModel item : profileModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }

            followingAdapter.followingList(filteredList);
            if (filteredList.isEmpty()) {
                Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
            } else {
                followingAdapter.followingList(filteredList);
            }
        }
    }

    private void filter(String newText) {
        filteredList = null;



        filteredList = new ArrayList<>() ;


        for (ProfileModel item : profileModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }

            followersAdapter.filterList(filteredList);
            if (filteredList.isEmpty()) {
                Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
            } else {
                followersAdapter.filterList(filteredList);
            }
        }

    }


    // set shimmer
    private void setShimmer() {
        /* Shimmer layout view type depending on List / Gird */
        rv_user.setAdapter(followersAdapter);
        rv_user.setItemViewType((type, position) -> {
            switch (type) {


                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_list_user_search
                            : R.layout.template_list_user_search;
            }
        });

        rv_user.showShimmer();     // to start showing shimmer


    }

    @Override
    public void onResume() {
        //show shimmer
        setShimmer();
        super.onResume();
    }

    @Override
    public void onPause() {
        rv_user.hideShimmer();
        super.onPause();
    }
}