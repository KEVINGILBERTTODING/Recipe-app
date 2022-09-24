package com.example.recipe_app.Adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class AdapterCommentReply extends RecyclerView.Adapter<AdapterCommentReply.ViewHolder> {

    Context context;
    List<ReplyCommentModel> replyCommentModelList;


    // Constructor
    public AdapterCommentReply(Context context, List<ReplyCommentModel> replyCommentModelList) {
        this.context = context;
        this.replyCommentModelList = replyCommentModelList;
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
        LinearLayout lrEdit;


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


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, replyCommentModelList.get(getAdapterPosition()).getComment_id(), Toast.LENGTH_SHORT).show();

        }
    }
}
