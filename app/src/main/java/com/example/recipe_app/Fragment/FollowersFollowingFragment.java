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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Adapter.FollowersAdapter;
import com.example.recipe_app.Adapter.FollowingAdapter;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersFollowingFragment extends Fragment {
    ImageButton btn_back;
    RecyclerView rv_user;
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


        userid = getArguments().getString("user_id");
        tv_username_bar.setText(getArguments().getString("username"));





        tabLayout.addTab(tabLayout.newTab().setText("Followers"));
        tabLayout.addTab(tabLayout.newTab().setText("Following"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getAllFollowers(userid);
                } else if (tab.getPosition() == 1) {
                    getAllFollowing(userid);

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

        getAllFollowers(userid);


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
                        filter(newText);

                    } else {
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

                    }

                } else {
                    rv_user.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error no connection", Toast.LENGTH_SHORT).show();
                

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


                } else {
                    rv_user.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toast.makeText(getContext(), "no connection", Toast.LENGTH_SHORT).show();

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
        filteredList =null;



        filteredList = new ArrayList<>() ;


        for (ProfileModel item : profileModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }

            followersAdapter.filterList(filteredList);
            Log.d("filter", "filter: " + filteredList);
            if (filteredList.isEmpty()) {
                Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
            } else {
                followersAdapter.filterList(filteredList);
            }
        }

    }

}