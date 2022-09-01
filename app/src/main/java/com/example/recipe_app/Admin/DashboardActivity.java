package com.example.recipe_app.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.recipe_app.Admin.Fragment.DashboardFragment;
import com.example.recipe_app.Fragment.AllRecipesFragment;
import com.example.recipe_app.Fragment.CreateRecipeFragment;
import com.example.recipe_app.Fragment.HomeFragment;
import com.example.recipe_app.Fragment.MyProfileFragment;
import com.example.recipe_app.Fragment.SavedRecipeFragment;
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
        bnv_dashboard.add(new MeowBottomNavigation.Model(2,R.drawable.ic_search));
        bnv_dashboard.add(new MeowBottomNavigation.Model(3,R.drawable.ic_person));
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
//                        replace(new AllRecipesFragment());
                        break;

                    case 3:
//                        replace(new MyProfileFragment());
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