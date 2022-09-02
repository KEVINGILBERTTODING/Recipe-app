package com.example.recipe_app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.Manifest;
import android.app.Dialog;
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
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.LoginActivity;
import com.example.recipe_app.Model.AppModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {

    RelativeLayout updte_pass, updt_email, contactUs, logout, appVersion, aboutUs, addBio;
    ImageButton btnBack;
    ImageView iv_profile;
    private List<ProfileModel> profileModelList = new ArrayList<>();
    String username, userid;
    TextView tv_username, tv_email, tvPhoto, tvApply;
    private final int TAG_GALLERY = 2222;
    Bitmap bitmap;
    public static final int progress_bar_type = 0;
    ProgressDialog progressDialog;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        updte_pass = view.findViewById(R.id.update_pass);
        updt_email = view.findViewById(R.id.email);
        btnBack = view.findViewById(R.id.btnBack);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_username= view.findViewById(R.id.tv_username);
        tv_email = view.findViewById(R.id.tv_email);
        contactUs = view.findViewById(R.id.rl_contact_us);
        logout = view.findViewById(R.id.rl_logout);
        appVersion = view.findViewById(R.id.rl_version);
        aboutUs = view.findViewById(R.id.rl_about_us);
        tvPhoto = view.findViewById(R.id.tv_photo);
        tvApply = view.findViewById(R.id.tv_apply);
        addBio = view.findViewById(R.id.add_bio);

        progressDialog = new ProgressDialog(getContext());


        // memamnggil method untuk load profile
        getProfile(userid);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        // saat button appply di klik

        tvApply.setOnClickListener(view1 -> {
            if (bitmap != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure want to change your profile picture?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] imgByte = byteArrayOutputStream.toByteArray();
                    String imgString = Base64.encodeToString(imgByte, Base64.DEFAULT);
                    tvApply.setVisibility(View.GONE);

                    updateProfile(userid, imgString);



                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });

                builder.show();

            } else {
                Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
            }


        });

        //saat menu biography di klik
        addBio.setOnClickListener(View -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.layout_add_bio);
            Button btnSave = dialog.findViewById(R.id.btnSave);
            EditText edtBio = dialog.findViewById(R.id.edt_bio);
            btnSave.setOnClickListener(view1 -> {

                InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
                interfaceProfile.updateBio(userid, edtBio.getText().toString()).enqueue(new Callback<ProfileModel>() {
                    @Override
                    public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                        if (response.body().getStatus().equals("success")) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileModel> call, Throwable t) {
                        Snackbar.make(getView(), "No connection", Snackbar.LENGTH_SHORT).show();

                    }
                });

            });
            dialog.show();
        });

        // saat button contact us di klik
        contactUs.setOnClickListener(view1 -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new ReportProblemFragment());
            ft.addToBackStack(null);
            ft.commit();
        });


        // Saat menu update password di klik
        updte_pass.setOnClickListener(view1 ->  {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new UpdatePassword());
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);

        });

        // Saat menu update email di klik
        updt_email.setOnClickListener(view1 ->  {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new UpdateEmailFragment());
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);


        });


        tvPhoto.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAG_GALLERY);
        });


        iv_profile.setOnClickListener(view1 -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAG_GALLERY);

            }

        });


        // Saat menu app version di klik
        appVersion.setOnClickListener(view1->{
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.layout_app_version);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            final Button btnOk= dialog.findViewById(R.id.btnOk);
            final TextView tv_app_version = dialog.findViewById(R.id.tv_version);

            InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
            interfaceAdmin.viewAbout().enqueue(new Callback<List<AppModel>>() {
                @Override
                public void onResponse(Call<List<AppModel>> call, Response<List<AppModel>> response) {
                    List<AppModel> appModelList = response.body();
                    if (appModelList.size() > 0 ) {
                        tv_app_version.setText(appModelList.get(0).getApp_version());
                    } else {
                        Toast.makeText(getContext(), "Failed load data", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<AppModel>> call, Throwable t) {

                }
            });
            btnOk.setOnClickListener(view2 -> {
                dialog.dismiss();
            });
            dialog.show();

        });

        // Saat menu about us di klik
        aboutUs.setOnClickListener(view1 -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.layout_about_us);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            final Button btnOk= dialog.findViewById(R.id.btnOk);
            final TextView tv_about_us = dialog.findViewById(R.id.tv_about_us);

            InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
            interfaceAdmin.viewAbout().enqueue(new Callback<List<AppModel>>() {
                @Override
                public void onResponse(Call<List<AppModel>> call, Response<List<AppModel>> response) {
                    List<AppModel> appModelList = response.body();
                    if (appModelList.size() > 0) {
                        tv_about_us.setText(appModelList.get(0).getAbut_us());
                    } else {
                        Toast.makeText(getContext(), "Failed load about", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<AppModel>> call, Throwable t) {

                }
            });
                    
                    
                    
            btnOk.setOnClickListener(view2 -> {
                dialog.dismiss();
            });
            dialog.show();
        });

        // Saat button logout di klik
        logout.setOnClickListener(view1 -> {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                SharedPreferences.Editor editor = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.show();



        });

        return view;
    }

    // method untuk load photo profile
    private void getProfile(String user_id) {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getProfile(user_id).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                if (profileModelList.size() > 0) {

                    tv_username.setText(profileModelList.get(0).getUsername());
                    tv_email.setText(profileModelList.get(0).getEmail());
                    Glide.with(getActivity())
                            .load(profileModelList.get(0).getPhoto_profile())
                            .thumbnail(0.5f)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                            .fitCenter()
                            .centerCrop()
                            .placeholder(R.drawable.template_img)
                            .override(1024, 768)
                            .into(iv_profile);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {

            }
        });
    }

    // method untuk mengirim phoot ke server
    private void updateProfile(String user_id, String image) {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.updateImageProfile(user_id, image).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                ProfileModel profileModel = response.body();

                if(profileModel.getStatus().equals("success")){

                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==  RESULT_OK && requestCode == TAG_GALLERY && data != null && data.getData() != null){

            Uri uri_path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri_path);
                iv_profile.setImageBitmap(bitmap);
                tvApply.setVisibility(View.VISIBLE);
                Snackbar.make(getView(), "Successfully load image", Snackbar.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Snackbar.make(getView(), "Failed to load image", Snackbar.LENGTH_LONG).show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}