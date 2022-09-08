package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

        String following_id = profileModelList.get(position).getFollowing_id().toString();

        if (userid.equals(following_id)) {
            holder.btn_unfollow.setVisibility(View.GONE);
            holder.btn_follow.setVisibility(View.GONE);
        } else  {
            //         check if user id have follow than show button following
        InterfaceProfile interfaceProfile =  DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.checkFollowing(userid, profileModelList.get(position).getFollowing_id().toString()).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0 ) {


                    holder.btn_follow.setVisibility(View.GONE);
                    holder.btn_unfollow.setVisibility(View.VISIBLE);




                    holder.btn_unfollow.setOnClickListener(view -> {
                        InterfaceProfile interfaceProfile1 = DataApi.getClient().create(InterfaceProfile.class);
                        interfaceProfile1.unfollAccount(userid, profileModelList.get(position).getFollowing_id().toString()).enqueue(new Callback<ProfileModel>() {
                            @Override
                            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                                if (response.body().getSuccess().equals("1")) {
                                    holder.btn_unfollow.setVisibility(View.GONE);
                                    holder.btn_follow.setVisibility(View.VISIBLE);
                                }

                                else {
                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ProfileModel> call, Throwable t) {


                            }
                        });
                    });



                } else {
                    holder.btn_follow.setVisibility(View.VISIBLE);
                    holder.btn_unfollow.setVisibility(View.GONE);

                    holder.btn_follow.setOnClickListener(view -> {
                        InterfaceProfile interfaceProfile1 =  DataApi.getClient().create(InterfaceProfile.class);
                        interfaceProfile1.followAccount(userid, profileModelList.get(position).getFollowing_id().toString()).enqueue(new Callback<ProfileModel>() {
                            @Override
                            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                                if (response.body().getSuccess().equals("1")) {
                                    holder.btn_follow.setVisibility(View.GONE);
                                    holder.btn_unfollow.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<ProfileModel> call, Throwable t) {

                            }
                        });
                    });


                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toast.makeText(context, "Error no connection", Toast.LENGTH_SHORT).show();

            }
        });
        }
















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
        Button btn_follow, btn_unfollow;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_user);
            tv_username = itemView.findViewById(R.id.tv_username);
            btn_unfollow = itemView.findViewById(R.id.btn_unfollow);
            btn_follow = itemView.findViewById(R.id.btn_follow);

          itemView.setOnClickListener(this);



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
