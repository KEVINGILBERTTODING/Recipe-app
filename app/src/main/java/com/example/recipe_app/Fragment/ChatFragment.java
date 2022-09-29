package com.example.recipe_app.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipe_app.R;

public class ChatFragment extends Fragment {

    ImageButton btnBack, btnSend;
    ImageView ivUser;
    RecyclerView rvChat;
    EditText etMessage;
    TextView tvUsername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        btnBack = root.findViewById(R.id.btnBack);
        btnSend = root.findViewById(R.id.btn_send);
        ivUser = root.findViewById(R.id.iv_user);
        rvChat = root.findViewById(R.id.rvChat);
        etMessage = root.findViewById(R.id.et_message);
        tvUsername = root.findViewById(R.id.tv_username);


        return root;
    }
}