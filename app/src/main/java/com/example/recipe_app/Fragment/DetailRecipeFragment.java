package com.example.recipe_app.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.ButtonBarLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.List;

public class DetailRecipeFragment extends Fragment {

    TextView tvRecipeName, tvRecipeIngredients, tvRecipeSteps, tvRating, tvDuration,
        tvServings, tvDescription, tvUsername, tvEmail, tvDate, tvTime, tvNotes;
    ImageView ivRecipeImage, ivProfile;
    Button  btnIngredients, btnSteps;
    ImageButton btnSave, btnBack;

    String recipe_id, user_id, recipeName, recipeIngredients, recipeSteps, recipeRating, recipeDuration,
        recipeServings, recipeDescription, recipeUsername, recipeEmail, recipeDate, recipeTime, photoProfile,
        photoRecipe, recipeNOtes;

    public DetailRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_detail_recipe, container, false);

        tvRecipeName = view.findViewById(R.id.tv_recipe_name);
        tvRecipeIngredients = view.findViewById(R.id.tv_ingredients);
        tvRecipeSteps = view.findViewById(R.id.tv_steps);
        tvRating = view.findViewById(R.id.tv_rating);
        tvDuration = view.findViewById(R.id.tv_duration);
        tvServings = view.findViewById(R.id.tv_servings);
        tvDescription = view.findViewById(R.id.tv_description);
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        tvDate = view.findViewById(R.id.tv_date);
        tvTime = view.findViewById(R.id.tv_time);
        ivRecipeImage = view.findViewById(R.id.img_recipe);
        ivProfile = view.findViewById(R.id.iv_profile);
        btnBack = view.findViewById(R.id.btn_back);
        btnIngredients = view.findViewById(R.id.btn_ingredients);
        btnSteps = view.findViewById(R.id.btn_steps);
        tvNotes = view.findViewById(R.id.tv_notes);

//        bundle.putString("recipe_id", recipeModels.get(getAdapterPosition()).getRecipe_id());
//        bundle.putString("user_id", recipeModels.get(getAdapterPosition()).getUser_id());
//        bundle.putString("username", recipeModels.get(getAdapterPosition()).getUsername());
//        bundle.putString("title", recipeModels.get(getAdapterPosition()).getTitle());
//        bundle.putString("description", recipeModels.get(getAdapterPosition()).getDescription());
//        bundle.putString("category", recipeModels.get(getAdapterPosition()).getCategory());
//        bundle.putString("servings", recipeModels.get(getAdapterPosition()).getServings());
//        bundle.putString("duration", recipeModels.get(getAdapterPosition()).getDuration());
//        bundle.putString("ingredients", recipeModels.get(getAdapterPosition()).getIngredients());
//        bundle.putString("steps", recipeModels.get(getAdapterPosition()).getSteps());
//        bundle.putString("upload_date", recipeModels.get(getAdapterPosition()).getUpload_date());
//        bundle.putString("upload_time", recipeModels.get(getAdapterPosition()).getUpload_time());
//        bundle.putString("image", recipeModels.get(getAdapterPosition()).getImage());
//        bundle.putString("status", recipeModels.get(getAdapterPosition()).getStatus());
//        bundle.putString("ratings", recipeModels.get(getAdapterPosition()).getRatings());
//        bundle.putString("likes", recipeModels.get(getAdapterPosition()).getLikes());
//        bundle.putString("photo_profile", recipeModels.get(getAdapterPosition()).getPhoto_profile());

        recipeName = getArguments().getString("title");
        recipeIngredients = getArguments().getString("ingredients");
        recipeSteps = getArguments().getString("steps");
        recipeRating = getArguments().getString("ratings");
        recipeDuration = getArguments().getString("duration");
        recipeServings = getArguments().getString("servings");
        recipeDescription = getArguments().getString("description");
        recipeUsername = getArguments().getString("username");
        recipeEmail = getArguments().getString("email");
        recipeDate = getArguments().getString("upload_date");
        recipeTime = getArguments().getString("upload_time");
        recipe_id = getArguments().getString("recipe_id");
        user_id = getArguments().getString("user_id");
        photoRecipe = getArguments().getString("image");
        photoProfile = getArguments().getString("photo_profile");
        recipeNOtes = getArguments().getString("notes");

        tvRecipeName.setText(recipeName);
        tvRecipeIngredients.setText(recipeIngredients);
        tvRecipeSteps.setText(recipeSteps);
        tvRating.setText(recipeRating);
        tvDuration.setText(recipeDuration);
        tvServings.setText(recipeServings);
        tvDescription.setText(recipeDescription);
        tvUsername.setText(recipeUsername);
        tvEmail.setText(recipeEmail);
        tvDate.setText(recipeDate);
        tvTime.setText(recipeTime);
        tvNotes.setText(recipeNOtes);

        // Load image profile
        Glide.with(getContext())
                .load(photoProfile)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .override(100, 100)
                .into(ivProfile);

        // Load image recipe
        Glide.with(getContext())
                .load(photoRecipe)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(ivRecipeImage);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();


            }
        });


        return view;
    }

}