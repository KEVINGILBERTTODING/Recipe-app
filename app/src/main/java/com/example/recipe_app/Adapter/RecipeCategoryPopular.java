package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;

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

        Glide.with(context)
                .load(recipeModels.get(position).getImage())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.template_img)
                .dontAnimate()
                .into(holder.img_recipe);



    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_rating, tv_duration, tv_title, tv_username;
        ImageView img_recipe, img_profile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_recipe = itemView.findViewById(R.id.img_recipe);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_title = itemView.findViewById(R.id.tv_recipe_name);
            tv_username = itemView.findViewById(R.id.tv_username);

        }

        @Override
        public void onClick(View view) {


        }
    }
}

