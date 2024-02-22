package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.ui.activities.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.ui.activities.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipe_app.ui.activities.LoginActivity;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewPasswordFragment extends Fragment {
    TextInputEditText txtPassword, txtPasswordConf;
    TextInputLayout tilPassword, tilPasswordConf;
    String usernamex, useridx;
    Button btnUpdate;
    ImageButton btnBack;
    ConnectivityManager conMgr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_new_password, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        usernamex = sharedPreferences.getString(TAG_USERNAME, null);
        useridx = sharedPreferences.getString("user_id", null);


        tilPassword = view.findViewById(R.id.til_new_pass);
        tilPasswordConf = view.findViewById(R.id.til_pass_conf);
        txtPassword = view.findViewById(R.id.ti_new_pass);
        txtPasswordConf = view.findViewById(R.id.ti_pass_conf);
        btnUpdate = view.findViewById(R.id.btn_update_password);
        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });


        btnUpdate.setOnClickListener(view1 ->  {

            if (txtPassword.getText().toString().equals(txtPasswordConf.getText().toString())){
                updatePassword(useridx, txtPassword.getText().toString());

            } else if (txtPassword.getText().toString().isEmpty() || txtPasswordConf.getText().toString().isEmpty()){
                Toasty.error(getContext(), "field cannot be empty", Toasty.LENGTH_SHORT ).show();

            } else if(txtPassword.length() < 6 || txtPasswordConf.length() < 6){
                Toasty.error(getContext(), "password must be at least 6 characters", Toasty.LENGTH_SHORT ).show();
            } else{
                Toast.makeText(getContext(), "password doesn't match", Toast.LENGTH_SHORT).show();
                tilPasswordConf.setError("password doesn't match");
            }


        });


        return view;
    }

    // Method untuk update password
    private void updatePassword(String user_id, String password){
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.updatePassword(user_id, password).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                ProfileModel profileModel = response.body();
                if (profileModel.getStatus().equals("success")){
                    Toast.makeText(getContext(), "Password is updated", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();
                    startActivity(new android.content.Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }else{
                    // Jika gagal update password
                    Toasty.error(getContext(), "Failed", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();


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