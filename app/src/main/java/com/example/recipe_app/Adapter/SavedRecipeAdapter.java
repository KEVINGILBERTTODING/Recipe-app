package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SavedRecipeAdapter extends RecyclerView.Adapter<SavedRecipeAdapter.ViewHolder> {

    private List<RecipeModel> recipeModels;
    Context context;


    // Buat constructor
    public SavedRecipeAdapter(Context context, List<RecipeModel> recipeModels) {
        this.context = context;
        this.recipeModels = recipeModels;
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
        ImageView iv_recipe, iv_profile;
        ImageButton btnSaved, btnLike;
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
        }

        @Override
        public void onClick(View view) {

        }
    }
}
