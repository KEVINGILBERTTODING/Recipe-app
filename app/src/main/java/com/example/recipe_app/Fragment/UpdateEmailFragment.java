package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEmailFragment extends Fragment {

    String usernamex, useridx;
    TextInputLayout til_email, til_password;
    TextInputEditText txt_email, txt_password;
    Button btnUpdate;
    ImageButton btnBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                    Toast.makeText(getContext(), "Email is updated", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
                    til_password.setError("Password is incorrect");
                }
            }
            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Snackbar.make(getView(), "No Connection", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

}