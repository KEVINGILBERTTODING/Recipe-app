package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    List<CommentModel> commentModelsList;
    Context context;

    public CommentAdapter(Context context, List<CommentModel> commentModelsList) {
        this.commentModelsList = commentModelsList;
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return commentModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile;
        TextView tv_username, tv_comment, tv_date, tv_time;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);

            img_profile = itemView.findViewById(R.id.iv_profile);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);

        }
    }
}


