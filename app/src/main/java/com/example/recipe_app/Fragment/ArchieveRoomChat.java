package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.recipe_app.Adapter.ListRoomChatAdapter;
import com.example.recipe_app.Adapter.ListRoomChatArchivedAdapter;
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

public class ArchieveRoomChat extends Fragment {

    ShimmerRecyclerView rvArchieverRoom;
    ImageButton btnBack;
    SwipeRefreshLayout swipeRefreshLayout;
    String userid;
    private List<ChatModel> chatModelList = new ArrayList<>();
    ListRoomChatArchivedAdapter listRoomChatAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ConnectivityManager conMgr;
    private TextView tvNoChat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_archieve_room_chat, container, false);


        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);


        // initilize
        rvArchieverRoom = root.findViewById(R.id.rvRoomChat);
        btnBack = root.findViewById(R.id.btnBack);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        tvNoChat = root.findViewById(R.id.tvNoMessage);

        // change color swipe refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.main);

        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        // call get room id
        getArchievedMessage();

        // swipe listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArchievedMessage();
            }
        });



        return root;


    }


    // Get archieved message
    private void getArchievedMessage() {
        ChatInterface chatInterface = DataApi.getClient().create(ChatInterface.class);
        chatInterface.getRoomChat(userid, 1).enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                chatModelList = response.body();
                if (chatModelList.size() > 0) {
                    listRoomChatAdapter = new ListRoomChatArchivedAdapter(getContext(), chatModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
                    rvArchieverRoom.setAdapter(listRoomChatAdapter);
                    rvArchieverRoom.setLayoutManager(linearLayoutManager);
                    rvArchieverRoom.setHasFixedSize(true);
                    rvArchieverRoom.setVisibility(View.VISIBLE);
                    tvNoChat.setVisibility(View.GONE);

                    rvArchieverRoom.showShimmer();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvArchieverRoom.hideShimmer();
                        }
                    }, 1200);


                } else {
                    rvArchieverRoom.setVisibility(View.GONE);
                    tvNoChat.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                getArchievedMessage();

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        rvArchieverRoom.showShimmer();
        checkConnection();
    }


    // method check connection
    private void checkConnection() {
        conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    &&
                    conMgr.getActiveNetworkInfo().isAvailable()
                    &&
                    conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        }
    }
}