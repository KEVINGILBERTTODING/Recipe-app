package com.example.recipe_app.Admin.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.recipe_app.R;
import com.github.chrisbanes.photoview.PhotoView;

import es.dmoral.toasty.Toasty;

public class FullScreenImageReport extends Fragment {
    PhotoView img_report;
    ImageButton btn_back;
    String image;
    private ConnectivityManager conMgr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_full_screen_image_report, container, false);
        img_report = root.findViewById(R.id.img_report);
        btn_back = root.findViewById(R.id.btnBack);

        // get data from bundle
        image = getArguments().getString("image");

        // load image using GLIDE
        Glide.with(getContext())
                .load(image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .into(img_report);

        btn_back.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });

        return root;
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