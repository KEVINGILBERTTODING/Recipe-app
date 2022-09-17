package com.example.recipe_app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Adapter.MyRecipeAdapter;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProfileFragment extends Fragment implements MyRecipeAdapter.OnRecipeListener {
    String user_id;
    ImageView iv_profile, ivReport;
    TextView tv_username, tv_email, tv_biography, tv_date, tv_time, tv_notfound;

    List<ProfileModel> profileModelList;
    ProfileModel profileModel;
    ShimmerRecyclerView rv_recipe;
    MyRecipeAdapter myRecipeAdapter;
    List<RecipeModel> recipeModelList;
    public static ArrayList<RecipeModel> mItems = new ArrayList<>();
    ImageButton btnBack, btnMore, btnQrCode;
    Button btnReport;
    LinearLayout lrImagePicker, lr_info;
    RelativeLayout rlImagePicker;
    Dialog reportForm;
    Bitmap bitmap;
    ProgressDialog pd;
    EditText et_report, et_title;
    String image, userid;
    private final int TAG_GALLERY = 200;
    SwipeRefreshLayout swipeRefreshLayout;
    ConnectivityManager conMgr;
    ImageView icVerified;

    LinearLayout lr_button;
    Context context;

    // textview to count total post, followers and following
    TextView tv_post, tv_followers, tv_following;

    TabLayout tabLayout;

    Button btn_follow, btn_message, btn_unfollow;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_profile, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);


        iv_profile = view.findViewById(R.id.iv_profile);
        tv_username = view.findViewById(R.id.tv_username);
        tv_email = view.findViewById(R.id.tv_email);
        tv_biography = view.findViewById(R.id.tv_biography);
        tv_date = view.findViewById(R.id.tv_date);
        tv_time = view.findViewById(R.id.tv_time);
        tabLayout = view.findViewById(R.id.tab_layout);
        rv_recipe = view.findViewById(R.id.recycler_recipe);
        btnBack = view.findViewById(R.id.btn_back);
        btnMore = view.findViewById(R.id.btn_more);
        lr_info = view.findViewById(R.id.lr_info_account);
        btnQrCode = view.findViewById(R.id.btn_qrcode);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        icVerified = view.findViewById(R.id.img_verified);

        tv_post = view.findViewById(R.id.tv_post);
        tv_followers = view.findViewById(R.id.tv_followers);
        tv_following = view.findViewById(R.id.tv_following);
        tv_notfound = view.findViewById(R.id.tv_notfound);

        btn_follow = view.findViewById(R.id.btn_follow);
        btn_message = view.findViewById(R.id.btn_message);
        btn_unfollow = view.findViewById(R.id.btn_unfollow);
        lr_button = view.findViewById(R.id.lr_button);


        reportForm = new Dialog(getContext());
        pd = new ProgressDialog(getContext());
        context = getContext();

        // change color swipe refresh icon
        swipeRefreshLayout.setColorSchemeResources(R.color.main);

        // mengambil data dari adapter menggunakan bundle
        user_id = getArguments().getString("user_id");

        // Mengambil data profile dari API
        getProfile(user_id);

        // mengambil data recipe dari API
        getRecipe(user_id, 1);


        // jika user id sama dengan user id profile maka btnmore dan button follow dihilangkan
        if (user_id.equals(userid)) {
            btnMore.setVisibility(View.GONE);
            lr_button.setVisibility(View.GONE);
        } else{
            btnMore.setVisibility(View.VISIBLE);
            lr_button.setVisibility(View.VISIBLE);
        }

        // show dialog saat klik button more
        btnMore.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Report user");
            builder.setItems(new CharSequence[] {"Report user"}, (dialog, which)->{
                switch (which){
                    case 0:
                        reportForm.setContentView(R.layout.layout_report_user);
                        reportForm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        lrImagePicker = reportForm.findViewById(R.id.lr_image_picker);
                        rlImagePicker = reportForm.findViewById(R.id.rl_image_picker);
                        ivReport = reportForm.findViewById(R.id.iv_report);
                        et_report = reportForm.findViewById(R.id.edt_report);
                        et_title = reportForm.findViewById(R.id.edt_title);


                        btnReport = reportForm.findViewById(R.id.btnReport);
                        lrImagePicker.setOnClickListener(view2 -> {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, TAG_GALLERY);

                            }
                        });

                        rlImagePicker.setOnClickListener(view3 -> {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, TAG_GALLERY);

                            }
                        });

                        btnReport.setOnClickListener(view2 -> {

                            // validasi button dan edittext
                            if (bitmap == null) {
                                Toast.makeText(getContext(), "Please select image first", Toast.LENGTH_SHORT).show();
                            } else if (et_report.getText().toString().isEmpty()) {
                                Toast.makeText(getContext(), "Please fill report", Toast.LENGTH_SHORT).show();
                                et_report.setError("Please fill report");
                            } else {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] bytes = byteArrayOutputStream.toByteArray();
                                image = Base64.encodeToString(bytes, Base64.DEFAULT);
                                pd.setMessage("Please wait...");
                                pd.show();
                                pd.setCancelable(false);
                                pd.setCanceledOnTouchOutside(false);

                                reportUser(userid, user_id, et_report.getText().toString(), image, et_title.getText().toString());

                            }

                        });





                        reportForm.show();
                        break;
                }

            }) ;

            builder.show();

        });




        // create tablayout
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_layout));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_love2));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                    getRecipe(user_id, 1);
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getRecipe(user_id, 1);
                        }
                    });

                } else if(tabLayout.getSelectedTabPosition() == 1)  {
                    getLikeRecipe(user_id);
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            getLikeRecipe(user_id);
                        }
                    });
                   ;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecipe(user_id, 1);
            }
        });


        btnBack.setOnClickListener(view1 -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });


        // COUNT TOTAL POST
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.getMyRecipe(user_id, 1).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.body().size() > 0) {
                    if(Math.abs(response.body().size()) > 1000){
                        tv_post.setText(Math.abs(response.body().size())/1000 + "K");
                    } else if(Math.abs(response.body().size()) > 1001) {
                        tv_post.setText(Math.abs(response.body().size())/1001 + "K+");
                    }
                    else if(Math.abs(response.body().size()) > 1000000){
                        tv_post.setText(Math.abs(response.body().size())/1000000 + "M");
                    } else if(Math.abs(response.body().size()) > 1000001){
                        tv_post.setText(Math.abs(response.body().size())/1000001 + "M+");
                    }

                    else if (Math.abs(response.body().size()) > 1000000000){
                        tv_post.setText(Math.abs(response.body().size())/1000000000 + "B");
                    } else if (Math.abs(response.body().size()) > 1000000001){
                        tv_post.setText(Math.abs(response.body().size())/1000000001 + "B+");
                    }
                    else {
                        tv_post.setText(Math.abs(response.body().size()) + "");
                    }
                } else {
                    tv_post.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

            }
        });

        // COUNT TOTAL FOLLOWERS
        InterfaceProfile interfaceProfile =  DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getAllFollowers(user_id).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0 ) {

                    if(Math.abs(response.body().size()) > 1000){
                        tv_followers.setText(Math.abs(response.body().size())/1000 + "K");
                    } else if(Math.abs(response.body().size()) > 1000000){
                        tv_followers.setText(Math.abs(response.body().size())/1000000 + "M");
                    } else if (Math.abs(response.body().size()) > 1000000000){
                        tv_followers.setText(Math.abs(response.body().size())/1000000000 + "B");
                    } else {
                        tv_followers.setText(Math.abs(response.body().size()) + "");
                    }

                } else {
                    tv_followers.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {

            }
        });

        // COUNT FOLLOWING
        interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getAllFollowing(user_id).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0 ) {
                    if(Math.abs(response.body().size()) > 1000){
                        tv_following.setText(Math.abs(response.body().size())/1000 + "K");
                    } else if(Math.abs(response.body().size()) > 1000000){
                        tv_following.setText(Math.abs(response.body().size())/1000000 + "M");
                    } else if (Math.abs(response.body().size()) > 1000000000){
                        tv_following.setText(Math.abs(response.body().size())/1000000000 + "B");
                    } else {
                        tv_following.setText(Math.abs(response.body().size()) + "");
                    }

                } else {
                    tv_following.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {

            }
        });


        // CHECK IF USER WAS FOLLOWED THAN SHOW UNFOLLOW BUTTON
       InterfaceProfile interfaceProfile2 = DataApi.getClient().create(InterfaceProfile.class);
       interfaceProfile2.checkFollowing(userid, user_id).enqueue(new Callback<List<ProfileModel>>() {
           @Override
           public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
               if (response.body().size() > 0) {
                   btn_unfollow.setVisibility(View.VISIBLE);
                   btn_follow.setVisibility(View.GONE);
               }
           }

           @Override
           public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
               Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();


           }
       });

        // ACTION BUTTON UNFOLLOW
        btn_unfollow.setOnClickListener(view1 ->  {

            InterfaceProfile interfaceProfile1 = DataApi.getClient().create(InterfaceProfile.class);
            interfaceProfile1.unfollAccount(userid, user_id).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.body().getSuccess().equals("1")) {
                        btn_follow.setVisibility(View.VISIBLE);
                        btn_unfollow.setVisibility(View.GONE);
                        InterfaceProfile interfaceProfile3 = DataApi.getClient().create(InterfaceProfile.class);
                        interfaceProfile3.getAllFollowers(user_id).enqueue(new Callback<List<ProfileModel>>() {
                            @Override
                            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                                if (response.body().size() > 0) {
                                    if(Math.abs(response.body().size()) > 1000){
                                        tv_followers.setText(Math.abs(response.body().size())/1000 + "K");
                                    } else if(Math.abs(response.body().size()) > 1001) {
                                        tv_followers.setText(Math.abs(response.body().size())/1001 + "K+");
                                    }
                                    else if(Math.abs(response.body().size()) > 1000000){
                                        tv_followers.setText(Math.abs(response.body().size())/1000000 + "M");
                                    } else if(Math.abs(response.body().size()) > 1000001){
                                        tv_followers.setText(Math.abs(response.body().size())/1000001 + "M+");
                                    }

                                    else if (Math.abs(response.body().size()) > 1000000000){
                                        tv_followers.setText(Math.abs(response.body().size())/1000000000 + "B");
                                    } else if (Math.abs(response.body().size()) > 1000000001){
                                        tv_followers.setText(Math.abs(response.body().size())/1000000001 + "B+");
                                    }
                                    else {
                                        tv_followers.setText(Math.abs(response.body().size()) + "");
                                    }

                                } else {
                                    tv_followers.setText("0");
                                }
                            }

                            @Override
                            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();


                            }
                        });

                    } else {
                        Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();


                }
            });

        });

        //  button follow di klik

        btn_follow.setOnClickListener(view1 -> {
            InterfaceProfile interfaceProfile1 = DataApi.getClient().create(InterfaceProfile.class);
            interfaceProfile1.followAccount(userid, user_id).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.body().getSuccess().equals("1")) {
                        btn_unfollow.setVisibility(View.VISIBLE);
                        btn_follow.setVisibility(View.GONE);

                        InterfaceProfile interfaceProfile3 = DataApi.getClient().create(InterfaceProfile.class);
                        interfaceProfile3.getAllFollowers(user_id).enqueue(new Callback<List<ProfileModel>>() {
                            @Override
                            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                                if (response.body().size() > 0) {
                                    if(Math.abs(response.body().size()) > 1000){
                                        tv_followers.setText(Math.abs(response.body().size())/1000 + "K");
                                    } else if(Math.abs(response.body().size()) > 1001) {
                                        tv_followers.setText(Math.abs(response.body().size())/1001 + "K+");
                                    }
                                    else if(Math.abs(response.body().size()) > 1000000){
                                        tv_followers.setText(Math.abs(response.body().size())/1000000 + "M");
                                    } else if(Math.abs(response.body().size()) > 1000001){
                                        tv_followers.setText(Math.abs(response.body().size())/1000001 + "M+");
                                    }

                                    else if (Math.abs(response.body().size()) > 1000000000){
                                        tv_followers.setText(Math.abs(response.body().size())/1000000000 + "B");
                                    } else if (Math.abs(response.body().size()) > 1000000001){
                                        tv_followers.setText(Math.abs(response.body().size())/1000000001 + "B+");
                                    }
                                    else {
                                        tv_followers.setText(Math.abs(response.body().size()) + "");
                                    }
                                } else {
                                    tv_followers.setText("0");
                                }
                            }

                            @Override
                            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();


                            }
                        });

                    } else {
                        Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

                }
            });
        });

        // check followers than set button text follow back if user not followback account
        interfaceProfile.checkFollowers(userid, user_id).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0) {
                    btn_follow.setText("Follow back");

                } else {
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {

            }
        });

        // ketik admin yang melihat maka akan di arahkan ke fragment admin
        // namun ketika user biasa maka akan diarahkan sebagai user biasa

        if (getArguments().getString("admin")!= null) {

            // BTN SHOW QOCDODE ACCOUNT
            btnQrCode.setOnClickListener(view1 ->{
                Fragment fragment = new AccountQrcode();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                bundle.putString("admin", "admin");
                fragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_admin, fragment);
                ft.addToBackStack(null);
                ft.commit();
            });





        } else {

            // BTN SHOW QOCDODE ACCOUNT
            btnQrCode.setOnClickListener(view1 ->{
                Fragment fragment = new AccountQrcode();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                fragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            });


            // infor user seperti jumlah followers dan following
            lr_info.setOnClickListener(view1 -> {
                Fragment fragment = new FollowersFollowingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                bundle.putString("username", tv_username.getText().toString());
                fragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            });

        }



        return view;
    }


    //get profile
    private void getProfile(String user_id) {
        DataApi.getClient().create(InterfaceProfile.class).getProfile(user_id).enqueue(new retrofit2.Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, retrofit2.Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                for (int i = 0; i < profileModelList.size(); i++) {
                    profileModel = profileModelList.get(i);
                    tv_username.setText(profileModel.getUsername());
                    tv_email.setText(profileModel.getEmail());

                    // show ic verified if account is verified
                    if (profileModelList.get(0).getVerified().equals("1")) {
                        icVerified.setVisibility(View.VISIBLE);
                    } else {
                        icVerified.setVisibility(View.GONE);
                    }
                    tv_biography.setText(profileModel.getBiography());
                    if (profileModel.getBiography().isEmpty()) {
                        tv_biography.setVisibility(View.GONE);
                    }
                    tv_date.setText(profileModel.getDate());
                    tv_time.setText(profileModel.getTime());
                    Glide.with(getContext())
                            .load(profileModel.getPhoto_profile())
                            .thumbnail(0.5f)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                            .fitCenter()
                            .centerCrop()
                            .placeholder(R.drawable.template_img)
                            .override(1024, 768)
                            .into(iv_profile);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(true);
                getProfile(user_id);
            }
        });

    }

    private void getRecipe(String user_id, Integer status) {

        DataApi.getClient().create(InterfaceRecipe.class).getMyRecipe(user_id, status).enqueue(new retrofit2.Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, retrofit2.Response<List<RecipeModel>> response) {

                if (response.body().size() > 0 ) {
                    recipeModelList = response.body();
                    myRecipeAdapter = new MyRecipeAdapter(getContext(), recipeModelList);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                    rv_recipe.setLayoutManager(gridLayoutManager);
                    rv_recipe.setAdapter(myRecipeAdapter);
                    rv_recipe.setHasFixedSize(true);
                    myRecipeAdapter.notifyDataSetChanged();
                    tv_notfound.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    rv_recipe.setVisibility(View.VISIBLE);
                    rv_recipe.setItemViewType((type, position) -> {
                        switch (type) {


                            default:
                            case ShimmerRecyclerView.LAYOUT_GRID:
                                return position == 0 || position % 2 == 0
                                        ? R.layout.template_my_recipe
                                        : R.layout.template_my_recipe;
                        }
                    });

                    rv_recipe.showShimmer();     // to start showing shimmer
                    final Handler handler = new Handler();
                    handler.postDelayed((Runnable) () -> {
                        rv_recipe.hideShimmer();
                    }, 1200);


                    // set agar item dapat di click
                    myRecipeAdapter.setOnRecipeListener(ShowProfileFragment.this);
                } else {
                    tv_notfound.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    rv_recipe.setVisibility(View.GONE);
                }




            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                getRecipe(user_id, 1);

            }
        });
    }

    private void getLikeRecipe(String user_id) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getMyLikeRecipe(user_id);
        call.enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {

                if (response.body().size() > 0) {
                    recipeModelList = response.body();
                    myRecipeAdapter = new MyRecipeAdapter(getContext(), recipeModelList);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                    rv_recipe.setLayoutManager(gridLayoutManager);
                    rv_recipe.setAdapter(myRecipeAdapter);
                    rv_recipe.setHasFixedSize(true);
                    swipeRefreshLayout.setRefreshing(false);
                    tv_notfound.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    rv_recipe.setVisibility(View.VISIBLE);
                    rv_recipe.showShimmer();     // to start showing shimmer
                    final Handler handler = new Handler();
                    handler.postDelayed((Runnable) () -> {
                        rv_recipe.hideShimmer();
                    }, 1000);

                    // click item
                    myRecipeAdapter.setOnRecipeListener(ShowProfileFragment.this);
                } else {
                    tv_notfound.setVisibility(View.VISIBLE);
                    rv_recipe.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }


            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                getLikeRecipe(user_id);
            }


        });
    }


    @Override
    public void onRecipeClick(View view, int position) {
        if (getArguments().getString("admin") != null) {
            RecipeModel recipeModel = recipeModelList.get(position);
            switch (view.getId()) {


                case R.id.iv_recipe:
                    Fragment fragment = new DetailRecipeFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("recipe_id", recipeModel.getRecipe_id());
                    bundle.putString("user_id", recipeModel.getUser_id());
                    bundle.putString("username", recipeModel.getUsername());
                    bundle.putString("title", recipeModel.getTitle());
                    bundle.putString("description", recipeModel.getDescription());
                    bundle.putString("category", recipeModel.getCategory());
                    bundle.putString("servings", recipeModel.getServings());
                    bundle.putString("duration", recipeModel.getDuration());
                    bundle.putString("ingredients", recipeModel.getIngredients());
                    bundle.putString("steps", recipeModel.getSteps());
                    bundle.putString("upload_date", recipeModel.getUpload_date());
                    bundle.putString("upload_time", recipeModel.getUpload_time());
                    bundle.putString("image", recipeModel.getImage());
                    bundle.putString("status", recipeModel.getStatus());
                    bundle.putString("ratings", recipeModel.getRatings());
                    bundle.putString("likes", recipeModel.getLikes());
                    bundle.putString("photo_profile", recipeModel.getPhoto_profile());
                    bundle.putString("email", recipeModel.getEmail());
                    bundle.putString("notes", recipeModel.getNote());
                    bundle.putString("admin", getArguments().getString("admin"));
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_admin, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;
            }

        }
        else{

            switch (view.getId()) {



                case R.id.iv_recipe:
                    Fragment fragment = new DetailRecipeFragment();
                    RecipeModel recipeModels = recipeModelList.get(position);


                    Bundle bundle = new Bundle();
                    bundle.putString("recipe_id", recipeModels.getRecipe_id());
                    bundle.putString("user_id", recipeModels.getUser_id());
                    bundle.putString("username", recipeModels.getUsername());
                    bundle.putString("title", recipeModels.getTitle());
                    bundle.putString("description", recipeModels.getDescription());
                    bundle.putString("category", recipeModels.getCategory());
                    bundle.putString("servings", recipeModels.getServings());
                    bundle.putString("duration", recipeModels.getDuration());
                    bundle.putString("ingredients", recipeModels.getIngredients());
                    bundle.putString("steps", recipeModels.getSteps());
                    bundle.putString("upload_date", recipeModels.getUpload_date());
                    bundle.putString("upload_time", recipeModels.getUpload_time());
                    bundle.putString("image", recipeModels.getImage());
                    bundle.putString("status", recipeModels.getStatus());
                    bundle.putString("ratings", recipeModels.getRatings());
                    bundle.putString("likes", recipeModels.getLikes());
                    bundle.putString("photo_profile", recipeModels.getPhoto_profile());
                    bundle.putString("email", recipeModels.getEmail());
                    bundle.putString("notes", recipeModels.getNote());
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;
            }


        }
    }

    // method for send data report user to server
    private void reportUser(String user_id, String user_id_report, String report, String image, String title) {
        InterfaceProfile ip = DataApi.getClient().create(InterfaceProfile.class);
        ip.reportUser(user_id, user_id_report, report, image, title).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                RecipeModel recipeModel = response.body();
                if (recipeModel.getStatus().equals("1")) {
                    Toast.makeText(getContext(), "Success reported user", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    reportForm.dismiss();

                }
                else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Snackbar.make(getView(), "Error no connection", Snackbar.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t.getCause());
                pd.dismiss();

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==  RESULT_OK && requestCode == TAG_GALLERY && data != null && data.getData() != null){

            Uri uri_path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri_path);
                ivReport.setImageBitmap(bitmap);
                rlImagePicker.setVisibility(View.VISIBLE);
                lrImagePicker.setVisibility(View.GONE);

                Snackbar.make(getView(), "Successfully load image", Snackbar.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Snackbar.make(getView(), "Failed to load image", Snackbar.LENGTH_LONG).show();

            }catch (IOException e){
                Snackbar.make(getView(), "Error no connection", Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    // set shimmer
    private void setShimmer() {
        rv_recipe.setItemViewType((type, position) -> {
            switch (type) {


                default:
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_my_recipe
                            : R.layout.template_my_recipe;
            }
        });

        rv_recipe.showShimmer();     // to start showing shimmer

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
        setShimmer();
        checkConnection();


        super.onResume();
    }

    @Override
    public void onPause() {
        rv_recipe.hideShimmer();
        super.onPause();
    }
}