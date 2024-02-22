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

import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEmailFragment extends Fragment {

    String usernamex, useridx;
    TextInputLayout til_email, til_password;
    TextInputEditText txt_email, txt_password;
    Button btnUpdate;
    ImageButton btnBack;
    ConnectivityManager conMgr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmenta
        View view= inflater.inflate(R.layout.fragment_update_email, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        usernamex = sharedPreferences.getString(TAG_USERNAME, null);
        useridx = sharedPreferences.getString("user_id", null);
        til_email = view.findViewById(R.id.til_email);
        til_password = view.findViewById(R.id.til_pass);
        txt_email = view.findViewById(R.id.ti_email);
        txt_password = view.findViewById(R.id.ti_pass);
        btnUpdate = view.findViewById(R.id.btn_update_email);
        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        // validasi form tidak boleh kosong

        btnUpdate.setOnClickListener(view1 ->  {
            if (txt_email.getText().toString().isEmpty() || txt_password.getText().toString().isEmpty()){
                Snackbar.make(view, "Field cannot empty", Snackbar.LENGTH_LONG).show();
            } else {
                checkOldPassword(useridx, txt_password.getText().toString());
            }

        });




        return view;
    }

    // method untuk mengubah alamat email
    private void updateEmail(String userid, String email) {
        InterfaceProfile interfaceProfile  = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.updateEmail(userid, email).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                ProfileModel profileModel = response.body();
                if (profileModel.getStatus().equals("success")){
                    Toasty.success(getContext(), "Email is updated", Toasty.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_LONG).show();

            }
        });
    }

    // method untuk cek password lama
    private void checkOldPassword(String user_id, String password) {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.checkOldPassword(user_id, password).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if (response.body().getSuccess().equals("1")){

                    updateEmail(useridx, txt_email.getText().toString());

                }else{
                    Toasty.error(getContext(), "Password is incorrect", Toasty.LENGTH_SHORT).show();
                    til_password.setError("Password is incorrect");
                }
            }
            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_LONG).show();
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