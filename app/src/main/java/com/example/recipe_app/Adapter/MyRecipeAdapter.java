package com.example.recipe_app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Fragment.MyProfileFragment;
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;

import java.util.List;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.ViewHolder> {

    List<RecipeModel> recipeModels;
    Context context;
    private OnRecipeListener onRecipeListener;


    public MyRecipeAdapter(Context context, List<RecipeModel> recipeModels) {
        this.context = context;
        this.recipeModels = recipeModels;
    }


    @NonNull
    @Override
    public MyRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_my_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeAdapter.ViewHolder holder, int position) {



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

    public void setOnRecipeListener(MyProfileFragment myProfileFragment) {
        this.onRecipeListener = myProfileFragment;

    }

    public void setOnRecipeListener(ShowProfileFragment showProfileFragment) {
        this.onRecipeListener = showProfileFragment;
    }



    public interface OnRecipeListener {
        void onRecipeClick(View view,  int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_recipe;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_recipe = itemView.findViewById(R.id.iv_recipe);



            //Jika igin long click
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    onRecipeListener.onRecipeClick(getAdapterPosition());
//                    return false;
//                }
//            });

            // sekali klik
            itemView.setOnClickListener(this);
            img_recipe.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (onRecipeListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onRecipeListener.onRecipeClick(view, position);
                }
            }

        }

    }

}