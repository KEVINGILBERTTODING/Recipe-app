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
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;

public class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.ViewHolder> {


    Context context;
    List<ProfileModel> profileModelList = new ArrayList<>();

    public NewChatAdapter(Context context, List<ProfileModel> profileModelList) {
        this.context = context;
        this.profileModelList = profileModelList;
    }


    @NonNull
    @Override
    public NewChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(context).inflate(R.layout.list_followers, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatAdapter.ViewHolder holder, int position) {


        holder.tvUsername.setText(profileModelList.get(position).getUsername().toString());
        // set image
        Glide.with(context)
                .load(profileModelList.get(position).getPhoto_profile())
                .dontAnimate()
                .override(300,300)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.template_img)
                .skipMemoryCache(true)
                .into(holder.ivUser);

    }

    @Override
    public int getItemCount() {
        return profileModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        ImageView ivUser;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvUsername = itemView.findViewById(R.id.tv_username);
            ivUser = itemView.findViewById(R.id.iv_user);
        }
    }
}
