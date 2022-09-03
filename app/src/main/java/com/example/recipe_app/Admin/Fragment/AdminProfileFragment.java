package com.example.recipe_app.Admin.Fragment;

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
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Fragment.UpdateEmailFragment;
import com.example.recipe_app.Fragment.UpdatePassword;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProfileFragment extends Fragment {
    RelativeLayout rl_version, rl_about, rl_logout,rl_username, rl_email, rl_password;
    ProgressDialog pd;
    ImageView iv_profile;
    TextView tv_username, tv_email, tv_img_picker, tv_apply;
    String userid;
    final Integer TAG_GALLERY = 222;
    Bitmap bitmap;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root =  inflater.inflate(R.layout.fragment_admin_profile, container, false);
       rl_version = root.findViewById(R.id.rl_version);
       rl_about = root.findViewById(R.id.rl_about_us);
       tv_email = root.findViewById(R.id.tv_email);
       tv_username = root.findViewById(R.id.tv_username);
       tv_img_picker = root.findViewById(R.id.tv_photo);
       iv_profile = root.findViewById(R.id.iv_profile);
       rl_logout = root.findViewById(R.id.rl_logout);
       tv_apply = root.findViewById(R.id.tv_apply);
       rl_username = root.findViewById(R.id.rl_username);
       rl_email = root.findViewById(R.id.rl_email);
       rl_password = root.findViewById(R.id.rl_update_pass);

       pd = new ProgressDialog(getContext());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);



       rl_about.setOnClickListener(view -> {
           Dialog dialog = new Dialog(getContext());
           dialog.setContentView(R.layout.layout_about_us);
           dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
           final TextView tv_about = dialog.findViewById(R.id.tv_about_us);
           tv_about.setVisibility(View.GONE);
           final EditText edt_about = dialog.findViewById(R.id.edt_about_us);
           final Button btn_ok = dialog.findViewById(R.id.btnOk);
           btn_ok.setText("Update");




           InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
           interfaceAdmin.viewAbout().enqueue(new Callback<List<AppModel>>() {
               @Override
               public void onResponse(Call<List<AppModel>> call, Response<List<AppModel>> response) {
                   List<AppModel> appModelList = response.body();
                   if (appModelList.size() > 0 ) {
                       edt_about.setText(appModelList.get(0).getAbut_us());
                   } else {
                       Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                   }
               }

               @Override
               public void onFailure(Call<List<AppModel>> call, Throwable t) {

               }
           });


           btn_ok.setOnClickListener(view1 -> {
               pd.setMessage("Updating...");
                pd.show();
                pd.setCancelable(false);

               
               InterfaceAdmin interfaceAdmin1 = DataApi.getClient().create(InterfaceAdmin.class);
               interfaceAdmin1.updateAboutUs(edt_about.getText().toString()).enqueue(new Callback<AppModel>() {
                   @Override
                   public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                       AppModel appModel = response.body();
                       if (appModel.getSuccess() == 1) {
                           Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                           pd.dismiss();
                           dialog.dismiss();
                       } else {
                           Toast.makeText(getContext(), "Failed to update ", Toast.LENGTH_SHORT).show();
                           pd.dismiss();
                       }
                   }

                   @Override
                   public void onFailure(Call<AppModel> call, Throwable t) {
                       Toast.makeText(getContext(), "Error no connection", Toast.LENGTH_SHORT).show();
                       pd.dismiss();

                   }





               });
               
               

           });
           dialog.show();
       });



        rl_username.setOnClickListener(view1 -> {
            Dialog dialog_username = new Dialog(getContext());
            dialog_username.setContentView(R.layout.layout_update_username);
            dialog_username.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog_username.show();

            final EditText edt_username = dialog_username.findViewById(R.id.edt_username);
            final Button btnUpdate = dialog_username.findViewById(R.id.btn_update);
            edt_username.requestFocus();


            btnUpdate.setOnClickListener(view2-> {
                InterfaceProfile ip = DataApi.getClient().create(InterfaceProfile.class);
                ip.updateUsername(userid, edt_username.getText().toString()).enqueue(new Callback<ProfileModel>() {
                    @Override
                    public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                        ProfileModel profileModel = response.body();
                        if (profileModel.getSuccess().equals("1")) {
                            Toast.makeText(getContext(), "Username updated", Toast.LENGTH_SHORT).show();
                            dialog_username.dismiss();
                        } else {
                            Toast.makeText(getContext(), profileModel.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileModel> call, Throwable t) {
                        Toast.makeText(getContext(), "Error no connection", Toast.LENGTH_SHORT).show();

                    }
                });
            });







        });

       rl_version.setOnClickListener(view -> {
           ProgressDialog progressDialog = new ProgressDialog(getContext());
           progressDialog.setMessage("Load data...");
           progressDialog.show();
           progressDialog.setCancelable(false);
           Dialog dialog = new Dialog(getContext());
           dialog.setContentView(R.layout.layout_app_version);
           dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

           final TextView tv_app_ver = dialog.findViewById(R.id.tv_version);
           final EditText edt_app_ver = dialog.findViewById(R.id.edt_app_version);
           final Button btnUpdate = dialog.findViewById(R.id.btnOk);
           btnUpdate.setText("Update");
           tv_app_ver.setVisibility(View.GONE);
           dialog.show();

           // get data from API
           InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
           interfaceAdmin.viewAbout().enqueue(new Callback<List<AppModel>>() {
               @Override
               public void onResponse(Call<List<AppModel>> call, Response<List<AppModel>> response) {
                   List<AppModel> appModelList = response.body();
                   if (appModelList.size() > 0) {
                       edt_app_ver.setText(appModelList.get(0).getApp_version());
                       progressDialog.dismiss();
                   } else {
                       Toast.makeText(getContext(), "Failed load data", Toast.LENGTH_SHORT).show();
                       progressDialog.dismiss();
                   }

               }

               @Override
               public void onFailure(Call<List<AppModel>> call, Throwable t) {
                   Toast.makeText(getContext(), "Error no connection", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();


               }


           });
           btnUpdate.setOnClickListener(view1 -> {
               ProgressDialog pd1 = new ProgressDialog(getContext());
               pd1.setMessage("Udated...");
               pd1.setCancelable(false);
               pd1.show();

               InterfaceAdmin interfaceAdmin1 = DataApi.getClient().create(InterfaceAdmin.class);
               interfaceAdmin1.updateAppVersion(edt_app_ver.getText().toString()).enqueue(new Callback<AppModel>() {
                   @Override
                   public void onResponse(Call<AppModel> call, Response<AppModel> response) {
                       AppModel appModel = response.body();
                       if (appModel.getSuccess() == 1) {
                           Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                           pd1.dismiss();
                           dialog.dismiss();

                       } else {
                           Toast.makeText(getContext(), "Updated failed", Toast.LENGTH_SHORT).show();
                           pd1.dismiss();
                           dialog.dismiss();

                       }
                   }

                   @Override
                   public void onFailure(Call<AppModel> call, Throwable t) {
                       pd1.dismiss();
                       dialog.dismiss();


                   }
               });
           });
       });

       // get username and photo profile
        getAdminInfo();

        // LOG OUT
        rl_logout.setOnClickListener(view -> {
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

        // change photo profile
        iv_profile.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAG_GALLERY);

            }
        });

        tv_img_picker.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAG_GALLERY);

            }
        });

        // BUTTON APPLY IS CLICKED

        tv_apply.setOnClickListener(view1 -> {
            if (bitmap != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure want to change your profile picture?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    pd.setMessage("Please wait...");
                    pd.show();
                    pd.setCancelable(false);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] imgByte = byteArrayOutputStream.toByteArray();
                    String imgString = Base64.encodeToString(imgByte, Base64.DEFAULT);
                    tv_apply.setVisibility(View.GONE);

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


        // Update email
        rl_email.setOnClickListener(view -> {
            Fragment fragment = new UpdateEmailFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, fragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        // Update password
        rl_password.setOnClickListener(view -> {
            Fragment fragment = new UpdatePassword();
            Bundle bundle = new Bundle();
            bundle.putString("admin", "admin");
            fragment.setArguments(bundle);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, fragment);
            ft.addToBackStack(null);
            ft.commit();
        });




       return root;
    }

    // get data admin
    private void getAdminInfo() {
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAdminInfo(userid).enqueue(new Callback<List<AdminModel>>() {
            @Override
            public void onResponse(Call<List<AdminModel>> call, Response<List<AdminModel>> response) {
                List<AdminModel> adminModelList = response.body();
                if (adminModelList.size() > 0) {
                    tv_username.setText(adminModelList.get(0).getUsername());
                    // load image profile
                    Glide.with(getContext())
                            .load(adminModelList.get(0).getPhoto_profile())
                            .thumbnail(0.5f)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .dontAnimate()
                            .fitCenter()
                            .centerCrop()
                            .override(200, 200)
                            .into(iv_profile);

                    tv_email.setText(adminModelList.get(0).getEmail());

                } else {
                    Toast.makeText(getContext(), "Failed to load info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {

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

                if (profileModel.getStatus().equals("success")) {

                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                pd.dismiss();
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
                tv_apply.setVisibility(View.VISIBLE);
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