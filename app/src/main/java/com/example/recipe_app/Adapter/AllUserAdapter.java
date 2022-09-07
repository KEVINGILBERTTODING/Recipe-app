package com.example.recipe_app.Adapter;

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
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder> {

    Context context;
    List<ProfileModel> profileModelList;


    public AllUserAdapter(Context context, List<ProfileModel> profileModelList) {
        this.context = context;
        this.profileModelList = profileModelList;
    }

    @NonNull
    @Override
    public AllUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_user_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserAdapter.ViewHolder holder, int position) {
        holder.tv_username.setText(profileModelList.get(position).getUsername());
        holder.tv_email.setText(profileModelList.get(position).getEmail());

        Glide.with(context)
                .load(profileModelList.get(position).getPhoto_profile())
                .override(300, 300)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_profile);


        // Itemview is clicked than send data using bundle to showProfileFragment
        holder.itemView.setOnClickListener(view -> {
            Fragment fragment = new ShowProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id", profileModelList.get(position).getUser_id());
            fragment.setArguments(bundle);

            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        });


    }

    @Override
    public int getItemCount() {
        return profileModelList.size();
    }

    public void filter(ArrayList<ProfileModel> searchUser) {
        profileModelList = searchUser;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_profile;
        TextView tv_username, tv_email;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_user);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_email = itemView.findViewById(R.id.tv_email);

           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
