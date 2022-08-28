package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    CardView card_user;
    TextView tv_total_users, tv_dashboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        card_user = view.findViewById(R.id.rl_users);
        tv_total_users = view.findViewById(R.id.tv_total_users);
        tv_dashboard = view.findViewById(R.id.tv_dashboard);


        // count users
        countUser();


        tv_dashboard.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Dashboard", Toast.LENGTH_SHORT).show();
        });

        // Card user is clicked
        card_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction  fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_admin, new UserFragment());
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });



        return view;
    }


    // method untuk menghitung total user
    private void countUser() {
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.countUsers().enqueue(new Callback<List<AdminModel>>() {
            @Override
            public void onResponse(Call<List<AdminModel>> call, Response<List<AdminModel>> response) {
                if (response.isSuccessful()) {
                    List<AdminModel> adminModelList = response.body();
                    Log.d("count", "onResponse: " + adminModelList.size());
                    tv_total_users.setText(adminModelList.size() + " Users");

                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {
                Toast.makeText(getContext(), "no connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
}