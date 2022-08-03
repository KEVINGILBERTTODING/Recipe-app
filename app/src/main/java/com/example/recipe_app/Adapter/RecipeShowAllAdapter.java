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
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeShowAllAdapter extends RecyclerView.Adapter<RecipeShowAllAdapter.ViewHolder> {

    Context context;
    List<RecipeModel> recipeModels;

    public RecipeShowAllAdapter(Context context, List<RecipeModel> recipeModels) {
        this.context = context;
        this.recipeModels = recipeModels;
    }


    @NonNull
    @Override
    public RecipeShowAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_show_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeShowAllAdapter.ViewHolder holder, int position) {
        holder.tv_duration.setText(recipeModels.get(position).getDuration());
        holder.tv_title.setText(recipeModels.get(position).getTitle());
        holder.tv_username.setText(recipeModels.get(position).getUsername());
        holder.tv_rating.setText(recipeModels.get(position).getRatings());

        // set image profile
        Glide.with(context)
                .load(recipeModels.get(position).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .into(holder.img_profile);

        // set recipe image
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
                .into(holder.img_recipe);



    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }


    public void filterList(ArrayList<RecipeModel> filteredList) {

        recipeModels = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_rating, tv_duration, tv_title, tv_username;
        ImageView img_recipe, img_profile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_recipe = itemView.findViewById(R.id.img_recipe);
            img_profile = itemView.findViewById(R.id.img_profile);

            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_username = itemView.findViewById(R.id.tv_recipe_username);

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
            fragment.setArguments(bundle);

            // get Fragment

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();



        }
    }
}

