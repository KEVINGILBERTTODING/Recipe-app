package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SwipeLayout;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Fragment.SavedRecipeFragment;
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedRecipeAdapter extends RecyclerView.Adapter<SavedRecipeAdapter.ViewHolder> {

    String userid;

    private List<RecipeModel> recipeModels;
    Context context;
    InterfaceRecipe interfaceRecipe;


    // Buat constructor
    public SavedRecipeAdapter(Context context, List<RecipeModel> recipeModels) {
        this.context = context;
        this.recipeModels = recipeModels;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);
    }
    @NonNull
    @Override
    public SavedRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_saved_recipe, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipeAdapter.ViewHolder holder, int position) {

        // set image recipe
        Glide.with(context)
                .load(recipeModels.get(position).getImage())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .fitCenter()
                .centerCrop()
                .into(holder.iv_recipe);

        // set profile image
        Glide.with(context)
                .load(recipeModels.get(position).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .fitCenter()
                .centerCrop()
                .into(holder.iv_profile);

        // set name
        holder.tv_recipe_name.setText(recipeModels.get(position).getTitle());
        holder.tv_duration.setText(recipeModels.get(position).getDuration());
        holder.tv_username.setText(recipeModels.get(position).getUsername());
        holder.tv_like.setText(recipeModels.get(position).getLikes());


        // set efefect swipe layout
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        // button delete save recipe
        holder.btnDelete.setOnClickListener(view -> {
            DataApi.getClient().create(InterfaceRecipe.class).deleteSavedRecipe(recipeModels.get(position).getRecipe_id(), userid)
                    .enqueue(new retrofit2.Callback<RecipeModel>() {
                        @Override
                        public void onResponse(retrofit2.Call<RecipeModel> call, retrofit2.Response<RecipeModel> response) {
                            if (response.isSuccessful()) {

                                // Menghapus data dari list

                                recipeModels.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, recipeModels.size());

                                Toasty.success(context, "Recipe deleted", Toasty.LENGTH_SHORT).show();



                            }
                        }
                        @Override
                        public void onFailure(retrofit2.Call<RecipeModel> call, Throwable t) {
                            Toasty.error(context, "Failed to delete recipe", Toasty.LENGTH_SHORT).show();

                        }
                    });
        });


        // check apa user sudah like atau belum

        interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call2 = interfaceRecipe.getLikeRecipe(userid);
        call2.enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {

                        // jika response terdapat recipe id maka akan dicocokan dengan
                        // recipe id yang ada di list recipe model
                        if (response.body().get(i).getRecipe_id().equals(recipeModels.get(position).getRecipe_id())) {

                            // akan mengubah backgorund button telah di save
                            holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_loved));
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });

        // saat button like di klik

        holder.btnLike.setOnClickListener(view -> {
            // jika di unklik maka akan menghapus resep yang sudah di save
            if (holder.btnLike.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_loved).getConstantState()) {
                deleteLikeRecipe(recipeModels.get(position).getRecipe_id(), userid, recipeModels.get(position).getUser_id());


                // memanggil method untuk mengurangi likes jika button unlike
                countLike(recipeModels.get(position).getRecipe_id(), 2, holder.tv_like);

                // mengubah background button menjadi belum di unlike
                holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_love));

                getTotalLikes(recipeModels.get(position).getRecipe_id(), holder.tv_like);



            }

            //jika di klik maka akan menyimpan resep
            else {
                likedRecipe(recipeModels.get(position).getRecipe_id(), userid, recipeModels.get(position).getUser_id());


                // set animation love
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(1)
                        .playOn(holder.btnLike);

                // memanggil method untuk menambah likes jika button di like
                countLike(recipeModels.get(position).getRecipe_id(), 1, holder.tv_like);

                // mengubah background button jika di like
                holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_loved));


                getTotalLikes(recipeModels.get(position).getRecipe_id(), holder.tv_like);
            }
        });



        // iv recipe listener

        holder.iv_recipe.setOnClickListener(view -> {
            Fragment fragment= new DetailRecipeFragment();

            Bundle bundle = new Bundle();
            bundle.putString("recipe_id", recipeModels.get(position).getRecipe_id());
            bundle.putString("user_id", recipeModels.get(position).getUser_id());
            bundle.putString("username", recipeModels.get(position).getUsername());
            bundle.putString("title", recipeModels.get(position).getTitle());
            bundle.putString("description", recipeModels.get(position).getDescription());
            bundle.putString("category", recipeModels.get(position).getCategory());
            bundle.putString("servings", recipeModels.get(position).getServings());
            bundle.putString("duration", recipeModels.get(position).getDuration());
            bundle.putString("ingredients", recipeModels.get(position).getIngredients());
            bundle.putString("steps", recipeModels.get(position).getSteps());
            bundle.putString("upload_date", recipeModels.get(position).getUpload_date());
            bundle.putString("upload_time", recipeModels.get(position).getUpload_time());
            bundle.putString("image", recipeModels.get(position).getImage());
            bundle.putString("status", recipeModels.get(position).getStatus());
            bundle.putString("ratings", recipeModels.get(position).getRatings());
            bundle.putString("likes", recipeModels.get(position).getLikes());
            bundle.putString("photo_profile", recipeModels.get(position).getPhoto_profile());
            bundle.putString("email", recipeModels.get(position).getEmail());
            bundle.putString("notes", recipeModels.get(position).getNote());
            fragment.setArguments(bundle);
            // get Fragment

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

        });

        holder.tv_username.setOnClickListener(view -> {

            Fragment fragment= new ShowProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id", recipeModels.get(position).getUser_id());
            fragment.setArguments(bundle);
            // get Fragment
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }

    public void filterList(ArrayList<RecipeModel> filteredList) {

        recipeModels = filteredList;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_recipe, iv_profile;
        ImageButton btnSaved, btnLike, btnDelete;
        TextView tv_duration, tv_username, tv_recipe_name, tv_like;
        SwipeLayout swipeLayout;
        RelativeLayout rlList;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_recipe = itemView.findViewById(R.id.img_recipe);
            iv_profile = itemView.findViewById(R.id.img_profile);
            btnSaved = itemView.findViewById(R.id.btn_favorite);
            btnLike = itemView.findViewById(R.id.btn_like);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_username = itemView.findViewById(R.id.tv_recipe_username);
            tv_recipe_name = itemView.findViewById(R.id.tv_title);
            tv_like = itemView.findViewById(R.id.tv_like);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            rlList = itemView.findViewById(R.id.rl_list);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }


    }

    // method untuk like recipe

    private void likedRecipe(String recipeid, String useridd, String user_id_notif) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.saveLikeRecipe(recipeid, useridd, user_id_notif).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    Toasty.success(context, "Recipe liked", Toasty.LENGTH_SHORT).show();
                }
                else {

                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }

    // method untu delete like recipe

    private void deleteLikeRecipe(String recipeid, String userid , String user_id_notif) {

        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.deleteLikedRecipe(recipeid, userid, user_id_notif).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                }
                else {
                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });

    }

    private void countLike(String recipe_id, Integer code, TextView tvLikes) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.countLikeRecipe(recipe_id, code).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {

                    interfaceRecipe.getRecipe(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
                        @Override
                        public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                            if (response.body().size() > 0 ) {
                                Integer totalLikes = Integer.parseInt(response.body().get(0).getLikes());

                                if(Math.abs(totalLikes) > 1000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000 + "K");
                                } else if(Math.abs(totalLikes) > 1001) {
                                    tvLikes.setText(Math.abs(totalLikes)/1001 + "K+");
                                }
                                else if(Math.abs(totalLikes) > 1000000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000 + "M");
                                } else if(Math.abs(totalLikes) > 1000001){
                                    tvLikes.setText(Math.abs(totalLikes)/1000001 + "M+");
                                }

                                else if (Math.abs(totalLikes) > 1000000000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000000 + "B");
                                } else if (Math.abs(totalLikes) > 1000000001){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000001 + "B+");
                                }
                                else {
                                    tvLikes.setText(Math.abs(totalLikes) + "");
                                }
                            } else {
                                tvLikes.setText("0");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

                        }
                    });

                }
                else {
                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();


            }
        });
    }


    // get recipe id to get total likes
    private void getTotalLikes(String recipe_id, TextView tv_likes) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.getRecipe(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.body().size() > 0) {
                    Integer totalLikes = Integer.parseInt(response.body().get(0).getLikes());

                    if(Math.abs(totalLikes) > 1000){
                        tv_likes.setText(Math.abs(totalLikes)/1000 + "K");
                    } else if(Math.abs(totalLikes) > 1001) {
                        tv_likes.setText(Math.abs(totalLikes)/1001 + "K+");
                    }
                    else if(Math.abs(totalLikes) > 1000000){
                        tv_likes.setText(Math.abs(totalLikes)/1000000 + "M");
                    } else if(Math.abs(totalLikes) > 1000001){
                        tv_likes.setText(Math.abs(totalLikes)/1000001 + "M+");
                    }

                    else if (Math.abs(totalLikes) > 1000000000){
                        tv_likes.setText(Math.abs(totalLikes)/1000000000 + "B");
                    } else if (Math.abs(totalLikes) > 1000000001){
                        tv_likes.setText(Math.abs(totalLikes)/1000000001 + "B+");
                    }
                    else {
                        tv_likes.setText(Math.abs(totalLikes) + "");
                    }

                } else {
                    tv_likes.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

            }
        });
    }







}
