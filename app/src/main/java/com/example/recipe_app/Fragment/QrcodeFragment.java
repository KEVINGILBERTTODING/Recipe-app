package com.example.recipe_app.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import es.dmoral.toasty.Toasty;

public class QrcodeFragment extends Fragment {
    ImageView iv_qrcode;
    ImageButton btnBack;
    TextView tv_recipe_name, tv_username;
    ImageView iv_recipe_image, icVerified;
    Button btnScan;

    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String recipe_id, recipe_name, username, img_recipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);

        iv_qrcode = view.findViewById(R.id.iv_qrcode);
        btnBack = view.findViewById(R.id.btn_back);
        tv_recipe_name = view.findViewById(R.id.tv_recipe_name);
        tv_username = view.findViewById(R.id.tv_username);
        iv_recipe_image = view.findViewById(R.id.iv_recipe);
        btnScan = view.findViewById(R.id.btn_scan);
        icVerified = view.findViewById(R.id.iv_verified);




        recipe_id = getArguments().getString("recipe_id");
        recipe_name = getArguments().getString("recipe_name");
        img_recipe = getArguments().getString("recipe_image");
        username = getArguments().getString("username");


        // if user is verified than show verified badge
        if (getArguments().getString("verified").equals("1")) {
            icVerified.setVisibility(View.VISIBLE);
        } else {
            icVerified.setVisibility(View.GONE);
        }



        // Load image recipe
        Glide.with(getContext())
                .load(img_recipe)
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .fitCenter()
                .centerCrop()
                .into(iv_recipe_image);

        // set recipe name

        tv_recipe_name.setText(recipe_name);
        tv_username.setText(username);

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });


        // Generate qrcode automatic by recipe_id

        qrgEncoder = new QRGEncoder(recipe_id, null, QRGContents.Type.TEXT, 500);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            iv_qrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setScreenBright();

        if (getArguments().getString("admin") != null) {
            btnScan.setOnClickListener(view1 -> {
                Fragment fragment = new ScannerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("admin", "admin");
                bundle.putString("recipe", "recipe");
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_admin, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            });
        } else {
            btnScan.setOnClickListener(view1 -> {
                Fragment fragment = new ScannerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("admin", "admin");
                bundle.putString("recipe", "recipe");
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            });
        }



        return view;
    }

    private void setScreenBright() {

    }

    private void setFullBrightness() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}