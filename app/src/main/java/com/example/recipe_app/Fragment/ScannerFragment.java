package com.example.recipe_app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.zxing.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerFragment extends Fragment {



    private CodeScanner mCodeScanner;
    private Button btn_show;
    private TextView tv_result;
    private ImageButton btn_back;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);



        btn_back = view.findViewById(R.id.btn_back);

        final Activity activity = getActivity();
        mCodeScanner = new CodeScanner(getContext(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // jika ada data dari bundle maka akan menjalankan method search account
                        if (getArguments() != null) {
                            getAccount(result.getText());
                        } else {
                            getRecipeScanner(result.getText());
                        }



                    }
                });
            }
        });
        
        cameraPermission();

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
            }
        });


        btn_back.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        });



        return view;
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)

            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();

    }

    // method untuk mencari recipe scanner

    private void getRecipeScanner(String recipe_id) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.getRecipeScanner(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                RecipeModel recipeModel = response.body().get(0);
                if (recipeModel.getRecipe_id().equals(recipe_id)) {

                    Toast.makeText(getContext(), "Recipe found", Toast.LENGTH_SHORT).show();

                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.layout_success_scann);
                    tv_result = dialog.findViewById(R.id.tv_result);
                    btn_show = dialog.findViewById(R.id.btn_show);



                    tv_result.setText(recipeModel.getTitle());

                    btn_show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Fragment fragment = new DetailRecipeFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("recipe_id", recipeModel.getRecipe_id());
                            bundle.putString("user_id", recipeModel.getUser_id());
                            bundle.putString("username", recipeModel.getUsername());
                            bundle.putString("title", recipeModel.getTitle());
                            bundle.putString("description", recipeModel.getDescription());
                            bundle.putString("category", recipeModel.getCategory());
                            bundle.putString("servings", recipeModel.getServings());
                            bundle.putString("duration", recipeModel.getDuration());
                            bundle.putString("ingredients", recipeModel.getIngredients());
                            bundle.putString("steps", recipeModel.getSteps());
                            bundle.putString("upload_date", recipeModel.getUpload_date());
                            bundle.putString("upload_time", recipeModel.getUpload_time());
                            bundle.putString("image", recipeModel.getImage());
                            bundle.putString("status", recipeModel.getStatus());
                            bundle.putString("ratings", recipeModel.getRatings());
                            bundle.putString("likes", recipeModel.getLikes());
                            bundle.putString("photo_profile", recipeModel.getPhoto_profile());
                            bundle.putString("email", recipeModel.getEmail());
                            bundle.putString("notes", recipeModel.getNote());

                            fragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.commit();
                            fragmentTransaction.addToBackStack(null);
                        }
                    });
                    dialog.show();



                } else {
                    Toast.makeText(getActivity(), "Recipe Not Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // getAccount
    private void getAccount(String user_id) {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getProfile(user_id).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0) {
                    Fragment fragment = new ShowProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", response.body().get(0).getUser_id());
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.addToBackStack(null);
                    ft.commit();


                } else {
                    Toast.makeText(getContext(), "User not ofund", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error check your connection", Toast.LENGTH_SHORT).show();

            }
        });

    }

}