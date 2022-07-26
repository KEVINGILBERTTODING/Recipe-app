package com.example.recipe_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RecipeDetail extends AppCompatActivity {

    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        tv_title = findViewById(R.id.tv_judul_resep);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        tv_title.setText(title);
    }
}