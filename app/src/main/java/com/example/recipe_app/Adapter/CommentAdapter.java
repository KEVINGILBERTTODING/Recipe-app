package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.DefaultTaskExecutor;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.parser.IntegerParser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.swipe.SwipeLayout;
import com.example.recipe_app.Admin.Fragment.DetailRecipeReport;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Fragment.MyProfileFragment;
import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.Model.ReplyCommentModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceComment;
import com.example.recipe_app.Util.InterfaceNotification;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.example.recipe_app.Util.InterfaceReplyComment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.Hold;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.w3c.dom.Text;

import java.util.IllegalFormatCodePointException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    String username, userid;
    List<CommentModel> commentModelsList;
    Context context;
    private OnCommentLisstener onCommentLisstener;
    AdapterCommentReply adapterCommentReply;
    private LinearLayoutManager linearLayoutManager;
    List<ReplyCommentModel> replyCommentModelList;

    public CommentAdapter(Context context, List<CommentModel> commentModelsList) {
        this.commentModelsList = commentModelsList;
        this.context = context;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

        holder.tv_username.setText(commentModelsList.get(position).getUsername());
        holder.tv_comment.setText(commentModelsList.get(position).getComment());
        holder.tv_date.setText(commentModelsList.get(position).getComment_date());
        holder.tv_time.setText(commentModelsList.get(position).getComment_time());
        String recipe_id = commentModelsList.get(position).getRecipe_id();
        String user_id = commentModelsList.get(position).getUser_id();
        String commentId = commentModelsList.get(position).getComment_id();

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);


        // Check if user already like comment or dont
        checkLikeComment(commentModelsList.get(position).getComment_id(), userid, holder.btnLike);


        // check if total like comment == 0 than hide settext ""
        if (commentModelsList.get(position).getLikes() == 0) {
            holder.tvLike.setText("");

        } else {
            holder.tvLike.setText(commentModelsList.get(position).getLikes() + " Likes");
        }



        // set agar tv username dan foto profile dapat di klik di detail recipe fragment
        holder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommentLisstener.onCommentCLick(view, position);
            }
        });

        holder.img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommentLisstener.onCommentCLick(view, position);
            }
        });


        // set agar tv reply dapat diklik di detail recipe fragment
        holder.tvReply.setOnClickListener(view -> {
            onCommentLisstener.onCommentCLick(view, position);
        });



        // If user is verified than show verified badge
        if (commentModelsList.get(position).getVerified().equals("1")) {
            holder.icVerified.setVisibility(View.VISIBLE);
        } else {
            holder.icVerified.setVisibility(View.GONE);
        }


        holder.btnLike.setOnClickListener(view -> {
            if (holder.btnLike.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_love).getConstantState()) {
                holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_loved));
                actionLikeComment(
                        commentModelsList.get(position).getComment_id(), userid, 1, commentModelsList.get(position).getRecipe_id(),
                        commentModelsList.get(position).getUser_id(), commentModelsList.get(position).getComment(), holder.tvLike
                );


            } else  if (holder.btnLike.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_loved).getConstantState()){
                holder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_love));
                actionLikeComment(
                        commentModelsList.get(position).getComment_id(), userid, 0, commentModelsList.get(position).getRecipe_id(),
                        commentModelsList.get(position).getUser_id(), commentModelsList.get(position).getComment(), holder.tvLike

                );




            }
        });




        // If comment is edited than show text "edited"
        if (commentModelsList.get(position).getEdited().equals("1")) {
            holder.tv_edited.setVisibility(View.VISIBLE);
        } else{
            holder.tv_edited.setVisibility(View.GONE);
        }


        Glide.with(context)
                .load(commentModelsList.get(position).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.template_img)
                .override(200, 200)
                .into(holder.img_profile);


        // set enable swipe layout

        holder.swipeLayout.setSwipeEnabled(false);



        // Mengambil userid pemilik recipe
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.getRecipe(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.body().size() > 0) {
                    // if user_id == user_id comment, maka bisa mengubah dan menghapus comment
                    if (user_id.equals(userid)) {

                        // Change text color if user_id == user_id comment
                        holder.tv_username.setTextColor(context.getResources().getColor(R.color.blue));

                        // set agar btn edit dapat diklik di detailrecipefragment
                        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onCommentLisstener.onCommentCLick(view, position);
                            }
                        });

                        // set agar btn delete dapat diklik di detailrecipefragment
                        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onCommentLisstener.onCommentCLick(view, position);
                            }
                        });

                        // Jika ingin long click listener
//                        holder.list_comment.setOnLongClickListener(new View.OnLongClickListener() {
//                            @Override
//                            public boolean onLongClick(View view) {
//                                onCommentLisstener.onCommentCLick(view, position);
//                                return false;
//                            }
//                        });


                        // active kan swipe layout
                        holder.swipeLayout.setSwipeEnabled(true);

                    } else if (response.body().get(0).getUser_id().equals(userid)) {

                        // set agar btn delete dapat diklik di detailrecipefragment
                        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onCommentLisstener.onCommentCLick(view, position);
                            }
                        });


                        // Hide edit comment button
                        holder.lrEdit.setVisibility(View.GONE);

                        // active swipe comment to edit or delete option
                        holder.swipeLayout.setSwipeEnabled(true);
                    }


                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

            }
        });



        // Count total reply
        // and hide linear layout reply if thers no reply comment

        InterfaceReplyComment interfaceReplyComment = DataApi.getClient().create(InterfaceReplyComment.class);
        interfaceReplyComment.getReplyComment(commentId).enqueue(new Callback<List<ReplyCommentModel>>() {
            @Override
            public void onResponse(Call<List<ReplyCommentModel>> call, Response<List<ReplyCommentModel>> response) {
                if (response.body().size() > 0) {
                    holder.lrReply.setVisibility(View.VISIBLE);
                    holder.tvCountReply.setText(response.body().size() + " Reply");

                } else {

                    holder.lrReply.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ReplyCommentModel>> call, Throwable t) {
                Toasty.error(context, "gagal mas brooo", Toasty.LENGTH_SHORT).show();

            }
        });











        holder.tvCountReply.setOnClickListener(view -> {



            InterfaceReplyComment interfaceComment1 = DataApi.getClient().create(InterfaceReplyComment.class);
            interfaceComment1.getReplyComment(commentId).enqueue(new Callback<List<ReplyCommentModel>>() {
                @Override
                public void onResponse(Call<List<ReplyCommentModel>> call, Response<List<ReplyCommentModel>> response) {
                    replyCommentModelList = response.body();
                    if (response.body().size() > 0 ) {
                        adapterCommentReply = new AdapterCommentReply(context, replyCommentModelList);
                        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        holder.rvReplyComment.setAdapter(adapterCommentReply);
                        holder.rvReplyComment.setLayoutManager(linearLayoutManager);
                        holder.rvReplyComment.setVisibility(View.VISIBLE);

                    } else {
                        holder.rvReplyComment.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<List<ReplyCommentModel>> call, Throwable t) {

                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();
                }
            });






        });







    }

    @Override
    public int getItemCount() {
        return commentModelsList.size();
    }

    public void setOnCommentListener(DetailRecipeFragment detailRecipeFragment) {
        this.onCommentLisstener = detailRecipeFragment;

    }

    public interface OnCommentLisstener {
        void onCommentCLick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_profile, icVerified;
        TextView tv_username, tv_comment, tv_date, tv_time, tv_edited, tvLike, tvCountReply, tvReply;
        RelativeLayout list_comment;
        ImageButton btn_edit, btn_delete, btnLike;
        SwipeLayout swipeLayout;
        LinearLayout lrEdit, lrReply;
        ShimmerRecyclerView rvReplyComment;



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
            lrEdit = itemView.findViewById(R.id.lr_edit);
            icVerified = itemView.findViewById(R.id.img_verified);
            tvLike = itemView.findViewById(R.id.tv_like);
            btnLike = itemView.findViewById(R.id.btnLove);
            rvReplyComment = itemView.findViewById(R.id.recycler_reply_comment);
            tvCountReply = itemView.findViewById(R.id.tv_count_reply_comment);
            lrReply = itemView.findViewById(R.id.lr_count_reply);
            tvReply = itemView.findViewById(R.id.tv_reply);












        }

        @Override
        public void onClick(View view) {
            if (onCommentLisstener!= null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onCommentLisstener.onCommentCLick(view, position);
                }
            }


        }
    }

    // Method action like comment
    private void actionLikeComment(String commentId, String userid, Integer code, String recipeId, String userIdNotif, String comment,TextView tvLikes) {
        DataApi.getClient().create(InterfaceComment.class).actionLikeComment(
                commentId, userid, code, recipeId, userIdNotif, comment
        ).enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.body().getStatus() == 1) {


                    // count like comment
                    countLikeComment(commentId, tvLikes);


                } else  {
                    // count like comment

                    countLikeComment(commentId, tvLikes);

                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                Toasty.error(context, "Check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }

    // method check if user already like comment or dont
    private void checkLikeComment(String commentId, String userId, ImageButton btnLikes) {
        DataApi.getClient().create(InterfaceComment.class).checkLikeComment(commentId, userId).enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if (response.body().size() > 0) {
                    btnLikes.setBackground(context.getResources().getDrawable(R.drawable.ic_loved));
                } else {
                    btnLikes.setBackground(context.getResources().getDrawable(R.drawable.ic_love));
                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }


    // Count like comment
    private void countLikeComment(String commentId, TextView tvLikes) {
        DataApi.getClient().create(InterfaceComment.class).countLikeComment(commentId).enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {

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
                        } else {
                            tvLikes.setText(Math.abs(totalLikes) + " Likes");
                        }



                    }

                } else {
                    tvLikes.setText("");
                }


            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

            }
        });
    }




}
