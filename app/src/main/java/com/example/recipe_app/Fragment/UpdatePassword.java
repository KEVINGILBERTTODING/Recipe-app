package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdatePassword extends Fragment {

    Button btnUpdate;
    TextInputLayout txtPassword;
    TextInputEditText txtPasswordUpdate;
    List<ProfileModel> profileModels;
    String usernamex, useridx;
    ImageButton btnBack;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_update_password, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        usernamex = sharedPreferences.getString(TAG_USERNAME, null);
        useridx = sharedPreferences.getString("user_id", null);



        btnUpdate = view.findViewById(R.id.btn_update_password);
        txtPassword = view.findViewById(R.id.til_old_pass);
        txtPasswordUpdate = view.findViewById(R.id.ti_old_password);
        btnBack = view.findViewById(R.id.btnBack);


        btnUpdate.setOnClickListener(view1 -> {
            String password = txtPasswordUpdate.getText().toString();
            if (password.isEmpty()){
                txtPassword.setError("Password is required");
            }else{
                txtPassword.setError(null);
                checkOldPassword(useridx, password);


            }
        });

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fm = getFragmentManager();
            fm.popBackStack();
        });




        return view;
    }

    // method untuk cek password lama
    private void checkOldPassword(String user_id, String password) {
      InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
     interfaceProfile.checkOldPassword(user_id, password).enqueue(new Callback<ProfileModel>() {
         @Override
         public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
             ProfileModel profileModel = response.body();
             if (profileModel.getSuccess().equals("1")){
                 if (getArguments().getString("admin") != null) {

                     FragmentManager fragmentManager = getFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.fragment_admin, new CreateNewPasswordFragment());
                     fragmentTransaction.addToBackStack(null);
                     fragmentTransaction.commit();

                 } else {
                     FragmentManager fragmentManager = getFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.fragment_container, new CreateNewPasswordFragment());
                     fragmentTransaction.addToBackStack(null);
                     fragmentTransaction.commit();
                 }







             }else{
                 Toast.makeText(getContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
             }
         }
         @Override
         public void onFailure(Call<ProfileModel> call, Throwable t) {
             Snackbar.make(getView(), "No Connection", Snackbar.LENGTH_SHORT).show();
         }
     });
    }


}