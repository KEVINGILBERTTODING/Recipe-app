package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.recipe_app.Adapter.ListRoomChatAdapter;
import com.example.recipe_app.Model.ChatInterface;
import com.example.recipe_app.Model.ChatModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.ServerAPI;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomChatFragment extends Fragment {
    private ShimmerRecyclerView rvChat;
   List<ChatModel> chatModelList = new ArrayList<>();
    private ListRoomChatAdapter listRoomChatAdapter;
    LinearLayoutManager linearLayoutManager;
    private SearchView searchView;
    ChatModel chatModel;
    private String userid;
    private ImageButton btnBack, btnNewMessage;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager conMgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);

        View root = inflater.inflate(R.layout.fragment_room_chat, container, false);

        rvChat = root.findViewById(R.id.rvChat);
        btnBack = root.findViewById(R.id.btnBack);
        searchView = root.findViewById(R.id.searchView);
        btnNewMessage = root.findViewById(R.id.btnChatNew);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);

        // btn Back listener
        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        // new chat
        btnNewMessage.setOnClickListener(view -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new newChatFragment())
                    .addToBackStack(null)
                    .commit();
        });


        // fungsi searchview
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        // call method get chat room
        getRoomChat(userid);
//        getListRoom();

        // change swipe refresh layout color
        swipeRefreshLayout.setColorSchemeResources(R.color.main);

        // set swipe refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRoomChat(userid);
            }
        });



        return root;

    }

    private void getListRoom() {
        ChatInterface chatInterface = DataApi.getClient().create(ChatInterface.class);
        chatInterface.getRoomChat2(userid).enqueue(new Callback<ChatModel>() {
            @Override
            public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                if (response.isSuccessful()) {
                    chatModel = response.body();
                    Toast.makeText(getContext(), chatModel.getUserId(), Toast.LENGTH_SHORT).show();
                    chatModel.setRoomId(chatModel.getRoomId());
                }
            }

            @Override
            public void onFailure(Call<ChatModel> call, Throwable t) {

            }
        });
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
                    swipeRefreshLayout.setRefreshing(false);


                    // show shimmer
                    rvChat.showShimmer();
                    final Handler handler = new Handler();
                    handler.postAtTime(new Runnable() {
                        @Override
                        public void run() {

                            rvChat.hideShimmer();

                        }
                    }, 1200);

                } else {

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                getRoomChat(userId);
                swipeRefreshLayout.setRefreshing(true);
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

            }


        });
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


    @Override
    public void onResume() {
        super.onResume();

        rvChat.showShimmer();
        checkConnection();

    }
}