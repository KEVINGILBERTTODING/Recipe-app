package com.example.recipe_app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Fragment.UserLikeFragment;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeCategoryPopular extends RecyclerView.Adapter<RecipeCategoryPopular.ViewHolder> {

    Context context;
    List<RecipeModel> recipeModels;

    public RecipeCategoryPopular(Context context, List<RecipeModel> recipeModels) {
        this.context = context;
        this.recipeModels = recipeModels;
    }


    @NonNull
    @Override
    public RecipeCategoryPopular.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_recipe_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCategoryPopular.ViewHolder holder, int position) {
        holder.tv_duration.setText(recipeModels.get(position).getDuration());
        holder.tv_title.setText(recipeModels.get(position).getTitle());
        holder.tv_username.setText(recipeModels.get(position).getUsername());



        // set image recipe
        Glide.with(context)
                .load(recipeModels.get(position).getImage())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.template_img)
                .dontAnimate()
                .override(1024, 768)
                .fitCenter()
                .centerCrop()
                .into(holder.img_recipe);



        // tv username jika di klik maka akan menampilkan profile
        holder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ShowProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", recipeModels.get(position).getUser_id());
                fragment.setArguments(bundle);

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });



    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }

    // Method from searchview
    public void filterList(ArrayList<RecipeModel> filteredList) {

        recipeModels = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_rating, tv_duration, tv_title, tv_username;
        ImageView img_recipe;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_recipe = itemView.findViewById(R.id.img_recipe);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_title = itemView.findViewById(R.id.tv_recipe_name);
            tv_username = itemView.findViewById(R.id.tv_username);

            itemView.setOnClickListener(this);




        }

        @Override
        public void onClick(View view) {

            // get position of item clicked
            Fragment fragment = new DetailRecipeFragment();

            // Send data to detailrecipe fragment

            Bundle bundle = new Bundle();
            bundle.putString("recipe_id", recipeModels.get(getAdapterPosition()).getRecipe_id());
            bundle.putString("user_id", recipeModels.get(getAdapterPosition()).getUser_id());
            bundle.putString("verified", recipeModels.get(getAdapterPosition()).getVerified());
            bundle.putString("username", recipeModels.get(getAdapterPosition()).getUsername());
            bundle.putString("title", recipeModels.get(getAdapterPosition()).getTitle());
            bundle.putString("description", recipeModels.get(getAdapterPosition()).getDescription());
            bundle.putString("category", recipeModels.get(getAdapterPosition()).getCategory());
            bundle.putString("servings", recipeModels.get(getAdapterPosition()).getServings());
            bundle.putString("duration", recipeModels.get(getAdapterPosition()).getDuration());
            bundle.putString("ingredients", recipeModels.get(getAdapterPosition()).getIngredients());
            bundle.putString("steps", recipeModels.get(getAdapterPosition()).getSteps());
            bundle.putString("upload_date", recipeModels.get(getAdapterPosition()).getUpload_date());
            bundle.putString("upload_time", recipeModels.get(getAdapterPosition()).getUpload_time());
            bundle.putString("image", recipeModels.get(getAdapterPosition()).getImage());
            bundle.putString("status", recipeModels.get(getAdapterPosition()).getStatus());
            bundle.putString("ratings", recipeModels.get(getAdapterPosition()).getRatings());
            bundle.putString("likes", recipeModels.get(getAdapterPosition()).getLikes());
            bundle.putString("photo_profile", recipeModels.get(getAdapterPosition()).getPhoto_profile());
            bundle.putString("email", recipeModels.get(getAdapterPosition()).getEmail());
            bundle.putString("notes", recipeModels.get(getAdapterPosition()).getNote());
            fragment.setArguments(bundle);

            // get Fragment

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();



        }
    }
}

