package com.example.recipe_app.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipe_app.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrcodeFragment extends Fragment {
    ImageView iv_qrcode;
    ImageButton btnBack;
    TextView tv_recipe_id;

    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String recipe_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);

        iv_qrcode = view.findViewById(R.id.iv_qrcode);
        btnBack = view.findViewById(R.id.btnBack);
        tv_recipe_id = view.findViewById(R.id.tv_recipeid);


        recipe_id = getArguments().getString("recipe_id");

        tv_recipe_id.setText(recipe_id);

        qrgEncoder = new QRGEncoder(recipe_id, null, QRGContents.Type.TEXT, 500);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            iv_qrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setScreenBright();

        return view;
    }

    private void setScreenBright() {

    }

    private void setFullBrightness() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}