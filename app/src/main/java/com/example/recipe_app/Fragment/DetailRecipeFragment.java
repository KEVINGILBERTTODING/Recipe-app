package com.example.recipe_app.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.ButtonBarLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.recipe_app.Adapter.CommentAdapter;
import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceComment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRecipeFragment extends Fragment  {

    TextView tvRecipeName, tvRecipeIngredients, tvRecipeSteps, tvRating, tvDuration,
            tvServings, tvDescription, tvUsername, tvEmail, tvDate, tvTime, tvNotes;
    ImageView ivRecipeImage, ivProfile;
    Button btnIngredients, btnSteps;
    ImageButton btnBack, btnSend;

    EditText et_comment;

    String recipe_id, user_id, recipeName, recipeIngredients, recipeSteps, recipeRating, recipeDuration,
            recipeServings, recipeDescription, recipeUsername, recipeEmail, recipeDate, recipeTime, photoProfile,
            photoRecipe, recipeNOtes, usernamex, useridx;


    RecyclerView recyclerView;
    private List<CommentModel> commentModelsList;
    InterfaceComment interfaceComment;
    CommentAdapter commentAdapter;
    NestedScrollView nestedScrollView;
    RelativeLayout relativeLayout;

    // Constructor
    public DetailRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_recipe, container, false);


        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        usernamex = sharedPreferences.getString(TAG_USERNAME, null);
        useridx = sharedPreferences.getString("user_id", null);

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
        recyclerView = view.findViewById(R.id.recycler_comment);
        btnSend = view.findViewById(R.id.btn_send);
        et_comment = view.findViewById(R.id.et_comment);
        nestedScrollView = view.findViewById(R.id.scroll_det_recipe);
        relativeLayout = view.findViewById(R.id.rl_comment);

        // Get data from bundle

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

        // Show ingredients

        btnIngredients.setOnClickListener(View -> {
            tvRecipeSteps.setVisibility(View.GONE);
            tvRecipeIngredients.setVisibility(View.VISIBLE);
            btnSteps.setBackgroundColor(getResources().getColor(R.color.white));
            btnSteps.setTextColor(getResources().getColor(R.color.main));
            btnIngredients.setBackgroundColor(getResources().getColor(R.color.main));
            btnIngredients.setTextColor(getResources().getColor(R.color.white));
        });

        // Show steps

        btnSteps.setOnClickListener(View -> {
            tvRecipeIngredients.setVisibility(View.GONE);
            tvRecipeSteps.setVisibility(View.VISIBLE);
            btnIngredients.setBackgroundColor(getResources().getColor(R.color.white));
            btnSteps.setBackgroundColor(getResources().getColor(R.color.main));
            btnSteps.setTextColor(getResources().getColor(R.color.white));
            btnIngredients.setTextColor(getResources().getColor(R.color.main));
        });

        getComment(recipe_id);

        btnSend.setOnClickListener(View -> {
            String comment = et_comment.getText().toString();
            if (comment.isEmpty()) {
                Toast.makeText(getContext(), "Please enter comment", Toast.LENGTH_SHORT).show();
            } else {
                postComment(useridx, recipe_id, et_comment.getText().toString());
            }
        });

        if (nestedScrollView != null) {
            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY > oldScrollY) {
                    relativeLayout.setVisibility(View.GONE);
                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            });
        }


        return view;
    }

    // Get comment
    public void getComment(String recipe_id) {

        DataApi.getClient().create(InterfaceComment.class).getComment(recipe_id).enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if (response.isSuccessful()) {
                    commentModelsList = response.body();
                    commentAdapter = new CommentAdapter(getContext(), commentModelsList);
                    recyclerView.setAdapter(commentAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                Snackbar.make(getView(), "Error no connection", Snackbar.LENGTH_SHORT).show();


            }
        });


    }

    // Post comment
    public void postComment(String user_id,  String recipe_id, String comment) {
        DataApi.getClient().create(InterfaceComment.class).createComment(user_id, recipe_id, comment).enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()) {

                    // refresh comment
                    getComment(recipe_id);
                    et_comment.setText("");

                    //show snackbar
                    Snackbar.make(getView(), "Comment added", Snackbar.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getContext(), "Error something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                Snackbar.make(getView(), "Error no connection", Snackbar.LENGTH_SHORT).show();
            }

        });
    }


}

