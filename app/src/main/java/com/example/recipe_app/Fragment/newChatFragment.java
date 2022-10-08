package com.example.recipe_app.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.recipe_app.Adapter.NewChatAdapter;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newChatFragment extends Fragment {

    ShimmerRecyclerView rvUser;
    ImageButton btnBack;
    private NewChatAdapter newChatAdapter;
    private List<ProfileModel> profileModelList;
    LinearLayoutManager linearLayoutManager;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.fragment_new_chat, container, false);

        // initilize
        btnBack = root.findViewById(R.id.btnBack);
        rvUser = root.findViewById(R.id.rvUser);


        // btn back listener
        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        // call method get all user
        getAllUser();

        return root;
    }

    // Method get all user
    private void getAllUser() {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getAllUser(1).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                profileModelList = response.body();
                if (profileModelList.size() > 0) {

                    newChatAdapter = new NewChatAdapter(getContext(), profileModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvUser.setAdapter(newChatAdapter);
                    rvUser.setLayoutManager(linearLayoutManager);
                    rvUser.setHasFixedSize(true);

                } else {
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                getAllUser();

            }
        });
    }

}