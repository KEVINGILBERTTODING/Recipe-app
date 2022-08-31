package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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
    TextView tvNoReport;
    ArrayList<UserReportModel> filteredList = new ArrayList<>();

    ImageButton btnBack;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_user, container, false);

        rv_user = view.findViewById(R.id.rv_user);
        tabLayout = view.findViewById(R.id.tab_layout);
        searchView = view.findViewById(R.id.search_bar);
        tvNoReport = view.findViewById(R.id.tv_no_report);
        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });


        getAllReport(1);

        tabLayout.addTab(tabLayout.newTab().setText("Reported User"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Rejected"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //get all report
                if (tab.getPosition() == 0) {
                    getAllReport(1);
                }

                // get accepted report
                else if  (tab.getPosition() == 1) {
                    getAllReport(2);
                }

                // get rejcted report
                else if(tab.getPosition() == 2) {
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

        // search report by username
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





    // get all report
    private void getAllReport(Integer status){
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllReport(status).enqueue(new Callback<List<UserReportModel>>() {
            @Override
            public void onResponse(Call<List<UserReportModel>> call, Response<List<UserReportModel>> response) {
               

                    if (response.isSuccessful()) {
                        List<UserReportModel> userReportModelList = response.body();
                        reportUserAdapter = new ReportUserAdapter(getContext(), userReportModelList);
                        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
                        rv_user.setLayoutManager(linearLayoutManager);
                        rv_user.setAdapter(reportUserAdapter);
                        tvNoReport.setVisibility(View.GONE);
                        if (userReportModelList.size() == 0) {
                            tvNoReport.setVisibility(View.VISIBLE);
                        }


                    } else {
                        Toast.makeText(getContext(), "Failed t load data", Toast.LENGTH_SHORT).show();

                    }
                

            }

            @Override
            public void onFailure(Call<List<UserReportModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error no connection", Toast.LENGTH_SHORT).show();

            }
        });




    }


    // method search
    private void filter(String newText) {


        for (UserReportModel item : userReportModelList) {

            if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }


        reportUserAdapter.filterList(filteredList);


        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
        } else {
            reportUserAdapter.filterList(filteredList);
        }


    }


}