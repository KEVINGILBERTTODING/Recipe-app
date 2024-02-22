package com.example.recipe_app.ui.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.recipe_app.ui.Admin.Fragment.AdminProfileFragment;
import com.example.recipe_app.ui.Admin.Fragment.DashboardFragment;
import com.example.recipe_app.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class DashboardActivity extends AppCompatActivity {

    private MeowBottomNavigation bnv_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bnv_dashboard = findViewById(R.id.bnv_dashboard);

        // Added a listener to the bottom navigation bar
        bnv_dashboard.add(new MeowBottomNavigation.Model(1,R.drawable.ic_home));
        bnv_dashboard.add(new MeowBottomNavigation.Model(2,R.drawable.ic_person));
        bnv_dashboard.show(1,true);
        replace(new DashboardFragment());
        bnv_dashboard.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()){
                    case 1:
                        replace(new DashboardFragment());
                        break;

                    case 2:
                        replace(new AdminProfileFragment());
                        break;


                }
                return null;
            }
        });


    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_admin,fragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}