package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    Context context;
    List<ProfileModel> profileModelList;
    String userid;

    public FollowingAdapter(Context context, List<ProfileModel> profileModelList) {
        this.context = context;
        this.profileModelList = profileModelList;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);
    }

    @NonNull
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_followers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ViewHolder holder, int position) {

        holder.tv_username.setText(profileModelList.get(holder.getAdapterPosition()).getUsername());
        Glide.with(context)
                .load(profileModelList.get(holder.getAdapterPosition()).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .into(holder.iv_profile);


        holder.btn_remove.setOnClickListener(view -> {
            InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
            interfaceProfile.removeFollowing(userid, profileModelList.get(holder.getAdapterPosition()).getFollowing_id()).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.body().getSuccess().equals("1")) {
                        profileModelList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                   

                    } else {
                        Toast.makeText(context, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toast.makeText(context, "Error no connection", Toast.LENGTH_SHORT).show();
                    Log.e("MYAPPP", t.getMessage());

                }
            });
        });




    }

    @Override
    public int getItemCount() {
        return profileModelList.size();
    }

    public void followingList(ArrayList<ProfileModel> filteredList) {
        profileModelList= filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_profile;
        TextView tv_username;
        Button btn_remove;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_user);
            tv_username = itemView.findViewById(R.id.tv_username);
            btn_remove = itemView.findViewById(R.id.btn_remove);

          itemView.setOnClickListener(this);


          // btn remove hide where userid != user id

          if (userid.equals(profileModelList.get(0).getUser_id()) ) {
              btn_remove.setVisibility(View.VISIBLE);
          } else{
              btn_remove.setVisibility(View.GONE);
          }


        }

        @Override
        public void onClick(View view) {
            Fragment fragment = new ShowProfileFragment();
            Bundle bundle =  new Bundle();
            bundle.putString("user_id", profileModelList.get(getAdapterPosition()).getFollowing_id().toString());
            fragment.setArguments(bundle);
            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    }


}
