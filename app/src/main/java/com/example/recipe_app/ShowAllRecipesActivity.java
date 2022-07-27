package com.example.recipe_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;

import com.example.recipe_app.Fragment.AllRecipesFragment;

public class ShowAllRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_recipes);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_show_all_recipes, new AllRecipesFragment()).commit();
    }
}