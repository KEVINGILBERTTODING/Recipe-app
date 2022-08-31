package com.example.recipe_app.Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    }

    @Override
    public int getItemCount() {
        return recipeReportmodelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
