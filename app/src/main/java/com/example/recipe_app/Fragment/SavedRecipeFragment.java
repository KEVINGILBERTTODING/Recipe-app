package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recipe_app.Adapter.MyRecipeAdapter;
import com.example.recipe_app.Adapter.SavedRecipeAdapter;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedRecipeFragment extends Fragment {

    List<RecipeModel> recipeModelList;
    SavedRecipeAdapter savedRecipeAdapter;
    RecyclerView rv_saved_recipe;
    String username, userid;



    public SavedRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_recipe, container, false);
        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        rv_saved_recipe = view.findViewById(R.id.recycler_saved_recipe);

        getSavedRecipe(userid);


        return view;
    }

    private void getSavedRecipe(String user_id) {

        DataApi.getClient().create(InterfaceRecipe.class).getSavedRecipe(user_id).enqueue(new retrofit2.Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, retrofit2.Response<List<RecipeModel>> response) {
                recipeModelList = response.body();
                savedRecipeAdapter = new SavedRecipeAdapter(getContext(), recipeModelList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                rv_saved_recipe.setLayoutManager(linearLayoutManager);
                rv_saved_recipe.setAdapter(savedRecipeAdapter);
                rv_saved_recipe.setHasFixedSize(true);


            }
            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Snackbar.make(getView(), "Check your connection", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

//    private void getSavedRecipe(String user_id) {
//
//        DataApi.getClient().create(InterfaceRecipe.class).getSavedRecipe(user_id).enqueue(new Callback<List<RecipeModel>>() {
//            @Override
//            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
//
//                    recipeModelList = response.body();
//                    savedRecipeAdapter = new SavedRecipeAdapter(getContext(), recipeModelList);
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//
//                    rv_saved_recipe.setLayoutManager(linearLayoutManager);
//                    rv_saved_recipe.setAdapter(savedRecipeAdapter);
//                    rv_saved_recipe.setHasFixedSize(true);
//                    savedRecipeAdapter.notifyDataSetChanged();
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
//                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}