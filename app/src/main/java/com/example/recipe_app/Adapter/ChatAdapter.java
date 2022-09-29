package com.example.recipe_app.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Model.ChatModel;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;

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
            holder.lrChatSender.setVisibility(View.VISIBLE);
            holder.tvChatSender.setText(chatModelList.get(position).getMessage());
            holder.tvDateSender.setText(chatModelList.get(position).getDate());
            holder.tvTImeSender.setText(chatModelList.get(position).getTime());
        } else {
            holder.rlChatReceiver.setVisibility(View.VISIBLE);
            holder.lrChatReceiver.setVisibility(View.VISIBLE);
            holder.tvChatReceiver.setText(chatModelList.get(position).getMessage());
            holder.tvDateReceiver.setText(chatModelList.get(position).getDate());
            holder.tvTimeReceiver.setText(chatModelList.get(position).getTime());
        }


    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvChatSender, tvDateSender, tvTImeSender,
                tvChatReceiver, tvDateReceiver, tvTimeReceiver;
        RelativeLayout rlChatSender, rlChatReceiver;
        LinearLayout lrChatSender, lrChatReceiver;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // item sender chat
            tvChatSender = itemView.findViewById(R.id.tvChatSender);
            tvDateSender = itemView.findViewById(R.id.tvDateSender);
            tvTImeSender = itemView.findViewById(R.id.tvTimeSender);
            rlChatSender = itemView.findViewById(R.id.rlChatsender);
            lrChatSender = itemView.findViewById(R.id.lrChatSender);

            // item receiver chat
            tvChatReceiver = itemView.findViewById(R.id.tvChatReceiver);
            tvDateReceiver = itemView.findViewById(R.id.tvDateReceiver);
            tvTimeReceiver = itemView.findViewById(R.id.tvTimeReceiver);
            rlChatReceiver = itemView.findViewById(R.id.rlChatreceiver);
            lrChatReceiver = itemView.findViewById(R.id.lrChatreceiver);


        }
    }
}
