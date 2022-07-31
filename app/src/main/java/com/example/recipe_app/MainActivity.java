package com.example.recipe_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.recipe_app.Fragment.AllRecipesFragment;
import com.example.recipe_app.Fragment.CategoryFragment;
import com.example.recipe_app.Fragment.HomeFragment;
import com.example.recipe_app.Fragment.TrendingRecipesFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private MeowBottomNavigation bnv_Main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnv_Main = findViewById(R.id.bnv_Main);

        bnv_Main.setScrollbarFadingEnabled(true);


        // Added a listener to the bottom navigation bar
        bnv_Main.add(new MeowBottomNavigation.Model(1,R.drawable.home));
        bnv_Main.add(new MeowBottomNavigation.Model(2,R.drawable.search));
        bnv_Main.add(new MeowBottomNavigation.Model(3,R.drawable.bookmark));
        bnv_Main.add(new MeowBottomNavigation.Model(4,R.drawable.person));
        bnv_Main.show(1,true);


        replace(new HomeFragment());
        bnv_Main.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        replace(new HomeFragment());
                        break;

                    case 2:
                        replace(new TrendingRecipesFragment());
                        break;

                    case 3:
                        replace(new CategoryFragment());
                        break;

                    case 4:
                        replace(new AllRecipesFragment());
                        break;

                }
                return null;
            }
        });



    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

}