package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Admin.Adapter.ReportRecipeAdapter;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.RecipeReportmodel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportRecipeFragment extends Fragment {
    ImageButton btnBack;
    RecyclerView rv_recipe;
    List<RecipeReportmodel> recipeReportmodelList;
    ReportRecipeAdapter reportRecipeAdapter;
    TabLayout tabLayout;
    LinearLayoutManager linearLayoutManager;
    TextView tv_no_data;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report_recipe, container, false);
        btnBack = root.findViewById(R.id.btn_back);
        rv_recipe = root.findViewById(R.id.rv_recipe);
        tabLayout = root.findViewById(R.id.tab_layout);
        tv_no_data = root.findViewById(R.id.tv_no_report);

        btnBack.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });

        getRecipeReport(1);


        tabLayout.addTab(tabLayout.newTab().setText("Reported Recipe"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Rejected"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //get all report
                if (tab.getPosition() == 0) {
                    getRecipeReport(1);
                }

                // get accepted report
                else if  (tab.getPosition() == 1) {
                    getRecipeReport(2);
                }

                // get rejcted report
                else if(tab.getPosition() == 2) {
                    getRecipeReport(0);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return root;
    }

    // get all report
    private void getRecipeReport(Integer status){
        InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
        interfaceAdmin.getAllReportRecipe(status).enqueue(new Callback<List<RecipeReportmodel>>() {
            @Override
            public void onResponse(Call<List<RecipeReportmodel>> call, Response<List<RecipeReportmodel>> response) {
                recipeReportmodelList = response.body();

                if (recipeReportmodelList.size() > 0) {
                    reportRecipeAdapter = new ReportRecipeAdapter(getContext(), recipeReportmodelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rv_recipe.setAdapter(reportRecipeAdapter);
                    rv_recipe.setLayoutManager(linearLayoutManager);
                    rv_recipe.setHasFixedSize(true);
                    tv_no_data.setVisibility(View.GONE);
                    rv_recipe.setVisibility(View.VISIBLE);

                    
                } else  {
                    rv_recipe.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<RecipeReportmodel>> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "No connection", Snackbar.LENGTH_LONG).show();
                rv_recipe.setVisibility(View.GONE);
                tv_no_data.setVisibility(View.VISIBLE);


            }
        });
    }
}