package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.swipe.SwipeLayout;
import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.ReplyCommentModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceReplyComment;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterCommentReply extends RecyclerView.Adapter<AdapterCommentReply.ViewHolder> {

    Context context;
    List<ReplyCommentModel> replyCommentModelList;
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
}
