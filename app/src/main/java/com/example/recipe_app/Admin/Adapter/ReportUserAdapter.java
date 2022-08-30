package com.example.recipe_app.Admin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Fragment.DetailUserReport;
import com.example.recipe_app.Admin.Fragment.ReportUserFragment;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Admin.Model.UserReportModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReportUserAdapter extends RecyclerView.Adapter<ReportUserAdapter.ViewHolder> {

    Context  context;
    List<UserReportModel> userReportModelList;

    // Constructor
    public ReportUserAdapter(Context context, List<UserReportModel> userReportModelList) {
        this.context = context;
        this.userReportModelList = userReportModelList;
    }


    @NonNull
    @Override
    public ReportUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_user_report, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ReportUserAdapter.ViewHolder holder, int position) {
        holder.tv_username.setText(userReportModelList.get(position).getUsername1());
        holder.tv_title.setText(userReportModelList.get(position).getTitle());

        Glide.with(context)
                .load(userReportModelList.get(position).getPhoto_profile1())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .into(holder.iv_profile);

        holder.tv_date.setText(userReportModelList.get(position).getDate());


    }




    @Override
    public int getItemCount() {
        return userReportModelList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

        TextView tv_username, tv_title, tv_date;
        ImageView iv_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_title = itemView.findViewById(R.id.tv_title);
            tv_username =  itemView.findViewById(R.id.tv_username);
            iv_profile = itemView.findViewById(R.id.iv_user);
            tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Fragment fragment = new DetailUserReport();
            Bundle bundle = new Bundle();
            bundle.putString("report_id", userReportModelList.get(getAdapterPosition()).getReport_id());
            bundle.putString("user_id1", userReportModelList.get(getAdapterPosition()).getUser_id());
            bundle.putString("user_id2", userReportModelList.get(getAdapterPosition()).getUser_id_report());
            bundle.putString("username1", userReportModelList.get(getAdapterPosition()).getUsername1());
            bundle.putString("username2", userReportModelList.get(getAdapterPosition()).getUsername2());
            bundle.putString("photo_profile1", userReportModelList.get(getAdapterPosition()).getPhoto_profile1());
            bundle.putString("photo_profile2", userReportModelList.get(getAdapterPosition()).getPhoto_profile2());
            bundle.putString("image", userReportModelList.get(getAdapterPosition()).getImage());
            bundle.putString("report", userReportModelList.get(getAdapterPosition()).getReport());
            bundle.putString("title", userReportModelList.get(getAdapterPosition()).getTitle());
            bundle.putString("date", userReportModelList.get(getAdapterPosition()).getDate());
            bundle.putString("time", userReportModelList.get(getAdapterPosition()).getTime());
            bundle.putString("email1", userReportModelList.get(getAdapterPosition()).getEmail1());
            bundle.putString("email2", userReportModelList.get(getAdapterPosition()).getEmail2());
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_admin, fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);


        }
    }
}
