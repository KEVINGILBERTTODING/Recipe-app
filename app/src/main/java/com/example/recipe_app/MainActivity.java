package com.example.recipe_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.recipe_app.Fragment.AllRecipesFragment;
import com.example.recipe_app.Fragment.CategoryFragment;
import com.example.recipe_app.Fragment.CreateRecipeFragment;
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

        // Added a listener to the bottom navigation bar
        bnv_Main.add(new MeowBottomNavigation.Model(1,R.drawable.ic_home));
        bnv_Main.add(new MeowBottomNavigation.Model(2,R.drawable.ic_search));
        bnv_Main.add(new MeowBottomNavigation.Model(3,R.drawable.ic_plus));
        bnv_Main.add(new MeowBottomNavigation.Model(4,R.drawable.ic_save));
        bnv_Main.add(new MeowBottomNavigation.Model(5,R.drawable.ic_person));
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
                        replace(new AllRecipesFragment());
                        break;

                    case 3:
                        replace(new CreateRecipeFragment());
                        break;

                    case 4:
                        replace(new AllRecipesFragment());
                        break;

                    case 5:
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

    // show alert dilaog when user click back button

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int
                            which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }
                });
        builder.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int
                            which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}