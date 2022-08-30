package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.recipe_app.R;

public class DetailUserReport extends Fragment {
    ImageButton btnDelete, btnBack;
    Button btnAccept, btnReject;
    ImageView imgReport, img_profile;
    TextView tv_report, tv_title, tv_date, tv_username1;
    private boolean zoomOut =  false;

    String image, pp1, pp2, username1, username2, title, report, email1, email2,
            date, time;


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




        image   = getArguments().getString("image");

        Glide.with(getContext())
                .load(image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .fitCenter()
                .centerCrop()
                .into(imgReport);

        username1 = getArguments().getString("username1");
        tv_username1.setText(username1);

        pp1 =  getArguments().getString("photo_profile1");
        Glide.with(getContext())
                .load(pp1)
                .override(300, 300)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(img_profile);

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




//
//
//        bundle.putString("user_id1", userReportModelList.get(getAdapterPosition()).getUser_id());
//        bundle.putString("user_id2", userReportModelList.get(getAdapterPosition()).getUser_id_report());
//        bundle.putString("username1", userReportModelList.get(getAdapterPosition()).getUsername1());
//        bundle.putString("username2", userReportModelList.get(getAdapterPosition()).getUsername2());
//        bundle.putString("photo_profile1", userReportModelList.get(getAdapterPosition()).getPhoto_profile1());
//        bundle.putString("photo_profile2", userReportModelList.get(getAdapterPosition()).getPhoto_profile2());
//        bundle.putString("image", userReportModelList.get(getAdapterPosition()).getImage());
//        bundle.putString("report", userReportModelList.get(getAdapterPosition()).getReport());
//        bundle.putString("title", userReportModelList.get(getAdapterPosition()).getTitle());
//        bundle.putString("date", userReportModelList.get(getAdapterPosition()).getDate());
//        bundle.putString("time", userReportModelList.get(getAdapterPosition()).getTime());
//        bundle.putString("email1", userReportModelList.get(getAdapterPosition()).getEmail1());
//        bundle.putString("email2", userReportModelList.get(getAdapterPosition()).getEmail2());



        return root;
    }
}