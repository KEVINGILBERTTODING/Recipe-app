package com.example.recipe_app;

import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Adapter.RecipePublicAdapter;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String username, userid;
    TextView  txt_username, txt_userid;
    ShimmerRecyclerView shimmerRecyclerView;
    private List<RecipeModel> recipeModelList;
    private InterfaceRecipe interfaceRecipe;
    RecipePublicAdapter recipePublicAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         shimmerRecyclerView = findViewById(R.id.recycler_recipe);

        SharedPreferences sharedPreferences = getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);


        // show shimmer recyclerview
        setShimmer();

        // Call method get recipe
        getRecipe();


    }

    private void getRecipe() {
        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getRecipe(1);
        call.enqueue(new Callback<List<RecipeModel>>() {


            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                recipePublicAdapter = new RecipePublicAdapter(MainActivity.this, recipeModelList);
                // Make it Horizontal recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

                shimmerRecyclerView.setLayoutManager(linearLayoutManager);
                shimmerRecyclerView.setAdapter(recipePublicAdapter);
                shimmerRecyclerView.setHasFixedSize(true);

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Periksa koneksi anda", Toast.LENGTH_SHORT).show();

            }


        });
    }

    private void setShimmer(){


        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shimmerRecyclerView.setItemViewType((type, position) -> {
            switch (type) {
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
                            ? R.layout.template_list_data_recipe_1
                            : R.layout.template_list_data_recipe_1;

                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_list_data_recipe_1
                            : R.layout.template_list_data_recipe_1;
            }
        });
        shimmerRecyclerView.showShimmer();     // to start showing shimmer

    }
}