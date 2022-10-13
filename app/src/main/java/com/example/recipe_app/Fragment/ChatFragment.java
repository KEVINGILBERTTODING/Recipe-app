package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Adapter.ChatAdapter;
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

public class ChatFragment extends Fragment {

    ImageButton btnBack, btnSend;
    ImageView ivUser;
    RecyclerView rvChat;
    EditText etMessage;
    TextView tvUsername;
    private List<ChatModel> chatModelList = new ArrayList<>();
    ChatAdapter chatAdapter;
    ChatInterface chatInterface = DataApi.getClient().create(ChatInterface.class);
    LinearLayoutManager linearLayoutManager;
    private  String userid, username;
    private ConnectivityManager conMgr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);


        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        btnBack = root.findViewById(R.id.btnBack);
        btnSend = root.findViewById(R.id.btn_send);
        ivUser = root.findViewById(R.id.iv_user);
        rvChat = root.findViewById(R.id.rvChat);
        etMessage = root.findViewById(R.id.et_message);
        tvUsername = root.findViewById(R.id.tv_username);

        // btn back listener
        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });


        rvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    Toast.makeText(getContext(), "Bawah", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getContext(), "ATas", Toast.LENGTH_SHORT).show();

                }


            }

        });


        // call get message
        getMessage(getArguments().getInt("room_id"));

        // call get profile
        getProfile();

        // saat username di klik maka akan show profile
        tvUsername.setOnClickListener(view -> {
            Fragment  f = new ShowProfileFragment();
            Bundle b = new Bundle();
            b.putString("user_id", getArguments().getString("user_id"));
            f.setArguments(b);

            getFragmentManager().beginTransaction().replace(R.id.fragment_container, f)
                    .addToBackStack(null)
                    .commit();
        });


        // Hide btn send when et text is empty
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etMessage.getText().toString().equals("")) {
                    btnSend.setVisibility(View.GONE);
                } else if (etMessage.getText().toString().length() > 0) {
                    btnSend.setVisibility(View.VISIBLE);
                }

                else {
                    btnSend.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // call method post message

        btnSend.setOnClickListener(view -> {
            postMessage();
        });



        return root;
    }

    // Method load image and username
    private void getProfile() {
        InterfaceProfile ip = DataApi.getClient().create(InterfaceProfile.class);
        ip.getProfile(getArguments().getString("user_id")).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0 ) {
                    tvUsername.setText(response.body().get(0).getUsername());

                    // load photo profile
                    Glide.with(getContext())
                            .load(response.body().get(0).getPhoto_profile())
                            .override(500, 500)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(ivUser);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {

            }
        });
    }

    // method get message
    private void getMessage(Integer roomId) {
        chatInterface.getMessage(roomId).enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                chatModelList = response.body();
                if (chatModelList.size() > 0 ){
                    chatAdapter = new ChatAdapter(getContext(), chatModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvChat.setAdapter(chatAdapter);
                    rvChat.setLayoutManager(linearLayoutManager);
                    rvChat.setHasFixedSize(false);
                    rvChat.scrollToPosition(chatModelList.size() -1);
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }
    
    // get message without scrolling
    private void refreshChat(Integer roomId) {
        ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
        ci.getMessage(roomId).enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                
                if (response.body().size()> 0) {
                    chatModelList = response.body();
                    chatAdapter= new ChatAdapter(getContext(), chatModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
                    rvChat.setAdapter(chatAdapter);
                    rvChat.setLayoutManager(linearLayoutManager);
                    rvChat.setHasFixedSize(true);
                    Toast.makeText(getContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    
                } else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
               
                
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Methood post message
    private void postMessage(){
        ChatInterface ci = DataApi.getClient().create(ChatInterface.class);
        ci.postMessage(getArguments().getInt("room_id"), userid, etMessage.getText().toString())
                .enqueue(new Callback<ChatModel>() {
                    @Override
                    public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                        if (response.body().getSuccess() == 1) {
                            etMessage.setText("");
                            getMessage(getArguments().getInt("room_id"));

                        } else {
                            Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatModel> call, Throwable t) {
                        Toasty.error(getContext(), "Check your connection", Toasty.LENGTH_SHORT).show();

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
}