package com.example.recipe_app.ui.Admin.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.ui.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.ui.Admin.Model.BugReportModel;
import com.example.recipe_app.R;
import com.example.recipe_app.data.remote.DataApi;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBugReportFragment extends Fragment {
    TextView tv_username, tv_date, tv_title, tv_report, tv_time;
    ImageView img_profile, imgReport, icVerified;
    ImageButton btnBack, btnDelete;
    Button btnAccept, btnReject, btnDone;
    ConnectivityManager conMgr;


    String userId, username, recipeId, status, time, date, report, image, imgProfile, email,
            title, reportId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail_bug_report, container, false);

        tv_username = root.findViewById(R.id.tv_username);
        tv_date = root.findViewById(R.id.tv_date);
        tv_title = root.findViewById(R.id.tv_title);
        tv_report = root.findViewById(R.id.tv_report);
        img_profile = root.findViewById(R.id.img_profile);
        btnBack = root.findViewById(R.id.btn_back);
        btnDelete = root.findViewById(R.id.btn_delete);
        btnAccept = root.findViewById(R.id.btn_accept);
        btnReject = root.findViewById(R.id.btn_rejected);
        btnDone = root.findViewById(R.id.btn_done);
        imgReport = root.findViewById(R.id.iv_report);
        tv_time = root.findViewById(R.id.tv_time);
        icVerified = root.findViewById(R.id.img_verified);




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
        tv_time.setText(time);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        // Check if user is verified than show verified badge
        if (getArguments().getInt("verified") == 1) {
            icVerified.setVisibility(View.VISIBLE);
        } else {
            icVerified.setVisibility(View.GONE);
        }


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

        if (status.equals("1")) {

        } else if(status.equals("2")) {
            btnAccept.setVisibility(View.GONE);
            btnDone.setVisibility(View.VISIBLE);
        } else if(status.equals("3")) {
            btnAccept.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.GONE);
            btnAccept.setText("Roll Back");
        } else{
            btnAccept.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        }

        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete report");
            builder.setMessage("Are you sure you want to delete this report?");
            builder.setPositiveButton("Yes", ((dialogInterface, i) -> {
                InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
                interfaceAdmin.deleteBugReport(reportId).enqueue(new Callback<BugReportModel>() {
                    @Override
                    public void onResponse(Call<BugReportModel> call, Response<BugReportModel> response) {
                        BugReportModel bugReportModel = response.body();
                        if (bugReportModel.getStatus().equals("1")) {
                            Toasty.success(getContext(), "Delete report successfully", Toasty.LENGTH_SHORT).show();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_admin, new ReportBugFragment());
                            ft.commit();
                        } else{

                            Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BugReportModel> call, Throwable t) {
                        Toasty.error(getContext(), "Error no connection", Toasty.LENGTH_SHORT).show();

                    }
                });

                dialogInterface.dismiss();

            }));
            builder.setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();


            });

            builder.show();
        });

        // btn accept is clicked
        btnAccept.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Accepted report");
            builder.setMessage("Are you sure you want to accept this report?");
            builder.setPositiveButton("yes", (dialogInterface, i) -> {
                actionBugReport(reportId, "2");
                if (status.equals("3")){
                    Toasty.success(getContext(), "Roll back successfully", Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.success(getContext(), "Accepted report successfully", Toasty.LENGTH_SHORT).show();
                }
                Toasty.success(getContext(), "Report accepted", Toasty.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }); builder.setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();

            });




            builder.show();
        });


        btnDone.setOnClickListener(view1 -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle("Solved Bug");
            builder2.setMessage("Are you sure you want to finish this bug?");
            builder2.setPositiveButton("Yes", (dialogInterface, i) -> {
                actionBugReport(reportId, "3");
                Toasty.success(getContext(), "Bug Solved!", Toasty.LENGTH_SHORT).show();
                dialogInterface.dismiss();

            }); builder2.setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });

            builder2.show();

        });

        btnReject.setOnClickListener(view1 -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle("Reject report");
            builder2.setMessage("Are you sure you want to reject this report?");
            builder2.setPositiveButton("Yes", (dialogInterface, i) -> {
                actionBugReport(reportId, "0");
                Toasty.success(getContext(), "Report rejected", Toasty.LENGTH_SHORT).show();
                dialogInterface.dismiss();

            }); builder2.setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });

            builder2.show();

        });


        return root;
    }

    // action bug report
    private void actionBugReport(String report_id, String status) {
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.actionBugReport(report_id, status).enqueue(new Callback<BugReportModel>() {
            @Override
            public void onResponse(Call<BugReportModel> call, Response<BugReportModel> response) {
                BugReportModel bugReportModel = response.body();
                if (bugReportModel.getStatus().equals("1")) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_admin, new ReportBugFragment());
                    ft.commit();

                } else {
                    Toasty.error(getContext(), "Failed load report", Toasty.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BugReportModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

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
        super.onResume();
    }
}