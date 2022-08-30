package com.example.recipe_app.Admin.Fragment;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.UserReportModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserReport extends Fragment {
    ImageButton btnDelete, btnBack;
    Button btnAccept, btnReject;
    ImageView imgReport, img_profile;
    TextView tv_report, tv_title, tv_date, tv_username1, tv_time, tv_username2;
    private boolean zoomOut =  false;

    String image, pp1, pp2, username1, username2, title, report, email1, email2,
            date, time, user_id1, user_id2, report_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail_user_report, container, false);

        btnDelete = root.findViewById(R.id.btn_delete);
        btnBack = root.findViewById(R.id.btn_back);
        btnAccept = root.findViewById(R.id.btn_accept);
        btnReject = root.findViewById(R.id.btn_rejected);
        imgReport = root.findViewById(R.id.iv_report);
        tv_report = root.findViewById(R.id.tv_report);
        tv_title = root.findViewById(R.id.tv_title);
        tv_date = root.findViewById(R.id.tv_date);
        tv_username1 = root.findViewById(R.id.tv_username1);
        img_profile = root.findViewById(R.id.img_profile);
        tv_time = root.findViewById(R.id.tv_time);
        tv_username2 = root.findViewById(R.id.tv_username2);



        // get data from bundle
        image   = getArguments().getString("image");
        username1 = getArguments().getString("username1");
        user_id1 = getArguments().getString("user_id1");
        user_id2 = getArguments().getString("user_id2");
        username2 = getArguments().getString("username2");
        title = getArguments().getString("title");
        report = getArguments().getString("report");
        email1 = getArguments().getString("email1");
        email2 = getArguments().getString("email2");
        date = getArguments().getString("date");
        time = getArguments().getString("time");
        pp1 = getArguments().getString("photo_profile1");
        pp2 = getArguments().getString("photo_profile2");
        report_id = getArguments().getString("report_id");

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

       // load tv_username1
        tv_username1.setText(username1);

        // load photo profile
        Glide.with(getContext())
                .load(pp1)
                .override(300, 300)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(img_profile);

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

       // set title
        tv_title.setText(title);

        // set report content
        tv_report.setText(report);

        // set date
        tv_date.setText(date);

        // set time
        tv_time.setText(time);

        // set username report
        tv_username2.setText(username2);

        btnBack.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });

        // btn delete is clicked
        btnDelete.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Report");
            builder.setMessage("Are you sure you want to delete this report?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
                interfaceAdmin.deleteUserReport(report_id).enqueue(new Callback<UserReportModel>() {
                    @Override
                    public void onResponse(Call<UserReportModel> call, Response<UserReportModel> response) {
                        UserReportModel userReportModel = response.body();
                        if (userReportModel.getStatus().equals("1")) {
                            Toast.makeText(getContext(),
                                    "Report deleted successfully", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_admin, new ReportUserFragment());
                            ft.commit();
                        } else  {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserReportModel> call, Throwable t) {
                        Snackbar.make(getView(), "Error no connection", Snackbar.LENGTH_SHORT).show();

                    }
                });
            }).setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            }).show();
        });




        return root;
    }

}