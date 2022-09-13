package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;
import static com.example.recipe_app.Util.ServerAPI.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Adapter.RecipeAllAdapter;
import com.example.recipe_app.Adapter.RecipeCategoryPopular;
import com.example.recipe_app.Adapter.RecipeTrandingAdapter;
import com.example.recipe_app.Model.NotificationModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceNotification;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    String username, userid;
    TextView tv_username, tvTotalNotif;
    ShimmerRecyclerView shimmerRecyclerView, shimmerRecipeCategoryPopular, shimmerRecipeTrending;
    private List<RecipeModel> recipeModelList;
    private InterfaceRecipe interfaceRecipe;
    View view1;

    private List<ProfileModel> profileModelList;
    InterfaceProfile interfaceProfile;
    RecipeAllAdapter recipeAllAdapter;
    RelativeLayout layoutHeader, rlCountNotif;
    RecipeCategoryPopular recipeCategoryPopular;
    RecipeTrandingAdapter recipeTrandingAdapter;
    TabLayout tabLayout;
    ImageView img_profile;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton btn_see_all_recipes, btn_see_all_trendings, btn_see_all_categories, btn_notification;
    Context context;




    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        context = getContext();
        view1 = getView();

        shimmerRecyclerView = view.findViewById(R.id.recycler_recipe_all);
        shimmerRecipeCategoryPopular = view.findViewById(R.id.recycler_recipe_category);
        shimmerRecipeTrending = view.findViewById(R.id.recycler_recipe_trending);
        tv_username = view.findViewById(R.id.tv_username);
        img_profile = view.findViewById(R.id.img_profile);
        tabLayout = view.findViewById(R.id.tab_layout);
        searchView = view.findViewById(R.id.search_barr);
        layoutHeader = view.findViewById(R.id.layout_header);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        btn_see_all_categories = view.findViewById(R.id.btn_see_all_categories);
        btn_see_all_recipes = view.findViewById(R.id.btn_see_all);
        btn_see_all_trendings = view.findViewById(R.id.btn_see_all_trending);
        btn_notification = view.findViewById(R.id.btn_notification);
        rlCountNotif = view.findViewById(R.id.rl_count_notif);
        tvTotalNotif = view.findViewById(R.id.tv_total_notif);

        // add tab recipe category item
        tabLayout.addTab(tabLayout.newTab().setText("Vegetables"));
        tabLayout.addTab(tabLayout.newTab().setText("Meat"));
        tabLayout.addTab(tabLayout.newTab().setText("Drinks"));
        tabLayout.addTab(tabLayout.newTab().setText("Noodle"));
        tabLayout.addTab(tabLayout.newTab().setText("Others"));

        // set username
        tv_username.setText("Hi, "+username);

        // Every 3 second will refresh notification
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countNotification();
                handler.postDelayed(this,3000);
            }
        }, 1000);



        // saat image profile di klik
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                MyProfileFragment profileFragment = new MyProfileFragment();
                fragmentTransaction.replace(R.id.fragment_container, profileFragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        // when btn notification is clicked
        btn_notification.setOnClickListener(view2 ->  {
            readNotification(userid);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new NotificationFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        // when refresh swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
        });

        // Call method get recipe
        getAllRecipe();
        getCategory("Vegetables", 1);
        getRecipeTranding(1,1);

        // when tabLayout and then excute method get recipe by category
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getCategory("Vegetables", 1);
                } else if (tab.getPosition() == 1) {
                    getCategory("Meat", 1);
                } else if (tab.getPosition() == 2) {
                    getCategory("Drinks", 1);
                } else if (tab.getPosition() == 3) {
                    getCategory("Noodle", 1);
                } else if (tab.getPosition() == 4) {
                    getCategory("Others", 1);
                } else {
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

        // button see all recipe
        btn_see_all_recipes.setOnClickListener(View ->{

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, new RecentRecipesFragment());
            fragmentTransaction.commit();

        });
        btn_see_all_categories.setOnClickListener(View ->{

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, new CategoryFragment());
            fragmentTransaction.commit();
        });

        btn_see_all_trendings.setOnClickListener(View ->{
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, new TrendingRecipesFragment());
            fragmentTransaction.commit();
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() == 0) {
                }
                else {
                    getInput(newText);
                }

                return false;
            }
        });

        // call method get profile image
        getProfileImage(userid);

        // change color swipe for refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);




        return view;
    }

    public void getInput(String searchText)
    {

        // send data to new fragment

        Fragment fragment = new AllRecipesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("searchText", searchText);
        fragment.setArguments(bundle);
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, fragment, "AllRecipesFragment");


    }


    private void refreshItem() {
        getAllRecipe();
        getCategory("Vegetables", 1);
        getRecipeTranding(1,1);
        swipeRefreshLayout.setRefreshing(false);

    }

    private void getRecipeTranding(Integer status, Integer likes) {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getRecipeTranding(status, likes);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipeTrandingAdapter = new RecipeTrandingAdapter(getContext(), recipeModelList);
                // Make it Horizontal recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                shimmerRecipeTrending.setLayoutManager(linearLayoutManager);
                shimmerRecipeTrending.setAdapter(recipeTrandingAdapter);
                shimmerRecipeTrending.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);



                shimmerRecipeTrending.showShimmer();
                final Handler handller  = new Handler();
                handller.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shimmerRecipeTrending.hideShimmer();
                    }
                },1000);




            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_LONG).show();
                getRecipeTranding(1, 1);


            }


        });
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
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                shimmerRecipeCategoryPopular.setLayoutManager(linearLayoutManager);
                shimmerRecipeCategoryPopular.setAdapter(recipeCategoryPopular);
                shimmerRecipeCategoryPopular.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);

                // show shimmer when get data success
                shimmerRecipeCategoryPopular.showShimmer();
                final Handler handller  = new Handler();
                handller.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shimmerRecipeCategoryPopular.hideShimmer();
                    }
                },1000);




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
    }

    private void getAllRecipe() {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getAllRecipe(1);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipeAllAdapter = new RecipeAllAdapter(getContext(), recipeModelList);
                // Make it Horizontal recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                shimmerRecyclerView.setLayoutManager(linearLayoutManager);
                shimmerRecyclerView.setAdapter(recipeAllAdapter);
                shimmerRecyclerView.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);



                shimmerRecipeTrending.showShimmer();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shimmerRecipeTrending.hideShimmer();
                    }
                }, 1000);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
                getAllRecipe();
            }


        });
    }


    // get profile image
    private void getProfileImage(String user_id) {
        DataApi.getClient().create(InterfaceProfile.class).getProfile(user_id).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                if (profileModelList.size() > 0) {
                    ProfileModel profileModel;
                    profileModel = profileModelList.get(0);
                    Glide.with(getContext())
                            .load(profileModel.getPhoto_profile())
                            .thumbnail(0.5f)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                            .fitCenter()
                            .placeholder(R.drawable.template_img)
                            .centerCrop()
                            .override(200, 200)
                            .into(img_profile);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Log.e("Disini Error nyaa", t.getMessage());
                swipeRefreshLayout.setRefreshing(true);
                getProfileImage(userid);
            }
        });


    }

    // Count total notification status = '1'
    private void countNotification(){
        InterfaceNotification interfaceNotification = DataApi.getClient().create(InterfaceNotification.class);
        interfaceNotification.countTotalNotif(userid).enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.body().size() > 0 ) {
                    rlCountNotif.setVisibility(View.VISIBLE);
                    tvTotalNotif.setText(response.body().size() + "");
                } else  {
                    rlCountNotif.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                Toasty.error(getContext(), "No connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }


    // method to read notification where user click button notification then update status notif
    // where 1 to

    private void readNotification(String userid) {
        InterfaceNotification interfaceNotification = DataApi.getClient().create(InterfaceNotification.class);
        interfaceNotification.readNotif(userid).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if (response.body().getStatus().equals("1")) {


                } else {
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        shimmerRecyclerView.showShimmer();
        shimmerRecipeTrending.showShimmer();
        shimmerRecipeCategoryPopular.showShimmer();
        countNotification();
        super.onResume();
    }


    @Override
    public void onPause() {
        shimmerRecyclerView.hideShimmer();
        shimmerRecipeTrending.hideShimmer();
        shimmerRecipeCategoryPopular.hideShimmer();
        super.onPause();
    }
}