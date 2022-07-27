package com.example.recipe_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Fragment.AllRecipesFragment;
import com.example.recipe_app.Fragment.CategoryFragment;

public class ShowAllRecipesActivity extends AppCompatActivity {

    String result, result2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_recipes);
        Intent intent2 = getIntent();

        // get data from intent and validate whether it is null or not
       if (intent2.getStringExtra("all") != null) {
           getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_show_all_recipes, new AllRecipesFragment()).commit();
       } else if (intent2.getStringExtra("kategori") != null) {
           getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_show_all_recipes, new CategoryFragment()).commit();
       }
    }
}