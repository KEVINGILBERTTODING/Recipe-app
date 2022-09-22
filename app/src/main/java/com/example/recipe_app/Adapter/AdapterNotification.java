package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.swipe.SwipeLayout;
import com.example.recipe_app.Fragment.FragmentReqVerification;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Fragment.MyProfileFragment;
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Model.NotificationModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceNotification;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.InterfaceRecipe;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {
    Context context;
    List<NotificationModel> notificationModelist = new ArrayList<>();
    String userid, username, user_id_notif;

    public AdapterNotification(Context context, List<NotificationModel> notificationModelist) {
        this.context = context;
        this.notificationModelist = notificationModelist;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

    }

    @NonNull
    @Override
    public AdapterNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotification.ViewHolder holder, int position) {

        String type = notificationModelist.get(position).getType();
        String recipe_id = notificationModelist.get(position).getRecipe_id();
        user_id_notif = notificationModelist.get(holder.getAdapterPosition()).getUser_id_notif();
        String user_idx = notificationModelist.get(holder.getAdapterPosition()).getUser_id();

        // if user is verified than show verified badge
        if (notificationModelist.get(position).getVerified().equals("1")) {
            holder.icVerified.setVisibility(View.VISIBLE);
        } else  {
            holder.icVerified.setVisibility(View.GONE);
        }

        if (type.equals("like")) {



            // Menampilkan teks liked recipe
            holder.tv_content.setText("liked your recipe.");
            holder.iv_recipe.setVisibility(View.VISIBLE);

            // Button delete notification
            holder.btn_delete.setOnClickListener(view -> {
                InterfaceNotification interfaceNotification = DataApi.getClient().create(InterfaceNotification.class);
                interfaceNotification.deleteNotification(notificationModelist.get(position).getNotif_id())
                        .enqueue(new Callback<NotificationModel>() {
                            @Override
                            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                                if (response.body().getSuccess().equals("1")) {
                                    // remove item dari notification modellist
                                    Toasty.success(context, "Success delete notification", Toasty.LENGTH_SHORT).show();
                                    notificationModelist.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, notificationModelist.size());


                                } else {
                                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<NotificationModel> call, Throwable t) {
                                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                            }
                        });
            });



            // call api untuk menampilkan gambar berdasarkan recipe id
            InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
            interfaceRecipe.getRecipe(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
                @Override
                public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                    if (response.body().size() > 0) {
                        Glide.with(context)
                                .load(response.body().get(0).getImage())
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .skipMemoryCache(true)
                                .override(300, 300)
                                .into(holder.iv_recipe);



                    } else {
                        Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                }
            });

        }

        // Jika type notif adalah follow
        else if (type.equals("follow")) {



            // maka akan menampilkan teks
            holder.tv_content.setText("started following you.");


            // Button delete notification
            holder.btn_delete.setOnClickListener(view -> {
                InterfaceNotification interfaceNotification = DataApi.getClient().create(InterfaceNotification.class);
                interfaceNotification.deleteNotification(notificationModelist.get(position).getNotif_id())
                        .enqueue(new Callback<NotificationModel>() {
                            @Override
                            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                                if (response.body().getSuccess().equals("1")) {
                                    // remove item dari notification modellist
                                    Toasty.success(context, "Success delete notification", Toasty.LENGTH_SHORT).show();
                                    notificationModelist.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, notificationModelist.size());


                                } else {
                                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<NotificationModel> call, Throwable t) {
                                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                            }
                        });
            });




            // check if user is following, jika user telah follow maka tampil button following
            // tetapi jika user belum follow maka tampil button follow
            InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
            interfaceProfile.checkFollowing(userid, notificationModelist.get(position).getUser_id()).enqueue(new Callback<List<ProfileModel>>() {
                @Override
                public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                    if (response.body().size() > 0 ) {
                        holder.btn_unfoll.setVisibility(View.VISIBLE);
                    } else {
                        holder.btn_follow.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                }


            });
        } else if(type.equals("comment")) {




            holder.tv_content.setText("Commented on your recipe:");
            holder.iv_recipe.setVisibility(View.VISIBLE);
            holder.tv_comment.setText(notificationModelist.get(position).getComment().toString() +"");
            holder.tv_comment.setVisibility(View.VISIBLE);
            holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);


            // Button delete notification
            holder.btn_delete.setOnClickListener(view -> {
                InterfaceNotification interfaceNotification = DataApi.getClient().create(InterfaceNotification.class);
                interfaceNotification.deleteNotification(notificationModelist.get(position).getNotif_id())
                        .enqueue(new Callback<NotificationModel>() {
                            @Override
                            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                                if (response.body().getSuccess().equals("1")) {
                                    // remove item dari notification modellist
                                    Toasty.success(context, "Success delete notification", Toasty.LENGTH_SHORT).show();
                                    notificationModelist.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, notificationModelist.size());


                                } else {
                                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<NotificationModel> call, Throwable t) {
                                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                            }
                        });
            });


            // call api untuk menampilkan gambar berdasarkan recipe id
            InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
            interfaceRecipe.getRecipe(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
                @Override
                public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                    if (response.body().size() > 0) {
                        Glide.with(context)
                                .load(response.body().get(0).getImage())
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .skipMemoryCache(true)
                                .override(300, 300)
                                .into(holder.iv_recipe);


                    } else {
                        Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                }
            });

        } else if (type.equals("verified")) {

            holder.tv_content.setText(R.string.notif_verified);
            holder.iv_recipe.setVisibility(View.GONE);
            holder.tv_comment.setVisibility(View.GONE);
            holder.animateVerified.setVisibility(View.VISIBLE);
            holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

            Glide.with(context)
                    .load(R.drawable.ic_verified)
                    .into(holder.ivVerified);

            // Button delete notification
            holder.btn_delete.setOnClickListener(view -> {
                InterfaceNotification interfaceNotification = DataApi.getClient().create(InterfaceNotification.class);
                interfaceNotification.deleteNotification(notificationModelist.get(position).getNotif_id())
                        .enqueue(new Callback<NotificationModel>() {
                            @Override
                            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                                if (response.body().getSuccess().equals("1")) {
                                    // remove item dari notification modellist
                                    Toasty.success(context, "Success delete notification", Toasty.LENGTH_SHORT).show();
                                    notificationModelist.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, notificationModelist.size());


                                } else {
                                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<NotificationModel> call, Throwable t) {
                                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                            }
                        });
            });


        }
        else if (type.equals("reject_verified")) {

            holder.tv_content.setText(R.string.reject_verified);
            holder.iv_recipe.setVisibility(View.VISIBLE);
            holder.tv_comment.setVisibility(View.GONE);
            holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

            holder.iv_recipe.setVisibility(View.GONE);

            // Button delete notification
            holder.btn_delete.setOnClickListener(view -> {
                InterfaceNotification interfaceNotification = DataApi.getClient().create(InterfaceNotification.class);
                interfaceNotification.deleteNotification(notificationModelist.get(position).getNotif_id())
                        .enqueue(new Callback<NotificationModel>() {
                            @Override
                            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                                if (response.body().getSuccess().equals("1")) {
                                    // remove item dari notification modellist
                                    Toasty.success(context, "Success delete notification", Toasty.LENGTH_SHORT).show();
                                    notificationModelist.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, notificationModelist.size());


                                } else {
                                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<NotificationModel> call, Throwable t) {
                                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                            }
                        });
            });


        }



        // button follow click listener
        holder.btn_follow.setOnClickListener(view -> {
            InterfaceProfile  interfaceProfile =  DataApi.getClient().create(InterfaceProfile.class);
            interfaceProfile.followAccount(userid, notificationModelist.get(position).getUser_id()).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.body().getSuccess().equals("1")) {
                        holder.btn_unfoll.setVisibility(View.VISIBLE);
                        holder.btn_follow.setVisibility(View.GONE);

                    } else {
                        Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                }
            });
        });

        // Button unfollow click listener
        holder.btn_unfoll.setOnClickListener(view -> {
            InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
            interfaceProfile.unfollAccount(userid, notificationModelist.get(position).getUser_id()).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.body().getSuccess().equals("1")) {
                        holder.btn_follow.setVisibility(View.VISIBLE);
                        holder.btn_unfoll.setVisibility(View.GONE);
                    } else {
                        Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                }
            });
        });



        holder.tv_username.setText(notificationModelist.get(position).getUsername());
        holder.tv_date.setText(notificationModelist.get(position).getDate());
        holder.tv_time.setText(notificationModelist.get(position).getTime());

        // load photo profile
        Glide.with(context)
                .load(notificationModelist.get(position).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.template_img)
                .override(300, 300)
                .into(holder.iv_user);

        // Jika user id like recipe sendiri maka akan langsung di hilangkan
        // dari notification
        if (userid.equals(user_idx)) {
            holder.rl_notification.setVisibility(View.GONE);
        } else {
            holder.rl_notification.setVisibility(View.VISIBLE);
        }






    }

    @Override
    public int getItemCount() {
        return notificationModelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_user, iv_recipe, icVerified, ivVerified;
        LottieAnimationView animateVerified;
        TextView tv_username, tv_content, tv_date, tv_time, tv_comment;
        Button btn_follow, btn_unfoll;
        RelativeLayout rl_notification;
        SwipeLayout swipeLayout;
        ImageButton btn_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            iv_recipe = itemView.findViewById(R.id.iv_recipe);
            iv_user = itemView.findViewById(R.id.iv_user);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            btn_follow = itemView.findViewById(R.id.btn_follow);
            btn_unfoll = itemView.findViewById(R.id.btn_unfollow);
            rl_notification =itemView.findViewById(R.id.rl_notification);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            swipeLayout = itemView.findViewById(R.id.swipe_notification);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            icVerified = itemView.findViewById(R.id.img_verified);
            ivVerified = itemView.findViewById(R.id.iv_verified);
            animateVerified = itemView.findViewById(R.id.animate_verified);
            rl_notification.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            
            // Jika type follow makan akan direct ke showprofile
            if (notificationModelist.get(getAdapterPosition()).getType().toString().equals("follow")) {

                Fragment fragment = new ShowProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", notificationModelist.get(getAdapterPosition()).getUser_id());
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();


            }

            // Jika type like maka kirim data ke detail recipe
            else if (notificationModelist.get(getAdapterPosition()).getType().equals("like")){
                InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
                interfaceRecipe.getRecipe(notificationModelist.get(getAdapterPosition()).getRecipe_id().toString()).enqueue(new Callback<List<RecipeModel>>() {
                    @Override
                    public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                        if (response.body().size() > 0 ) {
                            Fragment fragment =  new DetailRecipeFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("recipe_id", response.body().get(0).getRecipe_id());
                            bundle.putString("verified", response.body().get(0).getVerified());
                            bundle.putString("user_id", response.body().get(0).getUser_id_notif());
                            bundle.putString("username", response.body().get(0).getUsername());
                            bundle.putString("title", response.body().get(0).getTitle());
                            bundle.putString("description", response.body().get(0).getDescription());
                            bundle.putString("category", response.body().get(0).getCategory());
                            bundle.putString("servings", response.body().get(0).getServings());
                            bundle.putString("duration", response.body().get(0).getDuration());
                            bundle.putString("ingredients", response.body().get(0).getIngredients());
                            bundle.putString("steps", response.body().get(0).getSteps());
                            bundle.putString("upload_date", response.body().get(0).getUpload_date());
                            bundle.putString("upload_time", response.body().get(0).getUpload_time());
                            bundle.putString("image", response.body().get(0).getImage());
                            bundle.putString("status", response.body().get(0).getStatus());
                            bundle.putString("ratings", response.body().get(0).getRatings());
                            bundle.putString("likes", response.body().get(0).getLikes());
                            bundle.putString("photo_profile", response.body().get(0).getPhoto_profile());
                            bundle.putString("email", response.body().get(0).getEmail());
                            bundle.putString("notes", response.body().get(0).getNote());
                            fragment.setArguments(bundle);

                            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, fragment);
                            ft.addToBackStack(null);
                            ft.commit();


                        } else {
                            Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                        Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();

                    }
                });
            }

            // Jika type comment maka kirim data ke detail recipe dan menagarahkan pada comment
            else if (notificationModelist.get(getAdapterPosition()).getType().equals("comment")){
                InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
                interfaceRecipe.getRecipe(notificationModelist.get(getAdapterPosition()).getRecipe_id().toString()).enqueue(new Callback<List<RecipeModel>>() {
                    @Override
                    public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                        if (response.body().size() > 0 ) {
                            Fragment fragment =  new DetailRecipeFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("recipe_id", response.body().get(0).getRecipe_id());
                            bundle.putString("user_id_notif", response.body().get(0).getUser_id_notif());
                            bundle.putString("verified", response.body().get(0).getVerified());
                            bundle.putString("user_id", response.body().get(0).getUser_id());
                            bundle.putString("username", response.body().get(0).getUsername());
                            bundle.putString("title", response.body().get(0).getTitle());
                            bundle.putString("description", response.body().get(0).getDescription());
                            bundle.putString("category", response.body().get(0).getCategory());
                            bundle.putString("servings", response.body().get(0).getServings());
                            bundle.putString("duration", response.body().get(0).getDuration());
                            bundle.putString("ingredients", response.body().get(0).getIngredients());
                            bundle.putString("steps", response.body().get(0).getSteps());
                            bundle.putString("upload_date", response.body().get(0).getUpload_date());
                            bundle.putString("upload_time", response.body().get(0).getUpload_time());
                            bundle.putString("image", response.body().get(0).getImage());
                            bundle.putString("status", response.body().get(0).getStatus());
                            bundle.putString("ratings", response.body().get(0).getRatings());
                            bundle.putString("likes", response.body().get(0).getLikes());
                            bundle.putString("photo_profile", response.body().get(0).getPhoto_profile());
                            bundle.putString("email", response.body().get(0).getEmail());
                            bundle.putString("notes", response.body().get(0).getNote());
                            fragment.setArguments(bundle);

                            FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, fragment);
                            ft.addToBackStack(null);
                            ft.commit();


                        } else {
                            Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                        Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();

                    }
                });
            }

            // JIKA TYPE NOTIFIKASI ADALAH REJECT_VERIFIED

            else if (notificationModelist.get(getAdapterPosition()).getType().equals("reject_verified")) {

                // GET VERIFIED
                InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
                interfaceProfile.getProfile(user_id_notif).enqueue(new Callback<List<ProfileModel>>() {
                    @Override
                    public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                        if (response.body().get(0).getVerified().equals("1") ) {

                        } else {

                            // JIKA USER TELAH MENGAJUKAN VERIFIED MAKA NOTIFIKASI JIKA DIKLIK AKAN MUNCUL TOAST
                            InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
                            interfaceProfile.checkVerified(user_id_notif).enqueue(new Callback<List<ProfileModel>>() {
                                @Override
                                public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                                    if (response.body().size() > 0) {
                                        Toasty.warning(context, "Your verification is being processed", Toasty.LENGTH_SHORT).show();

                                    }
                                    // JIKA REQUEST DITOLAK MAKA USER DAPAT MENGAJUKAN REQUEST LAGI
                                    else {
                                        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.fragment_container, new FragmentReqVerification());
                                        ft.addToBackStack(null);
                                        ft.commit();

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                                   Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProfileModel>> call, Throwable t) {

                    }
                });




            }

            // JIKA TYPE NOTIFIKASI ADALAH VERIFIED
            else if (notificationModelist.get(getAdapterPosition()).getType().equals("verified")) {
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MyProfileFragment())
                        .addToBackStack(null)
                        .commit();

            }
            
          
        }
    }


}
