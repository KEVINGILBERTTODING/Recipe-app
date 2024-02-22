package com.example.recipe_app.ui.user.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static com.example.recipe_app.ui.activities.auth.LoginActivity.my_shared_preferences;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.data.remote.DataApi;
import com.example.recipe_app.data.remote.InterfaceProfile;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportProblemFragment extends Fragment {
    ImageButton btnBack;
    ImageView iv_report;
    EditText edt_report, edt_title;
    String userid, image;
    Button btnSend;
    LinearLayout lr_image_picker;
    RelativeLayout rl_image_picker;

    private final int TAG_GALLERY = 2222;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    ConnectivityManager conMgr;
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
        iv_report = view.findViewById(R.id.iv_report);
        edt_report = view.findViewById(R.id.edt_report);
        lr_image_picker = view.findViewById(R.id.lr_image_picker);
        edt_title = view.findViewById(R.id.edt_title);
        rl_image_picker = view.findViewById(R.id.rl_image_picker);

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });


        lr_image_picker.setOnClickListener(view1 -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, TAG_GALLERY);

            }
        });

        rl_image_picker.setOnClickListener(view1 -> {
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
            } else if (edt_report.getText().toString().isEmpty() || edt_title.getText().toString().isEmpty()) {
                edt_report.setError("Please fill this field");
                edt_title.setError("Please fill this field");
                Snackbar.make(getView(), "Please fill this field", Snackbar.LENGTH_SHORT).show();
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
                iv_report.setImageBitmap(bitmap);
                progressDialog.dismiss();
                lr_image_picker.setVisibility(View.GONE);
                rl_image_picker.setVisibility(View.VISIBLE);
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
        interfaceProfile.reportBug(userid, edt_report.getText().toString(), edt_title.getText().toString(), image).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                if (response.body().getStatus().equals("success")) {
                    progressDialog.dismiss();
                    Snackbar.make(getView(), "Successfully send report", Snackbar.LENGTH_LONG).show();
                    edt_report.setText("");

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