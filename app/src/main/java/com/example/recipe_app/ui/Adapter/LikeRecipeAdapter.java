package com.example.recipe_app.ui.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.ui.activities.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeRecipeAdapter extends RecyclerView.Adapter<LikeRecipeAdapter.ViewHolder> {

    Context context;
    List<RecipeModel> recipeModelList = new ArrayList<>();
    String userid;

    public LikeRecipeAdapter(Context context, List<RecipeModel> recipeModelList) {
        this.context = context;
        this.recipeModelList = recipeModelList;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);
    }

    @NonNull
    @Override
    public LikeRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.list_like_comment, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeRecipeAdapter.ViewHolder holder, int position) {

        holder.tvUsername.setText(recipeModelList.get(position).getUsername());
        String useIdx = recipeModelList.get(position).getUser_id();

        // load photo profile
        Glide.with(context)
                .load(recipeModelList.get(position).getPhoto_profile())
                .override(300, 300)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.ivProfile);

        // show badge verified if user is verified
        if (recipeModelList.get(position).getVerified().equals("1")) {
            holder.icVerified.setVisibility(View.VISIBLE);
        } else {
            holder.icVerified.setVisibility(View.GONE);
        }


        // Check if user id == user id
        if (userid.equals(recipeModelList.get(position).getUser_id())) {
            holder.btnFollow.setVisibility(View.GONE);
            holder.btnUnfollow.setVisibility(View.GONE);
        } else {

            // Check following is user alerady follow
            // if yes than show button unfollow and hide btn follow
            InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
            interfaceProfile.checkFollowing(userid, recipeModelList.get(position).getUser_id()).enqueue(new Callback<List<ProfileModel>>() {
                @Override
                public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                    if (response.body().size() > 0) {

                        holder.btnUnfollow.setVisibility(View.VISIBLE);
                        holder.btnFollow.setVisibility(View.GONE);




                    } else {
                        holder.btnFollow.setVisibility(View.VISIBLE);
                        holder.btnUnfollow.setVisibility(View.GONE);

                        // Check follow account than show button follow back
                        InterfaceProfile interfaceProfile1 = DataApi.getClient().create(InterfaceProfile.class);
                        interfaceProfile1.checkFollowers(userid, recipeModelList.get(position).getUser_id()).enqueue(new Callback<List<ProfileModel>>() {
                            @Override
                            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                                if (response.body().size() > 0) {
                                    holder.btnFollow.setText("Follow back");
                                    holder.btnFollow.setVisibility(View.VISIBLE);
                                    holder.btnUnfollow.setVisibility(View.GONE);

                                } else {
                                    holder.btnFollow.setVisibility(View.GONE);
                                    holder.btnUnfollow.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {

                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                }
            });



        }






        // Method follow account
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InterfaceProfile interfaceProfile1 = DataApi.getClient().create(InterfaceProfile.class);
                interfaceProfile1.followAccount(userid, recipeModelList.get(position).getUser_id()).enqueue(new Callback<ProfileModel>() {
                    @Override
                    public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {

                        if (response.body().getSuccess().equals("1")) {
                            holder.btnUnfollow.setVisibility(View.VISIBLE);
                            holder.btnFollow.setVisibility(View.GONE);
                            Toasty.success(context, "Followed " + recipeModelList.get(position).getUsername().toString()).show();

                        } else {
                            Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<ProfileModel> call, Throwable t) {
                        Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();


                    }
                });
            }
        });

        // Method unfollow account
        holder.btnUnfollow.setOnClickListener(view -> {

            InterfaceProfile interfaceProfile2 = DataApi.getClient().create(InterfaceProfile.class);
            interfaceProfile2.unfollAccount(userid, recipeModelList.get(position).getUser_id()).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.body().getSuccess().equals("1")) {
                        holder.btnFollow.setVisibility(View.VISIBLE);
                        holder.btnUnfollow.setVisibility(View.GONE);
                        Toasty.warning(context, "Unfollowed " + recipeModelList.get(position).getUsername().toString()).show();


                    } else  {
                        Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                }
            });

        });




    }

    @Override
    public int getItemCount() {
        return recipeModelList.size();
    }

    public void filterRecipeLike(ArrayList<RecipeModel> filterLikeRecipe) {
        recipeModelList = filterLikeRecipe;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfile, icVerified;
        TextView tvUsername;
        Button btnFollow, btnUnfollow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            ivProfile = itemView.findViewById(R.id.iv_user);
            tvUsername = itemView.findViewById(R.id.tv_username);
            icVerified = itemView.findViewById(R.id.img_verified);
            btnFollow = itemView.findViewById(R.id.btn_follow);
            btnUnfollow = itemView.findViewById(R.id.btn_unfollow);

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            Fragment fragment = new ShowProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id", recipeModelList.get(getAdapterPosition()).getUser_id());
            fragment.setArguments(bundle);

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

        }
    }





}
