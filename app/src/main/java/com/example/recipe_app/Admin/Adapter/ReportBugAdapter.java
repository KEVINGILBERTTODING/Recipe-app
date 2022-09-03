package com.example.recipe_app.Admin.Adapter;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Fragment.DetailBugReportFragment;
import com.example.recipe_app.Admin.Fragment.DetailRecipeReport;
import com.example.recipe_app.Admin.Model.BugReportModel;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;

public class ReportBugAdapter extends RecyclerView.Adapter<ReportBugAdapter.ViewHolder> {

    Context context;
    List<BugReportModel> bugReportModelList;


    public ReportBugAdapter (Context context, List<BugReportModel> bugReportModelList) {
        this.context = context;
        this.bugReportModelList = bugReportModelList;
    }

    @NonNull
    @Override
    public ReportBugAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_recipe_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportBugAdapter.ViewHolder holder, int position) {
        holder.tv_username.setText(bugReportModelList.get(position).getUsername());
        holder.tv_date.setText(bugReportModelList.get(position).getDate());
        holder.tv_title.setText(bugReportModelList.get(position).getTitle());

        Glide.with(context)
                .load(bugReportModelList.get(position).getPhoto_profile())
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
        return  bugReportModelList.size();
    }

    public void filterList(ArrayList<BugReportModel> filteredList) {
        bugReportModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

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

            Fragment fragment =  new DetailBugReportFragment();
            Bundle bundle  = new Bundle();
            bundle.putString("report_id", bugReportModelList.get(getAdapterPosition()).getReport_id());
            bundle.putString("user_id", bugReportModelList.get(getAdapterPosition()).getUser_id());
            bundle.putString("title", bugReportModelList.get(getAdapterPosition()).getTitle());
            bundle.putString("report", bugReportModelList.get(getAdapterPosition()).getReport());
            bundle.putString("image", bugReportModelList.get(getAdapterPosition()).getImage());
            bundle.putString("date", bugReportModelList.get(getAdapterPosition()).getDate());
            bundle.putString("time", bugReportModelList.get(getAdapterPosition()).getTitle());
            bundle.putString("status", bugReportModelList.get(getAdapterPosition()).getStatus());
            bundle.putString("username", bugReportModelList.get(getAdapterPosition()).getUsername());
            bundle.putString("photo_profile", bugReportModelList.get(getAdapterPosition()).getPhoto_profile());
            bundle.putString("email", bugReportModelList.get(getAdapterPosition()).getEmail());
            bundle.putString("time", bugReportModelList.get(getAdapterPosition()).getTime());
            fragment.setArguments(bundle);

            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, fragment);
            ft.addToBackStack(null);
            ft.commit();


        }
    }
}
