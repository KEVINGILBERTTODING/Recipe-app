package com.example.recipe_app.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.recipe_app.RecipeDetail;

import java.util.List;

public class RecipeTrandingAdapter extends RecyclerView.Adapter<RecipeTrandingAdapter.ViewHolder> {

    Context context;
    List<RecipeModel> recipeModels;

    public RecipeTrandingAdapter(Context context, List<RecipeModel> recipeModels) {
        this.context = context;
        this.recipeModels = recipeModels;
    }


    @NonNull
    @Override
    public RecipeTrandingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_recipe_tranding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeTrandingAdapter.ViewHolder holder, int position) {
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
                .centerCrop()
                .fitCenter()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .into(holder.img_profile);



        Glide.with(context)
                .load(recipeModels.get(position).getImage())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.template_img)
                .dontAnimate()
                .override(1024, 768)
                .centerCrop()
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
            img_profile = itemView.findViewById(R.id.img_profile);

            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_username = itemView.findViewById(R.id.tv_recipe_username);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), RecipeDetail.class);
            intent.putExtra("title", recipeModels.get(getAdapterPosition()).getTitle());
            view.getContext().startActivity(intent);


        }
    }
}

