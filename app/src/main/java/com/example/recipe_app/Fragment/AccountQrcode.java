package com.example.recipe_app.Fragment;

import android.graphics.Bitmap;
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
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountQrcode extends Fragment {

    ImageView iv_qrcode, iv_user;
    ImageButton btnBack;
    TextView tv_username;
    Button btnScan;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_accunt_qrcode, container, false);

        iv_qrcode = root.findViewById(R.id.iv_qrcode);
        btnBack = root.findViewById(R.id.btn_back);
        tv_username = root.findViewById(R.id.tv_username);
        btnScan = root.findViewById(R.id.btn_scan);
        iv_user = root.findViewById(R.id.iv_user);


        // btn back

        btnBack.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });

        // create qrcode
        qrgEncoder = new QRGEncoder(getArguments().getString("user_id"), null, QRGContents.Type.TEXT, 500);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            iv_qrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get account info
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getProfile(getArguments().getString("user_id")).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0) {
                    tv_username.setText(response.body().get(0).getUsername());
                    Glide.with(getContext())
                            .load(response.body().get(0).getPhoto_profile())
                            .override(300, 300)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .dontAnimate()
                            .into(iv_user);

                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {

            }
        });

        if (getArguments().getString("admin") != null) {
            btnScan.setOnClickListener(view -> {
                Fragment fragment  = new ScannerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", "user_id");
                bundle.putString("admin", "admin");
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_admin, fragment);
                ft.addToBackStack(null);
                ft.commit();
            });

        } else{
            btnScan.setOnClickListener(view -> {
                Fragment fragment  = new ScannerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", "user_id");
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            });

        }





        return root;
    }
}