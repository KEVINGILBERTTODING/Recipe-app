package com.example.recipe_app.ui.user.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.ui.activities.auth.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.ui.activities.auth.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.ui.user.Fragment.ChatFragment;
import com.example.recipe_app.Model.ChatInterface;
import com.example.recipe_app.Model.ChatModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.data.remote.DataApi;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.ViewHolder> {


    Context context;
    List<ProfileModel> profileModelList = new ArrayList<>();
    private List<ChatModel> chatModelList = new ArrayList<>();
    private String userid, username;

    public NewChatAdapter(Context context, List<ProfileModel> profileModelList) {
        this.context = context;
        this.profileModelList = profileModelList;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);
    }


    @NonNull
    @Override
    public NewChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(context).inflate(R.layout.list_followers, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatAdapter.ViewHolder holder, int position) {


        holder.tvUsername.setText(profileModelList.get(position).getUsername().toString());
        // set image
        Glide.with(context)
                .load(profileModelList.get(position).getPhoto_profile())
                .dontAnimate()
                .override(300,300)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.template_img)
                .skipMemoryCache(true)
                .into(holder.ivUser);

        // show verified badge if user is verified
        if (profileModelList.get(position).getVerified().equals("1")) {
            holder.ivVerivied.setVisibility(View.VISIBLE);

        } else {
            holder.ivVerivied.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return profileModelList.size();
    }

    public void filterList(ArrayList<ProfileModel> filteredList) {
        profileModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUsername;
        ImageView ivUser, ivVerivied;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvUsername = itemView.findViewById(R.id.tv_username);
            ivUser = itemView.findViewById(R.id.iv_user);
            ivVerivied = itemView.findViewById(R.id.img_verified);


            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {

            ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
            ci.getNewMessage(userid, profileModelList.get(getAdapterPosition()).getUser_id())
                    .enqueue(new Callback<List<ChatModel>>() {
                        @Override
                        public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                            if (response.body().size() > 0) {

                                final Fragment f = new ChatFragment();
                                final Bundle b = new Bundle();
                                b.putInt("room_id", response.body().get(0).getRoomId());

                                // send user id yang tidak sama dengan user id kita
                                if (userid.equals(response.body().get(0).getUserId1())) {
                                    b.putString("user_id", response.body().get(0).getUserId2());
                                } else {
                                    b.putString("user_id", response.body().get(0).getUserId1());
                                }

                                f.setArguments(b);

                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, f)
                                        .addToBackStack(null)
                                        .commit();


                            } else {
                                Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();


                            }
                        }

                        @Override
                        public void onFailure(Call<List<ChatModel>> call, Throwable t) {

                            Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();
                        }
                    });

        }
    }
}
