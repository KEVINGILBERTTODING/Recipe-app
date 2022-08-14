package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Fragment.SavedRecipeFragment;
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SavedRecipeAdapter extends RecyclerView.Adapter<SavedRecipeAdapter.ViewHolder> {

    String userid;

    private List<RecipeModel> recipeModels;
    Context context;


    // Buat constructor
    public SavedRecipeAdapter(Context context, List<RecipeModel> recipeModels) {
        this.context = context;
        this.recipeModels = recipeModels;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);
    }
    @NonNull
    @Override
    public SavedRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_saved_recipe, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipeAdapter.ViewHolder holder, int position) {

        // set image recipe
        Glide.with(context)
                .load(recipeModels.get(position).getImage())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .fitCenter()
                .centerCrop()
                .into(holder.iv_recipe);

        // set profile image
        Glide.with(context)
                .load(recipeModels.get(position).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .fitCenter()
                .centerCrop()
                .into(holder.iv_profile);

        // set name
        holder.tv_recipe_name.setText(recipeModels.get(position).getTitle());
        holder.tv_duration.setText(recipeModels.get(position).getDuration());
        holder.tv_username.setText(recipeModels.get(position).getUsername());

        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v, Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.saved_menu_recipe, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:

                                DataApi.getClient().create(InterfaceRecipe.class).deleteSavedRecipe(recipeModels.get(position).getRecipe_id(), userid)
                                        .enqueue(new retrofit2.Callback<RecipeModel>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<RecipeModel> call, retrofit2.Response<RecipeModel> response) {
                                        if (response.isSuccessful()) {

                                            // Menghapus data dari list

                                            recipeModels.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, recipeModels.size());

                                            Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Recipe deleted", Snackbar.LENGTH_SHORT)
                                                    .show();


                                        }
                                    }
                                    @Override
                                    public void onFailure(retrofit2.Call<RecipeModel> call, Throwable t) {
                                        Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Failed to delete recipe", Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
                                });



                                break;

                        }
                        return false;
                    }
                });

                popupMenu.show();

                }
            });


        holder.iv_recipe.setOnClickListener(view -> {
            Fragment fragment= new DetailRecipeFragment();

            Bundle bundle = new Bundle();
            bundle.putString("recipe_id", recipeModels.get(position).getRecipe_id());
            bundle.putString("user_id", recipeModels.get(position).getUser_id());
            bundle.putString("username", recipeModels.get(position).getUsername());
            bundle.putString("title", recipeModels.get(position).getTitle());
            bundle.putString("description", recipeModels.get(position).getDescription());
            bundle.putString("category", recipeModels.get(position).getCategory());
            bundle.putString("servings", recipeModels.get(position).getServings());
            bundle.putString("duration", recipeModels.get(position).getDuration());
            bundle.putString("ingredients", recipeModels.get(position).getIngredients());
            bundle.putString("steps", recipeModels.get(position).getSteps());
            bundle.putString("upload_date", recipeModels.get(position).getUpload_date());
            bundle.putString("upload_time", recipeModels.get(position).getUpload_time());
            bundle.putString("image", recipeModels.get(position).getImage());
            bundle.putString("status", recipeModels.get(position).getStatus());
            bundle.putString("ratings", recipeModels.get(position).getRatings());
            bundle.putString("likes", recipeModels.get(position).getLikes());
            bundle.putString("photo_profile", recipeModels.get(position).getPhoto_profile());
            bundle.putString("email", recipeModels.get(position).getEmail());
            bundle.putString("notes", recipeModels.get(position).getNote());
            fragment.setArguments(bundle);
            // get Fragment

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

        });

        holder.tv_username.setOnClickListener(view -> {

            Fragment fragment= new ShowProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id", recipeModels.get(position).getUser_id());
            fragment.setArguments(bundle);
            // get Fragment
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }

    public void filterList(ArrayList<RecipeModel> filteredList) {

        recipeModels = filteredList;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_recipe, iv_profile;
        ImageButton btnSaved, btnLike, btn_more;
        TextView tv_duration, tv_username, tv_recipe_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_recipe = itemView.findViewById(R.id.img_recipe);
            iv_profile = itemView.findViewById(R.id.img_profile);
            btnSaved = itemView.findViewById(R.id.btn_favorite);
            btnLike = itemView.findViewById(R.id.btn_like);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_username = itemView.findViewById(R.id.tv_recipe_username);
            tv_recipe_name = itemView.findViewById(R.id.tv_title);
            btn_more = itemView.findViewById(R.id.btn_more);
        }


    }


}
