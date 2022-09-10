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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Fragment.DetailRecipeReport;
import com.example.recipe_app.Fragment.DetailRecipeFragment;
import com.example.recipe_app.Fragment.MyProfileFragment;
import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceComment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    String username, userid;
    List<CommentModel> commentModelsList;
    Context context;
    private OnCommentLisstener onCommentLisstener;

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
        String comment = commentModelsList.get(position).getComment();
        String user_id = commentModelsList.get(position).getUser_id();


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


        // if user_id == user_id comment, maka bisa mengubah dan menghapus comment
        if (user_id.equals(userid)) {

            // Change text color if user_id == user_id comment
            holder.tv_username.setTextColor(context.getResources().getColor(R.color.main));
            holder.list_comment.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View view) {
                   onCommentLisstener.onCommentCLick(view, position);
                   return false;
               }
           });

        }



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
        ImageView img_profile;
        TextView tv_username, tv_comment, tv_date, tv_time;
        RelativeLayout list_comment;



        public ViewHolder(@NonNull View itemView) {


            super(itemView);

            img_profile = itemView.findViewById(R.id.iv_profile);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            list_comment = itemView.findViewById(R.id.list_comments);





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


}


