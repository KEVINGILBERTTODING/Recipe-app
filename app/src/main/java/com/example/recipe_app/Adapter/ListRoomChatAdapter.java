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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.daimajia.swipe.SwipeLayout;
import com.example.recipe_app.Fragment.ChatFragment;
import com.example.recipe_app.Model.ChatInterface;
import com.example.recipe_app.Model.ChatModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;

import org.json.JSONObject;

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

        // check if user is equals user_id
        if (userid.equals(chatModelList.get(position).getUserId1())) {
            if (chatModelList.get(position).getVerified1().equals("1")) {
                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
                holder.icVerified.setVisibility(View.VISIBLE);

                // Menyembunyikan chat

                if (chatModelList.get(position).getArchieved1() == 1) {
                    holder.relativeLayout.setVisibility(View.GONE);
//                    chatModelList.remove(position);

                } else {
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                }

                // hide if status chat i archieved

                holder.btnArchieve.setOnClickListener(view -> {
                    // call archieved room chat
                    ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
                    ci.archievedRoom(chatModelList.get(position).getRoomId(), 1).enqueue(new Callback<ChatModel>() {
                        @Override
                        public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                            if (response.body().getSuccess() == 1) {
                                Toasty.normal(context, "Archieved", Toasty.LENGTH_SHORT).show();
                                chatModelList.remove(position);
                                notifyDataSetChanged();

                            } else {
                                Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChatModel> call, Throwable t) {
                            Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();


                        }
                    });
                });
            } else {
                holder.icVerified.setVisibility(View.GONE);
                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);


                if (chatModelList.get(position).getArchieved2() == 2) {
                    holder.relativeLayout.setVisibility(View.GONE);

                } else {
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                }

                holder.btnArchieve.setOnClickListener(view -> {
                    // call archieved room chat
                    // call archieved room chat
                    ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
                    ci.archievedRoom(chatModelList.get(position).getRoomId(), 2).enqueue(new Callback<ChatModel>() {
                        @Override
                        public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                            if (response.body().getSuccess() == 1) {
                                Toasty.normal(context, "Archieved", Toasty.LENGTH_SHORT).show();
                                chatModelList.remove(position);
                                notifyDataSetChanged();


                            } else {
                                Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChatModel> call, Throwable t) {
                            Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();


                        }
                    });
                });
            }


            holder.tvUsername.setText(chatModelList.get(position).getUsername2());
            Glide.with(context)
                    .load(chatModelList.get(position).getGetPhotoProfile2())
                    .override(300, 300)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(holder.ivUser);


        } else {

            if (chatModelList.get(position).getVerified1().equals("1")) {
                holder.icVerified.setVisibility(View.VISIBLE);
            } else {
                holder.icVerified.setVisibility(View.GONE);
            }


            holder.tvUsername.setText(chatModelList.get(position).getUsername1());
            Glide.with(context)
                    .load(chatModelList.get(position).getPhotoProfile1())
                    .override(300, 300)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(holder.ivUser);


        }

        // call method to get latest message
        getLastChat(chatModelList.get(position).getRoomId(), holder.tvChat, holder.ivBlock, holder.tvDate, holder.relativeLayout);

        // call method to show bubble count total new message
        getCountNewMessage(chatModelList.get(position).getRoomId(), userid, holder.tvCountNewMessage);




        // saat itemview room chat di klik

        holder.rl2.setOnClickListener(view -> {

            Fragment fragment = new ChatFragment();
            Bundle bundle = new Bundle();


            // call read all message when item view is clicked
            actionReadMessage(chatModelList.get(position).getRoomId(), userid);

            bundle.putInt("room_id", chatModelList.get(position).getRoomId());
            if (userid.equals(chatModelList.get(position).getUserId1())) {
                bundle.putString("user_id", chatModelList.get(position).getUserId2());

            } else {
                bundle.putString("user_id", chatModelList.get(position).getUserId1());

            }
            fragment.setArguments(bundle);


            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

        });

    }


    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public void filterList(ArrayList<ChatModel> filteredList) {
        chatModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvChat, tvDate, tvCountNewMessage;
        ImageView ivUser, icVerified, ivBlock;
        RelativeLayout relativeLayout, rl1, rl2;
        SwipeLayout swipeLayout;
        private ImageButton btnArchieve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivUser = itemView.findViewById(R.id.ivUser);
            icVerified = itemView.findViewById(R.id.img_verified);
            tvChat = itemView.findViewById(R.id.tvChat);
            ivBlock = itemView.findViewById(R.id.ivBlock);
            tvDate = itemView.findViewById(R.id.tvDate);
            relativeLayout = itemView.findViewById(R.id.rlChat);
            tvCountNewMessage = itemView.findViewById(R.id.tvCountNewMessage);
            swipeLayout = itemView.findViewById(R.id.swipeChat);
            btnArchieve = itemView.findViewById(R.id.btnArchieve);
            rl2 = itemView.findViewById(R.id.relative2);

        }
    }


        // Method mengambil chat paling akhir
        private void getLastChat(Integer roomId, TextView tvChat, ImageView ivBlock, TextView tvDate, RelativeLayout rlChat) {
            ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
            ci.getMessage(roomId).enqueue(new Callback<List<ChatModel>>() {
                @Override
                public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                    if (response.body().size() > 0) {


                        tvDate.setText(response.body().get(response.body().size() - 1).getDate().toString());

                        if (response.body().get(response.body().size() - 1).getRemove() == 2) {
                            tvChat.setText("You deleted this message.");
                            final Typeface typeface = ResourcesCompat.getFont(context, R.font.popmedit);
                            tvChat.setTypeface(typeface);
                        } else if (response.body().get(response.body().size() - 1).getRemove() == 1) {
                            ivBlock.setVisibility(View.VISIBLE);
                            final Typeface typeface = ResourcesCompat.getFont(context, R.font.popmedit);
                            tvChat.setText("You deleted this message.");
                            tvChat.setTypeface(typeface);
                            ivBlock.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));

                        } else {
                            tvChat.setText(response.body().get(response.body().size() - 1).getMessage());


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

        // method action read message
        private void actionReadMessage(Integer roomId, String user_id) {
            ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
            ci.actionReadMessage(roomId, user_id).enqueue(new Callback<ChatModel>() {
                @Override
                public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                    if (response.body().getSuccess() == 1) {


                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ChatModel> call, Throwable t) {
                    Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();

                }
            });
        }

        // method untuk menampilkan total pesan yang belum
        // dibaca
        private void getCountNewMessage(Integer roomId, String userId, TextView tvCountNewMessage) {
            ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
            ci.getNewMessage(roomId, userId).enqueue(new Callback<List<ChatModel>>() {
                @Override
                public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                    if (response.body().size() > 0) {
                        tvCountNewMessage.setVisibility(View.VISIBLE);
                        tvCountNewMessage.setText(response.body().size() + "");
                    } else {
                        tvCountNewMessage.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toast.LENGTH_SHORT).show();

                }
            });
        }

        // Method archieved room chat
        private void archieveRoomChat(Integer roomId, Integer code, Integer postion) {
            ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
            ci.archievedRoom(roomId, code).enqueue(new Callback<ChatModel>() {
                @Override
                public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                    if (response.body().getSuccess() == 1) {
                        Toasty.normal(context, "Archieved", Toasty.LENGTH_SHORT).show();
                        notifyItemRemoved(postion);
                        notifyDataSetChanged();
                        notifyItemChanged(postion, chatModelList.size());

                    } else {
                        Toasty.error(context, "Something went wring", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ChatModel> call, Throwable t) {
                    Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();


                }
            });
        }


    }
