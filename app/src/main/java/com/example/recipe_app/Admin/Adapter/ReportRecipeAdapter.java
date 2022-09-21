package com.example.recipe_app.Admin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.swipe.SwipeLayout;
import com.example.recipe_app.Admin.Fragment.DetailRecipeReport;
import com.example.recipe_app.Admin.Fragment.ReportRecipeFragment;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Admin.Model.RecipeReportmodel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


        holder.btnDelete.setOnClickListener(view -> {
            InterfaceAdmin interfaceAdmin = DataApi.getClient().create(InterfaceAdmin.class);
            interfaceAdmin.deleteRecipeReport(recipeReportmodelList.get(position).getReport_id()).enqueue(new Callback<RecipeReportmodel>() {
                @Override
                public void onResponse(Call<RecipeReportmodel> call, Response<RecipeReportmodel> response) {
                    RecipeReportmodel recipeReportmodel = response.body();
                    if (recipeReportmodel.getStatus().equals("1")) {
                        Toasty.success(context, "Report deleted successfully", Toasty.LENGTH_SHORT).show();
                        notifyItemChanged(position);
                        notifyItemRangeRemoved(position, recipeReportmodelList.size());
                        recipeReportmodelList.remove(position);

                    } else  {
                        Toasty.warning(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RecipeReportmodel> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toast.LENGTH_SHORT).show();

                }
            });
        });

    }



    @Override
    public int getItemCount() {
        return recipeReportmodelList.size();
    }

    public void filterList(ArrayList<RecipeReportmodel> filteredList) {
        recipeReportmodelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_username, tv_date, tv_title;
        ImageView iv_profile;
        SwipeLayout swipeLayout;
        ImageButton btnDelete;
        RelativeLayout rlList;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_username = itemView.findViewById(R.id.tv_username);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_profile = itemView.findViewById(R.id.iv_user);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            rlList = itemView.findViewById(R.id.rl_list);

            rlList.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Fragment fragment =  new DetailRecipeReport();
            Bundle bundle  = new Bundle();
            bundle.putString("report_id", recipeReportmodelList.get(getAdapterPosition()).getReport_id());
            bundle.putString("recipe_id", recipeReportmodelList.get(getAdapterPosition()).getRecipe_id());
            bundle.putInt("verified", recipeReportmodelList.get(getAdapterPosition()).getVerfied());
            bundle.putString("user_id", recipeReportmodelList.get(getAdapterPosition()).getUser_id());
            bundle.putString("title", recipeReportmodelList.get(getAdapterPosition()).getTitle());
            bundle.putString("report", recipeReportmodelList.get(getAdapterPosition()).getReport());
            bundle.putString("image", recipeReportmodelList.get(getAdapterPosition()).getImage());
            bundle.putString("date", recipeReportmodelList.get(getAdapterPosition()).getDate());
            bundle.putString("time", recipeReportmodelList.get(getAdapterPosition()).getTitle());
            bundle.putString("status", recipeReportmodelList.get(getAdapterPosition()).getStatus());
            bundle.putString("username", recipeReportmodelList.get(getAdapterPosition()).getUsername());
            bundle.putString("photo_profile", recipeReportmodelList.get(getAdapterPosition()).getPhoto_profile());
            bundle.putString("email", recipeReportmodelList.get(getAdapterPosition()).getEmail());
            fragment.setArguments(bundle);

            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    }
}
