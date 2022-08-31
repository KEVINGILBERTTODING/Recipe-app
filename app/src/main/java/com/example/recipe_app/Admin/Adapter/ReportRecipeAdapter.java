package com.example.recipe_app.Admin.Adapter;

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
import com.example.recipe_app.Admin.Model.RecipeReportmodel;
import com.example.recipe_app.R;

import java.util.List;

public class ReportRecipeAdapter extends RecyclerView.Adapter<ReportRecipeAdapter.ViewHolder> {

    Context context;
    List<RecipeReportmodel> recipeReportmodelList;

    public ReportRecipeAdapter(Context context, List<RecipeReportmodel> recipeReportmodelList) {
        this.context = context;
        this.recipeReportmodelList = recipeReportmodelList;

    }


    @NonNull
    @Override
    public ReportRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_recipe_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportRecipeAdapter.ViewHolder holder, int position) {

        holder.tv_username.setText(recipeReportmodelList.get(position).getUsername());
        holder.tv_date.setText(recipeReportmodelList.get(position).getDate());
        holder.tv_title.setText(recipeReportmodelList.get(position).getTitle());

        Glide.with(context)
                .load(recipeReportmodelList.get(position).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .into(holder.iv_profile);

    }

    @Override
    public int getItemCount() {
        return recipeReportmodelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_username, tv_date, tv_title;
        ImageView iv_profile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_username = itemView.findViewById(R.id.tv_username);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_profile = itemView.findViewById(R.id.iv_user);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
