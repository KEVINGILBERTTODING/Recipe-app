package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
//import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import com.example.recipe_app.Admin.Adapter.ListUsersAdapter;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    RecyclerView rv_user;
    ListUsersAdapter listUsersAdapter;
    List<AdminModel> adminModelList;
    ImageButton btnBack;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        rv_user = view.findViewById(R.id.rv_user);
        btnBack = view.findViewById(R.id.btn_back);
        searchView = view.findViewById(R.id.search_barr);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_admin, new DashboardFragment());
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        // call method get data
        getAllUser();

        // search user
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });


        return view;
    }

    private void filter(String newText) {
        ArrayList<AdminModel> filteredList = new ArrayList<>();

        for (AdminModel item : adminModelList) {
            if (item.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);

            }
        }


        listUsersAdapter.filterList(filteredList);


        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
        } else {
            listUsersAdapter.filterList(filteredList);
        }


    }


    // method for get all user data
    private void getAllUser() {
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.countUsers().enqueue(new Callback<List<AdminModel>>() {
            @Override
            public void onResponse(Call<List<AdminModel>> call, Response<List<AdminModel>> response) {
                if (response.isSuccessful()){
                    adminModelList = response.body();
                    listUsersAdapter = new ListUsersAdapter(getContext(), adminModelList);
                    rv_user.setAdapter(listUsersAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_user.setLayoutManager(linearLayoutManager);
                    rv_user.setHasFixedSize(true);
                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {

            }
        });
    }
}