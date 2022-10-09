package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Fragment.ChatFragment;
import com.example.recipe_app.Model.ChatInterface;
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
            getUser(holder.tvUsername, chatModelList.get(position).getUserId2(), holder.ivUser, holder.icVerified);
        } else {
            getUser(holder.tvUsername, chatModelList.get(position).getUserId1(), holder.ivUser, holder.icVerified);
        }

        getLastChat(chatModelList.get(position).getRoomId(), holder.tvChat, holder.ivBlock, holder.tvDate, holder.relativeLayout);



    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public void filterList(ArrayList<ChatModel> filteredList) {
        chatModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvUsername, tvChat, tvDate;
        ImageView ivUser, icVerified, ivBlock;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivUser = itemView.findViewById(R.id.ivUser);
            icVerified = itemView.findViewById(R.id.img_verified);
            tvChat = itemView.findViewById(R.id.tvChat);
            ivBlock = itemView.findViewById(R.id.ivBlock);
            tvDate = itemView.findViewById(R.id.tvDate);
            relativeLayout = itemView.findViewById(R.id.rlChat);

//

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            Fragment fragment = new ChatFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("room_id", chatModelList.get(getAdapterPosition()).getRoomId());
            if (userid.equals(chatModelList.get(getAdapterPosition()).getUserId1())) {
                bundle.putString("user_id", chatModelList.get(getAdapterPosition()).getUserId2());
            } else {
                bundle.putString("user_id", chatModelList.get(getAdapterPosition()).getUserId1());
            }
            fragment.setArguments(bundle);

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

        }
    }

    // Method untuk mencari username dan photo profile user
    private void getUser(TextView tvUsername, String userid, ImageView ivUser, ImageView icVerified) {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getProfile(userid).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0 ) {
                    tvUsername.setText(response.body().get(0).getUsername());

                    // Load photo profile
                    Glide.with(context)
                            .load(response.body().get(0).getPhoto_profile())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .override(200, 200)
                            .into(ivUser);

                    // Show verified badge where user is verified
                    if (response.body().get(0).getVerified().equals("1")) {
                        icVerified.setVisibility(View.VISIBLE);
                    } else {
                        icVerified.setVisibility(View.GONE);
                    }

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

    // Method mengambil chat paling akhir
        private void getLastChat(Integer roomId, TextView tvChat, ImageView ivBlock, TextView tvDate, RelativeLayout rlChat) {
        ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
        ci.getMessage(roomId).enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                if (response.body().size() > 0) {


                    tvDate.setText(response.body().get(response.body().size() -1).getDate().toString());

                    if (response.body().get(response.body().size() - 1).getRemove() == 2) {
                        tvChat.setText("You deleted this message.");
                        final Typeface typeface = ResourcesCompat.getFont(context, R.font.popmedit);
                        tvChat.setTypeface(typeface);
                    }


                    else if (response.body().get(response.body().size() - 1).getRemove() == 1) {
                        ivBlock.setVisibility(View.VISIBLE);
                        final Typeface typeface = ResourcesCompat.getFont(context, R.font.popmedit);
                        tvChat.setText("You deleted this message.");
                        tvChat.setTypeface(typeface);
                        ivBlock.setBackgroundTintList( ColorStateList.valueOf(Color.parseColor("#FF0000")));

                    }

                    else {
                        tvChat.setText(response.body().get(response.body().size() - 1).getMessage());
//

                    }
                } else {
                    rlChat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();


            }
        });
    }

}
