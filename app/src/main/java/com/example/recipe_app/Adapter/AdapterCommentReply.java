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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SwipeLayout;
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Fragment.UserLikeFragment;
import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.ReplyCommentModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceComment;
import com.example.recipe_app.Util.InterfaceReplyComment;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterCommentReply extends RecyclerView.Adapter<AdapterCommentReply.ViewHolder> {

    Context context;
    List<ReplyCommentModel> replyCommentModelList = new ArrayList<>();
    String username, userid;


    // Constructor
    public AdapterCommentReply(Context context, List<ReplyCommentModel> replyCommentModelList) {
        this.context = context;
        this.replyCommentModelList = replyCommentModelList;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

    }



    @NonNull
    @Override
    public AdapterCommentReply.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.list_comment_reply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCommentReply.ViewHolder holder, int position) {

        holder.tv_username.setText(replyCommentModelList.get(position).getUsername());
        holder.tv_comment.setText(replyCommentModelList.get(position).getComment());
        holder.tv_date.setText(replyCommentModelList.get(position).getComment_date());
        holder.tv_time.setText(replyCommentModelList.get(position).getComment_time());
        String replyID = replyCommentModelList.get(position).getReplyId().toString();

        // set profile image

        Glide.with(context)
                .load(replyCommentModelList.get(position).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.template_img)
                .override(200, 200)
                .into(holder.img_profile);


        // chechk if user already like the comment or not
        InterfaceReplyComment interfaceReplyComment = DataApi.getClient().create(InterfaceReplyComment.class);
        interfaceReplyComment.checkLikeComment(replyID, userid).enqueue(new Callback<List<ReplyCommentModel>>() {
            @Override
            public void onResponse(Call<List<ReplyCommentModel>> call, Response<List<ReplyCommentModel>> response) {



                if (response.body().size() > 0) {
                    holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_loved));
                } else {
                    holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_love));

                }
            }

            @Override
            public void onFailure(Call<List<ReplyCommentModel>> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });



        // Settext total likes
        // if total liks == 0 than settext empty
        if (replyCommentModelList.get(position).getLikes() == 0) {
            holder.tvLike.setText("");

        } else if (replyCommentModelList.get(position).getLikes() == 1){
            holder.tvLike.setText(replyCommentModelList.get(position).getLikes() + " Like");
        }  else{
            holder.tvLike.setText(replyCommentModelList.get(position).getLikes() + " Likes");
        }

        // If user is verified than show verified badge
        if (replyCommentModelList.get(position).getVerified().equals("1")) {
            holder.icVerified.setVisibility(View.VISIBLE);
        } else {
            holder.icVerified.setVisibility(View.GONE);
        }




        // Jika userid pemilik comment reply = user id account

        if (replyCommentModelList.get(position).getUser_id().equals(userid)) {
            holder.tv_username.setTextColor(context.getResources().getColor(R.color.blue));
            holder.swipeLayout.setSwipeEnabled(true);


        } else {
            // Do something here
            holder.swipeLayout.setSwipeEnabled(false);
        }

        // Method Button delete comment listener
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InterfaceReplyComment interfaceReplyComment = DataApi.getClient().create(InterfaceReplyComment.class);
                interfaceReplyComment.deleteCommentReply(replyCommentModelList.get(position).getReplyId())
                        .enqueue(new Callback<ReplyCommentModel>() {
                            @Override
                            public void onResponse(Call<ReplyCommentModel> call, Response<ReplyCommentModel> response) {
                                if (response.body().getSuccess().equals("1")) {
                                    Toasty.success(context, "Comment deleted", Toasty.LENGTH_SHORT).show();
                                    notifyItemRemoved(position);
                                    replyCommentModelList.remove(position);
                                    notifyItemRangeChanged(position, replyCommentModelList.size());

                                } else {
                                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReplyCommentModel> call, Throwable t) {

                                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // tv like listener
        holder.tvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UserLikeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("reply_id", replyID);
                bundle.putString("like_comment_reply", "like_comment_reply");
                fragment.setArguments(bundle);

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        // Saat  button like di klik
        holder.btnLike.setOnClickListener(view -> {
            if (holder.btnLike.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_love).getConstantState()) {

                // set background image button like
                holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_loved));

                // set animation love
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(3)
                        .playOn(holder.btnLike);

                // call method action like comment reply
                actionLikeCommentReply(
                        replyCommentModelList.get(position).getComment_id(), replyCommentModelList.get(position).getReplyId(),  userid, 1, replyCommentModelList.get(position).getRecipe_id(),
                        replyCommentModelList.get(position).getUser_id(), replyCommentModelList.get(position).getComment(), holder.tvLike
                );


            } else  if (holder.btnLike.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_loved).getConstantState()){
                // set background image button like
                holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_love));

                // call method action like comment reply
                actionLikeCommentReply(
                        replyCommentModelList.get(position).getComment_id(),replyCommentModelList.get(position).getReplyId(), userid, 0, replyCommentModelList.get(position).getRecipe_id(),
                        replyCommentModelList.get(position).getUser_id(), replyCommentModelList.get(position).getComment(), holder.tvLike

                );




            }
        });


        // set agar username dapat diklik dan direct ke show profile
        holder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ShowProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", replyCommentModelList.get(position).getUser_id());
                fragment.setArguments(bundle);

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        // set agar photo profile dapat diklik dan direct ke show profile
        holder.img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ShowProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id", replyCommentModelList.get(position).getUser_id());
                fragment.setArguments(bundle);

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });





    }

    @Override
    public int getItemCount() {
        return replyCommentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_profile, icVerified;
        TextView tv_username, tv_comment, tv_date, tv_time, tv_edited, tvLike;
        RelativeLayout list_comment;
        ImageButton btn_edit, btn_delete, btnLike;
        SwipeLayout swipeLayout;
        LinearLayout  lrDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            img_profile = itemView.findViewById(R.id.iv_profile);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            list_comment = itemView.findViewById(R.id.list_comments);
            tv_edited = itemView.findViewById(R.id.tv_edited);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            swipeLayout = itemView.findViewById(R.id.swipe_comment);
            icVerified = itemView.findViewById(R.id.img_verified);
            tvLike = itemView.findViewById(R.id.tv_like);
            btnLike = itemView.findViewById(R.id.btnLove);
            lrDelete = itemView.findViewById(R.id.lr_delete);

        }

        @Override
        public void onClick(View view) {

        }
    }


    // Method action like comment
    private void actionLikeCommentReply(String commentId, String replyId, String userid, Integer code, String recipeId, String userIdNotif, String comment,TextView tvLikes) {
        DataApi.getClient().create(InterfaceReplyComment.class).actionLikeComment(
                commentId, replyId, userid, code, recipeId, userIdNotif, comment
        ).enqueue(new Callback<ReplyCommentModel>() {
            @Override
            public void onResponse(Call<ReplyCommentModel> call, Response<ReplyCommentModel> response) {
                if (response.body().getStatus() == 1) {



//                    // count like comment

                    InterfaceReplyComment interfaceReplyComment = DataApi.getClient().create(InterfaceReplyComment.class);
                    interfaceReplyComment.countLikeCommentReply(replyId).enqueue(new Callback<List<ReplyCommentModel>>() {
                        @Override
                        public void onResponse(Call<List<ReplyCommentModel>> call, Response<List<ReplyCommentModel>> response) {

                            if (response.body().size() > 0) {
                                Integer totalLikes = response.body().get(0).getLikes();


                                if(Math.abs(totalLikes) > 1000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000 + "K" + " Likes");
                                } else if(Math.abs(totalLikes) > 1001) {
                                    tvLikes.setText(Math.abs(totalLikes)/1001 + "K+" + " Likes");
                                }
                                else if(Math.abs(totalLikes) > 1000000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000 + "M" + " Likes");
                                } else if(Math.abs(totalLikes) > 1000001){
                                    tvLikes.setText(Math.abs(totalLikes)/1000001 + "M+" + " Likes");
                                }

                                else if (Math.abs(totalLikes) > 1000000000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000000 + "B" + " Likes");
                                } else if (Math.abs(totalLikes) > 1000000001){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000001 + "B+" + " Likes");
                                }
                                else {
                                    if (totalLikes < 1) {
                                        tvLikes.setText("");
                                    }  else if (totalLikes == 1) {
                                        tvLikes.setText(Math.abs(totalLikes) + " Like");
                                    }

                                    else {
                                        tvLikes.setText(Math.abs(totalLikes) + " Likes");
                                    }



                                }

                            } else {
                                tvLikes.setText("");
                                Toasty.error(context, "Gagal", Toasty.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<List<ReplyCommentModel>> call, Throwable t) {
                            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();

                        }
                    });


                } else  {
                    // count like comment

//

                    InterfaceReplyComment interfaceReplyComment = DataApi.getClient().create(InterfaceReplyComment.class);
                    interfaceReplyComment.countLikeCommentReply(replyId).enqueue(new Callback<List<ReplyCommentModel>>() {
                        @Override
                        public void onResponse(Call<List<ReplyCommentModel>> call, Response<List<ReplyCommentModel>> response) {

                            if (response.body().size() > 0) {
                                Integer totalLikes = response.body().get(0).getLikes();


                                if(Math.abs(totalLikes) > 1000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000 + "K" + " Likes");
                                } else if(Math.abs(totalLikes) > 1001) {
                                    tvLikes.setText(Math.abs(totalLikes)/1001 + "K+" + " Likes");
                                }
                                else if(Math.abs(totalLikes) > 1000000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000 + "M" + " Likes");
                                } else if(Math.abs(totalLikes) > 1000001){
                                    tvLikes.setText(Math.abs(totalLikes)/1000001 + "M+" + " Likes");
                                }

                                else if (Math.abs(totalLikes) > 1000000000){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000000 + "B" + " Likes");
                                } else if (Math.abs(totalLikes) > 1000000001){
                                    tvLikes.setText(Math.abs(totalLikes)/1000000001 + "B+" + " Likes");
                                }
                                else {
                                    if (totalLikes < 1) {
                                        tvLikes.setText("");
                                    }  else if (totalLikes == 1) {
                                        tvLikes.setText(Math.abs(totalLikes) + " Like");
                                    }

                                    else {
                                        tvLikes.setText(Math.abs(totalLikes) + " Likes");
                                    }



                                }

                            } else {
                                tvLikes.setText("");
                                Toasty.error(context, "Gagal", Toasty.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<List<ReplyCommentModel>> call, Throwable t) {
                            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ReplyCommentModel> call, Throwable t) {
                Toasty.error(context, "Check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }

    // method check if user already like comment or dont
    private void checkLikeCommentReply(String replyId, String userId, ImageButton btnLikes) {
       InterfaceReplyComment interfaceReplyComment = DataApi.getClient().create(InterfaceReplyComment.class);
       interfaceReplyComment.checkLikeComment(replyId, userId).enqueue(new Callback<List<ReplyCommentModel>>() {
           @Override
           public void onResponse(Call<List<ReplyCommentModel>> call, Response<List<ReplyCommentModel>> response) {

               
               if (response.body().size() > 0) {
                   btnLikes.setBackground(context.getResources().getDrawable(R.drawable.ic_loved));
               } else {
                   btnLikes.setBackground(context.getResources().getDrawable(R.drawable.ic_love));

               }
           }

           @Override
           public void onFailure(Call<List<ReplyCommentModel>> call, Throwable t) {
               Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

           }
       });
    }






}
