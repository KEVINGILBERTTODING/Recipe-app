package com.example.recipe_app.ui.Admin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.daimajia.swipe.SwipeLayout;
import com.example.recipe_app.ui.Admin.Fragment.RequestVerifiedDetailFragment;
import com.example.recipe_app.ui.Admin.Model.VerificationModel;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;

public class ReqVerifiedAdapter extends RecyclerView.Adapter<ReqVerifiedAdapter.ViewHolder> {
    Context context;
    List<VerificationModel> verificationModelList;

    public ReqVerifiedAdapter(Context context, List<VerificationModel> verificationModelList) {
        this.context = context;
        this.verificationModelList = verificationModelList;
    }

    @NonNull
    @Override
    public ReqVerifiedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_req_verified, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReqVerifiedAdapter.ViewHolder holder, int position) {

        holder.tvUsername.setText(verificationModelList.get(position).getUsername());
        holder.tvEmail.setText(verificationModelList.get(position).getEmail());
        holder.tvDate.setText(verificationModelList.get(position).getDate());

        if (verificationModelList.get(position).getVerified() == 1) {
            holder.icVerified.setVisibility(View.VISIBLE);
        } else {
            holder.icVerified.setVisibility(View.GONE);
        }


        // load image profile
        Glide.with(context)
                .load(verificationModelList.get(position).getPhoto_profile())
                .override(300, 300)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .dontAnimate()
                .centerCrop()
                .fitCenter()
                .into(holder.ivProfile);

    }

    @Override
    public int getItemCount() {
        return verificationModelList.size();
    }

    public void filterlist(ArrayList<VerificationModel> filteredList) {
        verificationModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivProfile, icVerified;
        TextView tvUsername, tvEmail, tvDate;
        SwipeLayout swipeLayout;
        ImageButton btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_user);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvDate = itemView.findViewById(R.id.tv_date);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            icVerified = itemView.findViewById(R.id.img_verified);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


            // SEND DATA TO Fragment
            Fragment fragment = new RequestVerifiedDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username",verificationModelList.get(getAdapterPosition()).getUsername());
            bundle.putString("fullname",verificationModelList.get(getAdapterPosition()).getFullname());
            bundle.putString("doctype",verificationModelList.get(getAdapterPosition()).getDocType());
            bundle.putString("category",verificationModelList.get(getAdapterPosition()).getCategory());
            bundle.putString("region",verificationModelList.get(getAdapterPosition()).getRegion());
            bundle.putString("linktype",verificationModelList.get(getAdapterPosition()).getType());
            bundle.putString("url",verificationModelList.get(getAdapterPosition()).getUrl());
            bundle.putString("image",verificationModelList.get(getAdapterPosition()).getImage());
            bundle.putInt("user_id",verificationModelList.get(getAdapterPosition()).getUser_id());
            bundle.putInt("id", verificationModelList.get(getAdapterPosition()).getId());
            bundle.putInt("status", verificationModelList.get(getAdapterPosition()).getStatus());
            fragment.setArguments(bundle);

            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_admin, fragment);
            ft.addToBackStack(null);
            ft.commit();



        }
    }
}
