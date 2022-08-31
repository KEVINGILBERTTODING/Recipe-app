package com.example.recipe_app.Admin.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.RecipeReportmodel;
import com.example.recipe_app.Admin.Model.UserReportModel;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailRecipeReport extends Fragment {

    TextView tv_username, tv_date, tv_title, tv_report;
    ImageView img_profile, imgReport;
    ImageButton btnBack, btnDelete;
    Button btnAccept, btnReject, btnUnBlock, btnShow;

    String userId, username, recipeId, status, time, date, report, image, imgProfile, email,
            title, reportId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail_recipe_report, container, false);
        tv_username = root.findViewById(R.id.tv_username);
        tv_date = root.findViewById(R.id.tv_date);
        tv_title = root.findViewById(R.id.tv_title);
        tv_report = root.findViewById(R.id.tv_report);
        img_profile = root.findViewById(R.id.img_profile);
        btnBack = root.findViewById(R.id.btn_back);
        btnDelete = root.findViewById(R.id.btn_delete);
        btnAccept = root.findViewById(R.id.btn_accept);
        btnReject = root.findViewById(R.id.btn_rejected);
        btnUnBlock = root.findViewById(R.id.btn_unblock);
        imgReport = root.findViewById(R.id.iv_report);
        btnShow = root.findViewById(R.id.btn_show);

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });

        // get data from bundle
        reportId    = getArguments().getString("report_id");
        recipeId    =    getArguments().getString("recipe_id");
        userId      =   getArguments().getString("user_id");
        title       =   getArguments().getString("title");
        report      =  getArguments().getString("report");
        image       =  getArguments().getString("image");
        date        = getArguments().getString("date");
        time        =           getArguments().getString("time");
        status      =         getArguments().getString("status");
        username    =         getArguments().getString("username");
        imgProfile  =         getArguments().getString("photo_profile");
        email       =        getArguments().getString("email");

        tv_username.setText(username);
        tv_date.setText(date);
        tv_title.setText(title);
        tv_report.setText(report);


        // load photo profile
        Glide.with(getContext())
                .load(imgProfile)
                .override(300, 300)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(img_profile);

        // load image report
        Glide.with(getContext())
                .load(image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .fitCenter()
                .centerCrop()
                .into(imgReport);

        // if image report is clicked navigate to full screen image
        imgReport.setOnClickListener(view -> {
            Fragment fragment = new FullScreenImageReport();
            Bundle bundle = new Bundle();
            bundle.putString("image", image);
            fragment.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, fragment);
            ft.addToBackStack(null);
            ft.commit();

        });

        // button show clicked
        btnShow.setOnClickListener(view -> {
            InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
            interfaceRecipe.getRecipe(recipeId).enqueue(new Callback<List<RecipeModel>>() {
                @Override
                public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                    List<RecipeModel> recipeModelList = response.body();

                    if (recipeModelList.size() != 0) {

                        // get position of item clicked
                        Fragment fragment = new DetailRecipeFragment();

                        // Send data to detailrecipe fragment

                        Bundle bundle = new Bundle();
                        bundle.putString("recipe_id", recipeModelList.get(0).getRecipe_id());
                        bundle.putString("user_id", recipeModelList.get(0).getUser_id());
                        bundle.putString("username", recipeModelList.get(0).getUsername());
                        bundle.putString("title", recipeModelList.get(0).getTitle());
                        bundle.putString("description", recipeModelList.get(0).getDescription());
                        bundle.putString("category", recipeModelList.get(0).getCategory());
                        bundle.putString("servings", recipeModelList.get(0).getServings());
                        bundle.putString("duration", recipeModelList.get(0).getDuration());
                        bundle.putString("ingredients", recipeModelList.get(0).getIngredients());
                        bundle.putString("steps", recipeModelList.get(0).getSteps());
                        bundle.putString("upload_date", recipeModelList.get(0).getUpload_date());
                        bundle.putString("upload_time", recipeModelList.get(0).getUpload_time());
                        bundle.putString("image", recipeModelList.get(0).getImage());
                        bundle.putString("status", recipeModelList.get(0).getStatus());
                        bundle.putString("ratings", recipeModelList.get(0).getRatings());
                        bundle.putString("likes", recipeModelList.get(0).getLikes());
                        bundle.putString("photo_profile", recipeModelList.get(0).getPhoto_profile());
                        bundle.putString("email", recipeModelList.get(0).getEmail());
                        bundle.putString("notes", recipeModelList.get(0).getNote());
                        fragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_admin, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();



                    }

                }

                @Override
                public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

                }
            });
        });


        // btn delete is clicked
        btnDelete.setOnClickListener(view -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Report");
            builder.setMessage("Are you sure you want to delete this report?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
                interfaceAdmin.deleteRecipeReport(reportId).enqueue(new Callback<RecipeReportmodel>() {
                    @Override
                    public void onResponse(Call<RecipeReportmodel> call, Response<RecipeReportmodel> response) {
                        RecipeReportmodel recipeReportmodel = response.body();
                        if (recipeReportmodel.getStatus().equals("1")) {
                            Toast.makeText(getContext(),
                                    "Report deleted successfully", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_admin, new ReportRecipeFragment());
                            ft.commit();
                        } else  {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipeReportmodel> call, Throwable t) {
                        Snackbar.make(getView(), "Error no connection", Snackbar.LENGTH_SHORT).show();
                        Log.e("INI ERRORNYAA", t.getMessage());

                    }
                });
            }).setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            }).show();
        });





        return root;


    }

    // get recipe by recipe_id
    private void getRecipe(String recipe_id) {

    }
}