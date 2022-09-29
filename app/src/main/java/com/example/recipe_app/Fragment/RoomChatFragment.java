package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recipe_app.Adapter.ListRoomChatAdapter;
import com.example.recipe_app.Model.ChatInterface;
import com.example.recipe_app.Model.ChatModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomChatFragment extends Fragment {
    private ShimmerRecyclerView rvChat;
    private List<ChatModel> chatModelList = new ArrayList<>();
    private ListRoomChatAdapter listRoomChatAdapter;
    LinearLayoutManager linearLayoutManager;
    private String userid;
    private ImageButton btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);

        View root = inflater.inflate(R.layout.fragment_room_chat, container, false);

        rvChat = root.findViewById(R.id.rvChat);
        btnBack = root.findViewById(R.id.btnBack);

        // btn Back listener
        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        // call method get chat room
        getRoomChat(userid);









        return root;

    }


    // Method get ChatRoom
    private void getRoomChat(String userId) {
        ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
        ci.getRoomChat(userId).enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                chatModelList = response.body();
                if (chatModelList.size() >  0) {
                    listRoomChatAdapter = new ListRoomChatAdapter(getContext(), chatModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvChat.setAdapter(listRoomChatAdapter);
                    rvChat.setLayoutManager(linearLayoutManager);
                    rvChat.setHasFixedSize(true);

                } else {
//                    Toasty.error(getContext(), "GAGAL", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();


            }
        });
    }

}