package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recipe_app.Admin.Adapter.ReportUserAdapter;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Admin.Model.UserReportModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportUserFragment extends Fragment {
    RecyclerView rv_user;
    ReportUserAdapter reportUserAdapter;
    LinearLayoutManager linearLayoutManager;
    TabLayout tabLayout;
    SearchView searchView;
    List<UserReportModel> userReportModelList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_user, container, false);

        rv_user = view.findViewById(R.id.rv_user);
        tabLayout = view.findViewById(R.id.tab_layout);
        searchView = view.findViewById(R.id.search_bar);

        getAllReport(1);



        tabLayout.addTab(tabLayout.newTab().setText("Reported User"));
        tabLayout.addTab(tabLayout.newTab().setText("Read Report"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    getAllReport(1);
                }
                if (tab.getPosition() == 1) {
                    getAllReport(0);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });











        return view;
    }



    // get all report
    private void getAllReport(Integer status){
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllReport(status).enqueue(new Callback<List<UserReportModel>>() {
            @Override
            public void onResponse(Call<List<UserReportModel>> call, Response<List<UserReportModel>> response) {
                if (response.isSuccessful()) {
                    List<UserReportModel> userReportModelList = response.body();
                    if (userReportModelList.size() > 0) {
                        reportUserAdapter = new ReportUserAdapter(getContext(), userReportModelList);
                        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
                        rv_user.setLayoutManager(linearLayoutManager);
                        rv_user.setAdapter(reportUserAdapter);
                        rv_user.setHasFixedSize(true);




                    }
                }

            }

            @Override
            public void onFailure(Call<List<UserReportModel>> call, Throwable t) {

            }
        });




    }



}