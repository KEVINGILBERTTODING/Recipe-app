package com.example.recipe_app.Admin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Admin.Model.AdminModel;
import com.example.recipe_app.Fragment.ShowProfileFragment;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;

public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.ViewHolder> {

    Context context;
    List<AdminModel> adminModelList;


    public ListUsersAdapter(Context context, List<AdminModel> adminModelList) {
        this.context = context;
        this.adminModelList = adminModelList;
    }



    @NonNull
    @Override
    public ListUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListUsersAdapter.ViewHolder holder, int position) {

        holder.tv_username.setText(adminModelList.get(position).getUsername());
        holder.tv_email.setText(adminModelList.get(position).getEmail());

        Glide.with(context)
                .load(adminModelList.get(position).getPhoto_profile())
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .into(holder.iv_user);

        holder.btnMore.setOnClickListener(view -> {
           PopupMenu popupMenu = new PopupMenu(context, holder.btnMore, Gravity.END);
           popupMenu.getMenuInflater().inflate(R.menu.list_user_admin_menu, popupMenu.getMenu());
              popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.mnu_show:
                        Fragment fragment = new ShowProfileFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", adminModelList.get(position).getUser_id());
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_admin, fragment);
                        ft.commit();
                        ft.addToBackStack(null);
                          break;
                    case R.id.mnu_disable:
                          Toast.makeText(context, "Disable user", Toast.LENGTH_SHORT).show();
                          break;
                }
                return false;
              });
              popupMenu.show();

        });

    }

    public void filterList(ArrayList<AdminModel> filteredList) {

        adminModelList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return adminModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_user;
        TextView tv_username, tv_email;
        ImageButton btnMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            iv_user = itemView.findViewById(R.id.iv_user);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_email = itemView.findViewById(R.id.tv_email);
            btnMore = itemView.findViewById(R.id.btn_more);
        }

        @Override
        public void onClick(View view) {



        }
    }
}
