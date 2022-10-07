package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.ContentInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Model.ChatInterface;
import com.example.recipe_app.Model.ChatModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    private List<ChatModel> chatModelList = new ArrayList<>();
    String userid;

    public ChatAdapter(Context context, List<ChatModel> chatModelList) {
        this.context = context;
        this.chatModelList = chatModelList;

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.list_chat, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {

//        // settext
        if (userid.equals(chatModelList.get(position).getUserId())) {
            holder.rlChatSender.setVisibility(View.VISIBLE);
            holder.rlChatReceiver.setVisibility(View.GONE);
            holder.lrChatReceiver.setVisibility(View.GONE);
            holder.lrChatSender.setVisibility(View.VISIBLE);
            holder.rlChat.setEnabled(true);
            holder.tvChatSender.setText(chatModelList.get(position).getMessage());
            holder.tvDateSender.setText(chatModelList.get(position).getDate());
            holder.tvTImeSender.setText(chatModelList.get(position).getTime());


            // menyembunyikan chat jika remove == 2 atau jika user sendiri telah menghapus chat
            // dengan opsi delete for me

            if (chatModelList.get(position).getRemove() == 2) {
                holder.rlChatSender.setVisibility(View.GONE);
                holder.tvDateSender.setVisibility(View.GONE);
                holder.tvTImeSender.setVisibility(View.GONE);
            }


            // set agar dapat menghapus pesan
            holder.rlChat.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view, Gravity.END);
                    popupMenu.inflate(R.menu.chat_menu);
                    popupMenu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.dltForEveryone:

                                ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
                                ci.deleteMessage(chatModelList.get(position).getChatId(), 1).enqueue(new Callback<ChatModel>() {
                                    @Override
                                    public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                                        if (response.body().getSuccess() == 1) {
                                            Toasty.success(context, "Deleted", Toasty.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                            chatModelList.get(position).setRemove(1);
                                            notifyItemChanged(position);
                                            chatModelList.get(position).setMessage("You deleted this message.");
                                            holder.ivBlock.setVisibility(View.VISIBLE);
//
//                                            notifyItemRangeChanged(position, chatModelList.size());
                                        } else {
                                            Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ChatModel> call, Throwable t) {
                                        Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();

                                    }
                                });

                                Toasty.success(context, "Delete", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.dltForMe:
                                ChatInterface chatInterface = DataApi.getClient().create(ChatInterface.class);
                                chatInterface.deleteMessage(chatModelList.get(position).getChatId(), 2).enqueue(new Callback<ChatModel>() {
                                    @Override
                                    public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                                        if (response.body().getSuccess() == 1) {
                                            notifyDataSetChanged();
                                            chatModelList.remove(position);
//                                            chatModelList.get(position).setRemove(2);

                                            Toasty.success(context, "berhasil", Toasty.LENGTH_SHORT).show();


                                        } else {
                                            Toasty.error(context, "Something went wrong", Toasty.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ChatModel> call, Throwable t) {
                                        Toasty.error(context, "Please check your connection", Toasty.LENGTH_SHORT).show();


                                    }
                                });
                                break;
                        }

                        return false;
                    });

                    popupMenu.show();

                    return false;
                }

            });

            // settext kalo message telah dihapus
            if (chatModelList.get(position).getRemove() == 1) {
                holder.tvChatSender.setText("You deleted this message.");
                holder.tvChatReceiver.setText("You deleted this message.");
                holder.ivBlock.setVisibility(View.VISIBLE);

                final Typeface typeface = ResourcesCompat.getFont(context, R.font.popmedit);
                holder.tvChatSender.setTypeface(typeface);

                // Disable long click if message was deleted
                holder.rlChat.setEnabled(false);
            } else {
                holder.ivBlock.setVisibility(View.GONE);
            }


        } else {
            holder.rlChatReceiver.setVisibility(View.VISIBLE);
            holder.lrChatReceiver.setVisibility(View.VISIBLE);
            holder.rlChatSender.setVisibility(View.GONE);
            holder.lrChatSender.setVisibility(View.GONE);
            holder.rlChat.setEnabled(false);
            holder.tvChatReceiver.setText(chatModelList.get(position).getMessage());
            holder.tvDateReceiver.setText(chatModelList.get(position).getDate());
            holder.tvTimeReceiver.setText(chatModelList.get(position).getTime());

            // settext kalo message telah dihapus
            if (chatModelList.get(position).getRemove() == 1) {
                holder.tvChatSender.setText("he deleted this message.");
                holder.tvChatReceiver.setText("he deleted this message.");
                holder.ivBlock2.setVisibility(View.VISIBLE);

                final Typeface typeface = ResourcesCompat.getFont(context, R.font.popmedit);
                holder.tvChatSender.setTypeface(typeface);

                // Disable long click if message was deleted
                holder.rlChat.setEnabled(false);
            } else {
                holder.ivBlock2.setVisibility(View.GONE);

            }


        }





    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvChatSender, tvDateSender, tvTImeSender,
                tvChatReceiver, tvDateReceiver, tvTimeReceiver;
        RelativeLayout rlChatSender, rlChatReceiver, rlChat;
        LinearLayout lrChatSender, lrChatReceiver;
        ImageView ivBlock, ivBlock2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rlChat = itemView.findViewById(R.id.rlChat);

            // item sender chat
            tvChatSender = itemView.findViewById(R.id.tvChatSender);
            tvDateSender = itemView.findViewById(R.id.tvDateSender);
            tvTImeSender = itemView.findViewById(R.id.tvTimeSender);
            rlChatSender = itemView.findViewById(R.id.rlChatsender);
            lrChatSender = itemView.findViewById(R.id.lrChatSender);
            ivBlock = itemView.findViewById(R.id.imgBlock);

            // item receiver chat
            tvChatReceiver = itemView.findViewById(R.id.tvChatReceiver);
            tvDateReceiver = itemView.findViewById(R.id.tvDateReceiver);
            tvTimeReceiver = itemView.findViewById(R.id.tvTimeReceiver);
            rlChatReceiver = itemView.findViewById(R.id.rlChatreceiver);
            lrChatReceiver = itemView.findViewById(R.id.lrChatreceiver);
            ivBlock2 = itemView.findViewById(R.id.imgBlock2);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    PopupMenu popupMenu = new PopupMenu(context, view, Gravity.END);
                    popupMenu.inflate(R.menu.chat_menu);
                    popupMenu.show();


                    return false;
                }
            });


        }

        @Override
        public void onClick(View view) {

        }
    }
}
