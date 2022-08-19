package com.example.recipe_app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportProblemFragment extends Fragment {
    ImageButton btnBack, btnSend;
    ImageView imgReport;
    TextView tv_select_image;
    LottieAnimationView addAnim;
    EditText edt_report;
    String userid, image;

    private final int TAG_GALLERY = 2222;
    Bitmap bitmap;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_report_problem, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);
        progressDialog = new ProgressDialog(getActivity());




        btnBack = view.findViewById(R.id.btn_back);
        btnSend = view.findViewById(R.id.btn_send);
        imgReport = view.findViewById(R.id.img_report);
        tv_select_image = view.findViewById(R.id.tv_select_image);
        addAnim = view.findViewById(R.id.add_image);
        edt_report = view.findViewById(R.id.edt_report);

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });


        imgReport.setOnClickListener(view1 -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, TAG_GALLERY);

            }
        });

        // validasi field tidak boleh kosong
        btnSend.setOnClickListener(view1 -> {

            if (bitmap == null) {
                Snackbar.make(getView(), "Please select image", Snackbar.LENGTH_SHORT).show();
            } else if (edt_report.getText().toString().isEmpty()) {
                Snackbar.make(getView(), "Please fill in the field", Snackbar.LENGTH_SHORT).show();
            } else {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                image = Base64.encodeToString(bytes, Base64.DEFAULT);


                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                // memanggil method addReport
                sendReport();
            }

        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==  RESULT_OK && requestCode == TAG_GALLERY && data != null && data.getData() != null){

            Uri uri_path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri_path);
                imgReport.setImageBitmap(bitmap);
                addAnim.setVisibility(View.GONE);
                tv_select_image.setVisibility(View.GONE);
                progressDialog.dismiss();
                Snackbar.make(getView(), "Successfully load image", Snackbar.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Snackbar.make(getView(), "Failed to load image", Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }catch (IOException e){
                Snackbar.make(getView(), "Error no connection", Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    // Method untuk mengirim report bug ke servr
    private void sendReport() {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.reportBug(userid, edt_report.getText().toString(), image).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                if (response.body().getStatus().equals("success")) {
                    progressDialog.dismiss();
                    Snackbar.make(getView(), "Successfully send report", Snackbar.LENGTH_LONG).show();
                    edt_report.setText("");
                    addAnim.setVisibility(View.VISIBLE);
                    tv_select_image.setVisibility(View.VISIBLE);
                    imgReport.setBackground(getContext().getResources().getDrawable(R.color.white));

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack();
                } else {
                    progressDialog.dismiss();
                    Snackbar.make(getView(), "Failed to send report", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Snackbar.make(getView(), "Error no connection", Snackbar.LENGTH_LONG).show();

            }
        });
    }
}