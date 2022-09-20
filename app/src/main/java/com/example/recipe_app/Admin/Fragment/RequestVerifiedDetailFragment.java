package com.example.recipe_app.Admin.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Admin.Interface.InterfaceVerified;
import com.example.recipe_app.Admin.Model.VerificationModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestVerifiedDetailFragment extends Fragment {

    EditText etUsername, etFullName, etDocType, etCategory, etRegion, etLinkType, etUrl;
    ImageView ivDocument;
    Button btnAccept, btnReject;
    Integer userId;
    ImageButton btnBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_request_verified_detail, container, false);

        etUsername = root.findViewById(R.id.et_username);
        etFullName = root.findViewById(R.id.et_full_name);
        etDocType = root.findViewById(R.id.et_document_type);
        etCategory = root.findViewById(R.id.et_category);
        etRegion = root.findViewById(R.id.et_region);
        etLinkType = root.findViewById(R.id.et_link_type);
        etUrl = root.findViewById(R.id.et_link);
        btnAccept = root.findViewById(R.id.btn_accept);
        btnReject = root.findViewById(R.id.btn_reject);
        ivDocument = root.findViewById(R.id.iv_document);
        btnBack = root.findViewById(R.id.btn_back);



        // Get data from bundle
        etUsername.setText(getArguments().getString("username"));
        etFullName.setText(getArguments().getString("fullname"));
        etDocType.setText(getArguments().getString("doctype"));
        etCategory.setText(getArguments().getString("category"));
        etRegion.setText(getArguments().getString("region"));
        etLinkType.setText(getArguments().getString("linktype"));
        etUrl.setText(getArguments().getString("url"));
        userId = getArguments().getInt("user_id");


        // Load image using glide
        Glide.with(getContext())
                .load(getArguments().getString("image").toString())
                .dontAnimate()
                .into(ivDocument);

        // btn back listener
        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        // if image is click
        ivDocument.setOnClickListener(view -> {
            Fragment fragment = new FullScreenImageReport();
            Bundle bundle = new Bundle();
            bundle.putString("image", getArguments().getString("image"));
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_admin, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // bnt back listener
        btnAccept.setOnClickListener(view -> {
            acceptRequest(getArguments().getInt("user_id"));
        });

        // btn rejected listener
        btnReject.setOnClickListener(view -> {
            rejectRequest(getArguments().getInt("id"), getArguments().getInt("user_id"));
        });


        // if status == 2 than show button rejected only
        // if status == 0 (REJECTED) than show button accepted only
        if (getArguments().getInt("status") == 2) {
            btnAccept.setVisibility(View.GONE);
            btnReject.setVisibility(View.VISIBLE);
        }
        return root;
    }

    // Method accept request verified
    private void acceptRequest(Integer userId){
        InterfaceVerified iv = DataApi.getClient().create(InterfaceVerified.class);
        iv.acceptRequest(userId).enqueue(new Callback<VerificationModel>() {
            @Override
            public void onResponse(Call<VerificationModel> call, Response<VerificationModel> response) {
                VerificationModel verificationModel = response.body();
                if (verificationModel.getStatus() == 1) {
                    Toasty.success(getContext(), "Success verified user", Toasty.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                } else {
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerificationModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }

    // METHOD REJECT REQUEST
    private void rejectRequest (Integer id, Integer user_id) {
        InterfaceVerified iv = DataApi.getClient().create(InterfaceVerified.class);
        iv.rejectRequest(id, user_id).enqueue(new Callback<VerificationModel>() {
            @Override
            public void onResponse(Call<VerificationModel> call, Response<VerificationModel> response) {
                if (response.body().getStatus() == 1) {
                    Toasty.success(getContext(), "Successfully rejected", Toasty.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                } else  {
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<VerificationModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();


            }
        });
    }


}