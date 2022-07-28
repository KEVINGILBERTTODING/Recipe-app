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
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;

import java.util.List;

public class RecipeAllAdapter extends RecyclerView.Adapter<RecipeAllAdapter.ViewHolder> {

    Context context;
    List<RecipeModel> recipeModels;

    public RecipeAllAdapter(Context context, List<RecipeModel> recipeModels) {
        this.context = context;
        this.recipeModels = recipeModels;
    }


    @NonNull
    @Override
    public RecipeAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_recipe_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAllAdapter.ViewHolder holder, int position) {
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


        // Set recipe image
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
        // Limit data to show in recycler view
        return recipeModels.size() > 4 ? 4 : recipeModels.size();
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

        }

        @Override
        public void onClick(View view) {


        }
    }
}

