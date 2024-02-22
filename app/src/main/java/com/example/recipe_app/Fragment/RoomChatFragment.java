package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.ui.activities.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.ui.Adapter.ListRoomChatAdapter;
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
    List<ChatModel> chatModelList = new ArrayList<>();
    private ListRoomChatAdapter listRoomChatAdapter;
    LinearLayoutManager linearLayoutManager;
    private SearchView searchView;
    ChatModel chatModel;
    private String userid;
    private ImageButton btnBack, btnNewMessage;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager conMgr;
    private RelativeLayout rlArchieved;
    private TextView tvNoChat;

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
        rlArchieved = root.findViewById(R.id.rlArchieved);
        tvNoChat = root.findViewById(R.id.tvNoMessage);

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

                filter(newText);

                return false;
            }
        });


        // call method get chat room
        getRoomChat(userid);


        // change swipe refresh layout color
        swipeRefreshLayout.setColorSchemeResources(R.color.main);

        // set swipe refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRoomChat(userid);
            }
        });

        rlArchieved.setOnClickListener(view -> {

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ArchieveRoomChat())
                    .addToBackStack(null)
                    .commit();
        });



        return root;

    }

    private void filter(String newText) {
        ArrayList<ChatModel> filteredList = new ArrayList<>();
        for (ChatModel item : chatModelList){
            if (item.getUsername2().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }

            listRoomChatAdapter.filterList(filteredList);

            if (filteredList.isEmpty()) {
                Toast.makeText(getContext(), "Username not found", Toast.LENGTH_SHORT).show();
            } else {
                listRoomChatAdapter.filterList(filteredList);
            }
        }
    }





    // Method get ChatRoom
    private void getRoomChat(String userId) {
        ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
        ci.getRoomChat(userId, 0).enqueue(new Callback<List<ChatModel>>() {
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
                    tvNoChat.setVisibility(View.GONE);
                    rvChat.setVisibility(View.VISIBLE);


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
                    rvChat.setVisibility(View.GONE);
                    tvNoChat.setVisibility(View.VISIBLE);

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