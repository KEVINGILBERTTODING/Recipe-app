package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Model.ChatModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRoomChatAdapter extends RecyclerView.Adapter<ListRoomChatAdapter.ViewHolder> {

    Context context;
    List<ChatModel> chatModelList = new ArrayList<>();
    String username, userid;

    public ListRoomChatAdapter(Context context, List<ChatModel> chatModelList) {
        this.context = context;
        this.chatModelList = chatModelList;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);
    }


    @NonNull
    @Override
    public ListRoomChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(context).inflate(R.layout.list_room_chat, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRoomChatAdapter.ViewHolder holder, int position) {

        // get Username and photo profile
        if (userid.equals(chatModelList.get(position).getUserId1())) {
            getUser(holder.tvUsername, chatModelList.get(position).getUserId2(), holder.ivUser);
        } else {
            getUser(holder.tvUsername, chatModelList.get(position).getUserId1(), holder.ivUser);
        }



    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        ImageView ivUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivUser = itemView.findViewById(R.id.ivUser);
        }
    }

    // Method untuk mencari username dan photoprofile user
    private void getUser(TextView tvUsername, String userid, ImageView ivUser) {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getProfile(userid).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0 ) {
                    tvUsername.setText(response.body().get(0).getUsername());
                    Glide.with(context)
                            .load(response.body().get(0).getPhoto_profile())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .override(500, 600)
                            .into(ivUser);
                } else {
                    Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();


            }
        });
    }

}
