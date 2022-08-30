package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.recipe_app.R;
import com.github.chrisbanes.photoview.PhotoView;

public class FullScreenImageReport extends Fragment {
    PhotoView img_report;
    ImageButton btn_back;
    String image;



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
                .placeholder(R.drawable.template_img)
                .into(img_report);

        btn_back.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });

        return root;
    }
}