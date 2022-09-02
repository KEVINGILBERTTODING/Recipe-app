package com.example.recipe_app.Admin.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Model.AppModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProfileFragment extends Fragment {
    RelativeLayout rl_version, rl_about;
    ProgressDialog pd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root =  inflater.inflate(R.layout.fragment_admin_profile, container, false);
       rl_version = root.findViewById(R.id.rl_version);
       rl_about = root.findViewById(R.id.rl_about_us);

       pd = new ProgressDialog(getContext());



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

       return root;
    }
}