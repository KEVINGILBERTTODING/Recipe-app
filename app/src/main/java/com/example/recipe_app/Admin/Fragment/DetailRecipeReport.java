package com.example.recipe_app.Admin.Fragment;

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
import com.example.recipe_app.R;


public class DetailRecipeReport extends Fragment {

    TextView tv_username, tv_date, tv_title, tv_report, tv_recipe_name;
    ImageView img_profile, imgReport;
    ImageButton btnBack, btnDelete;
    Button btnAccept, btnReject, btnUnBlock;

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
        tv_recipe_name = root.findViewById(R.id.tv_recipe_name);
        img_profile = root.findViewById(R.id.img_profile);
        btnBack = root.findViewById(R.id.btn_back);
        btnDelete = root.findViewById(R.id.btn_delete);
        btnAccept = root.findViewById(R.id.btn_accept);
        btnReject = root.findViewById(R.id.btn_rejected);
        btnUnBlock = root.findViewById(R.id.btn_unblock);
        imgReport = root.findViewById(R.id.iv_report);

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
        tv_recipe_name.setText(recipeId);


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





        return root;


    }
}