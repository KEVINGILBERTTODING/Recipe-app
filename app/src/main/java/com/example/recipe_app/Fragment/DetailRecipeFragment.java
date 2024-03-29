package com.example.recipe_app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.inputmethodservice.Keyboard;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.recipe_app.Adapter.AdapterCommentReply;
import com.example.recipe_app.Adapter.CommentAdapter;
import com.example.recipe_app.Admin.Fragment.FullScreenImageReport;
import com.example.recipe_app.Admin.Interface.InterfaceAdmin;
import com.example.recipe_app.Model.CommentModel;
import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.Model.ReplyCommentModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceComment;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.example.recipe_app.Util.InterfaceReplyComment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRecipeFragment extends Fragment implements  GestureDetector.OnDoubleTapListener, View.OnClickListener, CommentAdapter.OnCommentLisstener {

    TextView tvRecipeName, tvRecipeIngredients, tvRecipeSteps, tvDuration,
            tvServings, tvDescription, tvUsername, tvEmail, tvDate, tvTime, tvNotes, tvLikes;
    ImageView ivRecipeImage, ivProfile, ivMyProfile, ivReport, ivProfile2;
    ImageView ivVerified;

    Button btnIngredients, btnSteps;
    ImageButton btnBack, btnSend, btnFav, btnLike, btnMore, btnQrcode, btnMore2, btnRefresh;
    private List<ProfileModel> profileModels;
    private  List<ReplyCommentModel> replyCommentModelList = new ArrayList<>();
    private AdapterCommentReply adapterCommentReply;
    LottieAnimationView anim_love, save_anim, disslike_anim;
    ProgressDialog pd;
    String commentId = UUID.randomUUID().toString();
    ConnectivityManager connectivityManager;
    CommentAdapter commentAdapter;
    EditText et_comment;
    Button btnReport;
    LinearLayout lrImagePicker;
    RelativeLayout rlImagePicker;
    Dialog reportForm;
    Bitmap bitmap;
    EditText et_report, et_title;
    String image;
    private final int TAG_GALLERY = 200;

    String recipe_id, user_id, recipeName, recipeIngredients, recipeSteps, recipeRating, recipeDuration,
            recipeServings, recipeDescription, recipeUsername, recipeEmail, recipeDate, recipeTime, photoProfile,
            photoRecipe, recipeNOtes, usernamex, useridx, totalLikes, recipeStatus, recipeCategory;

    ShimmerRecyclerView recyclerView;
    private List<CommentModel> commentModelsList;
    NestedScrollView nestedScrollView;
    RelativeLayout relativeLayout, rlDummyComment;



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
        tvDuration = view.findViewById(R.id.tv_duration);
        tvServings = view.findViewById(R.id.tv_servings);
        tvDescription = view.findViewById(R.id.tv_description);
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        tvDate = view.findViewById(R.id.tv_date);
        tvTime = view.findViewById(R.id.tv_time);
        ivRecipeImage = view.findViewById(R.id.img_recipe);
        ivProfile = view.findViewById(R.id.iv_profile_recipe);
        btnBack = view.findViewById(R.id.btn_back);
        btnIngredients = view.findViewById(R.id.btn_ingredients);
        btnSteps = view.findViewById(R.id.btn_steps);
        tvNotes = view.findViewById(R.id.tv_notes);
        recyclerView = view.findViewById(R.id.recycler_comment);
        btnSend = view.findViewById(R.id.btn_send);
        et_comment = view.findViewById(R.id.et_comment);
        nestedScrollView = view.findViewById(R.id.scroll_det_recipe);
        relativeLayout = view.findViewById(R.id.rl_comment);
        ivMyProfile = view.findViewById(R.id.iv_myProfile);
        btnFav = view.findViewById(R.id.btn_fav);
        anim_love = view.findViewById(R.id.love_anim);
        save_anim = view.findViewById(R.id.saved_anim);
        btnLike = view.findViewById(R.id.btn_like);
        tvLikes = view.findViewById(R.id.tv_likes);
        disslike_anim = view.findViewById(R.id.disslike);
        btnMore = view.findViewById(R.id.btn_more);
        btnQrcode = view.findViewById(R.id.btn_qrcode);
        btnMore2 = view.findViewById(R.id.btn_more2);
        rlDummyComment = view.findViewById(R.id.rl_dummy_comment);
        ivProfile2 = view.findViewById(R.id.iv_myProfile2);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        ivVerified= view.findViewById(R.id.iv_verified);
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
        totalLikes = getArguments().getString("likes");
        recipeStatus = getArguments().getString("status");
        recipeCategory = getArguments().getString("category");


        tvRecipeName.setText(recipeName);
        tvRecipeIngredients.setText(recipeIngredients);
        tvRecipeSteps.setText(recipeSteps);
        tvDuration.setText(recipeDuration);
        tvServings.setText(recipeServings);
        tvDescription.setText(recipeDescription);
        tvUsername.setText(recipeUsername);
        tvEmail.setText(recipeEmail);
        tvDate.setText(recipeDate);
        tvTime.setText(recipeTime);
        tvNotes.setText(recipeNOtes);


        getTotalLikes(recipe_id);



        pd = new ProgressDialog(getContext());
        reportForm = new Dialog(getContext());

        rlDummyComment.setOnClickListener(view1 -> {
            relativeLayout.setVisibility(View.VISIBLE);
            et_comment.requestFocus();
            showKeyboard();
        });

        // get profile
        getProfile();


        // tvLike Listener to show like recipe
        tvLikes.setOnClickListener(view1 -> {
            Fragment fragment = new UserLikeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("recipe_id", recipe_id);
            fragment.setArguments(bundle);

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });


        // jika user id sama dengan user id maka akan muncul button edit dan delete
        if (useridx.equals(user_id)) {
            btnMore.setVisibility(View.VISIBLE);
            btnMore2.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            btnMore2.setVisibility(View.VISIBLE);
        }

        btnRefresh.setOnClickListener(view1 -> {
            YoYo.with(Techniques.RotateIn)
                    .duration(700)
                    .playOn(view.findViewById(R.id.btnRefresh));
            getComment(recipe_id);
        });

        btnMore2.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Options");
            builder.setItems(new CharSequence[]{"Report recipe"}, (dialog, which) -> {
                switch (which) {
                    case 0:
                        reportForm.setContentView(R.layout.layout_report_recipe);
                        reportForm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        lrImagePicker = reportForm.findViewById(R.id.lr_image_picker);
                        rlImagePicker = reportForm.findViewById(R.id.rl_image_picker);
                        ivReport = reportForm.findViewById(R.id.iv_report);
                        et_report = reportForm.findViewById(R.id.edt_report);
                        et_title = reportForm.findViewById(R.id.edt_title);


                        btnReport = reportForm.findViewById(R.id.btnReport);
                        lrImagePicker.setOnClickListener(view2 -> {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, TAG_GALLERY);

                            }
                        });

                        rlImagePicker.setOnClickListener(view3 -> {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, TAG_GALLERY);

                            }
                        });

                        btnReport.setOnClickListener(view2 -> {

                            // validasi button dan edittext
                            if (bitmap == null) {
                                Toast.makeText(getContext(), "Please select image first", Toast.LENGTH_SHORT).show();
                            } else if (et_report.getText().toString().isEmpty()) {
                                Toast.makeText(getContext(), "Please fill report", Toast.LENGTH_SHORT).show();
                                et_report.setError("Please fill report");
                            } else {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] bytes = byteArrayOutputStream.toByteArray();
                                image = Base64.encodeToString(bytes, Base64.DEFAULT);
                                pd.setMessage("Please wait...");
                                pd.show();
                                pd.setCancelable(false);
                                pd.setCanceledOnTouchOutside(false);
                                reportRecipe(recipe_id, useridx, et_title.getText().toString(), image, et_report.getText().toString());

                            }

                        });


                        reportForm.show();
                        break;
                }

            });

            builder.show();

        });


        // saat btn more di klik

        btnMore.setOnClickListener(View -> {
            // membuat dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose an action");
            builder.setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
                switch (which) {
                    case 0:
                        // edit recipe

                        Fragment fragment = new EditMyRecipeFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("recipe_id", recipe_id);
                        bundle.putString("title", recipeName);
                        bundle.putString("ingredients", recipeIngredients);
                        bundle.putString("steps", recipeSteps);
                        bundle.putString("ratings", recipeRating);
                        bundle.putString("duration", recipeDuration);
                        bundle.putString("servings", recipeServings);
                        bundle.putString("description", recipeDescription);
                        bundle.putString("username", recipeUsername);
                        bundle.putString("email", recipeEmail);
                        bundle.putString("upload_date", recipeDate);
                        bundle.putString("upload_time", recipeTime);
                        bundle.putString("image", photoRecipe);
                        bundle.putString("photo_profile", photoProfile);
                        bundle.putString("notes", recipeNOtes);
                        bundle.putString("likes", totalLikes);
                        bundle.putString("status", recipeStatus);
                        bundle.putString("category", recipeCategory);
                        fragment.setArguments(bundle);

                        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();


                        break;
                    case 1:
                        // delete
                        MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(getContext());
                        builder1.setTitle("Delete Recipe");
                        builder1.setMessage("Are you sure want to delete this recipe?");
                        builder1.setPositiveButton("Yes", (dialogInterface, i) -> {
                            // delete recipe
                            pd.setMessage("Deleting...");
                            pd.show();
                            deleteRecipe(recipe_id);
                        });
                        builder1.setNegativeButton("No", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        });

                        builder1.show();


                        break;
                }
            });
            builder.show();
        });

        // Saat image recipe di klik
        ivRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FullScreenImageReport();
                Bundle bundle = new Bundle();
                bundle.putString("image", image);
                fragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // show shimmer
        setShimmer();


        // load photo profile in comment
        getPhotoProfile(useridx);

        // load image recipe
        Glide.with(getContext())
                .load(photoRecipe)
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .fitCenter()
                .centerCrop()
                .into(ivRecipeImage);

        // load photo profile user
        Glide.with(getContext())
                .load(photoProfile)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .fitCenter()
                .override(300, 300)
                .into(ivProfile);

        // if btn back is click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();


            }
        });

        // jika username di klik maka akan ke profile
        tvUsername.setOnClickListener(View -> {


            // jika admin
            if (getArguments().getString("admin") != null) {

                Fragment fragment = new ShowProfileFragment();

                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                bundle.putString("admin", "admin");
                fragment.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_admin, fragment);
                ft.addToBackStack(null);
                ft.commit();


            } else {


                Fragment fragment = new ShowProfileFragment();

                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                fragment.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();


            }


        });

        // if recipe image double tap
        ivRecipeImage.setOnTouchListener(new View.OnTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if (btnLike.getBackground().getConstantState() == getResources().getDrawable(R.drawable.btn_like).getConstantState()) {

                        // menyimpan informasi like recipe ke database
                        likedRecipe(recipe_id, useridx, user_id);

                        // menambah jumlah like pada di database
                        countLike(recipe_id, 1);
                        btnLike.setBackgroundResource(R.drawable.btn_liked);
                        anim_love.setVisibility(View.VISIBLE);
                        anim_love.playAnimation();
                        getTotalLikes(recipe_id);

                        // menambah jumlah like di view
                        tvLikes.setText(String.valueOf(Integer.parseInt(tvLikes.getText().toString()) + 1));


                    } else {

                        disslike_anim.setVisibility(View.VISIBLE);
                        disslike_anim.playAnimation();

                        // mengurangi jumlah like di database
                        countLike(recipe_id, 2);

                        // mengurangi jumlah like di view
                        tvLikes.setText(String.valueOf(Integer.parseInt(tvLikes.getText().toString()) - 1));
                        btnLike.setBackgroundResource(R.drawable.btn_like);

                        // set duration animation
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                disslike_anim.setVisibility(View.GONE);
                            }
                        }, 1500);

                        deleteLikeRecipe(recipe_id, useridx, user_id);

                    }

                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    if (getArguments().getString("admin") != null) {
                        Fragment fragment = new FullScreenImageReport();
                        Bundle bundle = new Bundle();
                        bundle.putString("image", photoRecipe);
                        fragment.setArguments(bundle);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_admin, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    } else {
                        Fragment fragment = new FullScreenImageReport();
                        Bundle bundle = new Bundle();
                        bundle.putString("image", photoRecipe);
                        fragment.setArguments(bundle);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }


                    return super.onSingleTapUp(e);


                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);

                return true;
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
                Toasty.normal(getContext(), "Please fill the comment", Toasty.LENGTH_SHORT).show();
            } else {

                postComment(commentId, useridx, recipe_id, user_id, et_comment.getText().toString());
                hideKeyboard();
                relativeLayout.setVisibility(android.view.View.GONE);
            }
        });

//           Saat scrolling maka editttext comment hide
//        if (nestedScrollView != null) {
//            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//                if (scrollY > oldScrollY) {
//                    relativeLayout.setVisibility(View.GONE);
//                } else {
//                    relativeLayout.setVisibility(View.VISIBLE);
//                }
//            });
//        }

        // Jika get argument admin != null
        if (getArguments().getString("admin") != null) {
            btnFav.setVisibility(View.GONE);
            // saat btn qrcode diklik
            btnQrcode.setOnClickListener(view1 -> {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                QrcodeFragment qrcodeFragment = new QrcodeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("recipe_id", recipe_id);
                bundle.putString("recipe_name", recipeName);
                bundle.putString("verified", getArguments().getString("verified"));
                bundle.putString("recipe_image", photoRecipe);
                bundle.putString("username", recipeUsername);
                bundle.putString("admin", "admin");
                qrcodeFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_admin, qrcodeFragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            });

        } else {
            // saat btn qrcode diklik
            btnQrcode.setOnClickListener(view1 -> {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                QrcodeFragment qrcodeFragment = new QrcodeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("recipe_id", recipe_id);
                bundle.putString("verified", getArguments().getString("verified"));
                bundle.putString("recipe_name", recipeName);
                bundle.putString("recipe_image", photoRecipe);
                bundle.putString("username", recipeUsername);
                qrcodeFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, qrcodeFragment);

                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            });

        }

        // check apakah recipe sudah ada di simpan atau belum
        checkSavedRecipe(useridx, recipe_id);

        // check apakah recipe sudah ada di like atau belum
        checkLikedRecipe(useridx, recipe_id);

        // saat button save di klik
        btnFav.setOnClickListener(View -> {
            // jika di unklik maka akan menghapus resep yang sudah di save
            if (btnFav.getBackground().getConstantState() == getContext().getResources().getDrawable(R.drawable.btn_favorite).getConstantState()) {

                deleteSavedRecipe(recipe_id, useridx);
                btnFav.setBackground(getContext().getResources().getDrawable(R.drawable.btn_favorite_netral));
            }

            //jika di klik maka akan menyimpan resep
            else {

                // set animation love
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(2)
                        .playOn(btnFav);


                if (save_anim.getVisibility() == View.GONE) {
                    save_anim.setVisibility(View.VISIBLE);
                    save_anim.playAnimation();
                    saveRecipe(recipe_id, useridx);
                    btnFav.setBackground(getContext().getResources().getDrawable(R.drawable.btn_favorite));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            save_anim.setVisibility(View.GONE);
                        }
                    }, 2 * 1000); // For 2 seconds
                }

                btnFav.setBackground(getContext().getResources().getDrawable(R.drawable.btn_favorite));
            }

        });


        // saat button like di klik
        btnLike.setOnClickListener(View -> {


            // jika di unklik maka akan menghapus resep yang sudah di save
            if (btnLike.getBackground().getConstantState() == getContext().getResources().getDrawable(R.drawable.btn_liked).getConstantState()) {
                deleteLikeRecipe(recipe_id, useridx, user_id);
                countLike(recipe_id, 2);

                btnLike.setBackground(getContext().getResources().getDrawable(R.drawable.btn_like));
            }

            //jika di klik maka akan like resep
            else {
                likedRecipe(recipe_id, useridx, user_id);

                // set animation love
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(2)
                        .playOn(btnLike);


                // method untuk mengambil data like recipe
                countLike(recipe_id, 1);
                btnLike.setBackground(getContext().getResources().getDrawable(R.drawable.btn_liked));

                anim_love.setVisibility(View.VISIBLE);
                anim_love.playAnimation();

                btnLike.setBackground(getContext().getResources().getDrawable(R.drawable.btn_liked));
            }

        });


        return view;
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_comment, InputMethodManager.SHOW_IMPLICIT);
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
                    recyclerView.setHasFixedSize(false);

                    recyclerView.setItemViewType((type, position) -> {
                        switch (type) {
                            case ShimmerRecyclerView.LAYOUT_GRID:
                                return position % 2 == 0
                                        ? R.layout.template_comments
                                        : R.layout.template_comments;

                            default:
                            case ShimmerRecyclerView.LAYOUT_LIST:
                                return position == 0 || position % 2 == 0
                                        ? R.layout.template_comments
                                        : R.layout.template_comments;
                        }
                    });
                    recyclerView.showShimmer();     // to start showing shimmer
                    final Handler handler = new Handler();
                    handler.postDelayed((Runnable) () -> {
                        recyclerView.hideShimmer(); // to hide shimmer
                    }, 1000);

                    // method click dari comment adapter
                    commentAdapter.setOnCommentListener(DetailRecipeFragment.this);


                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                getComment(recipe_id);




            }
        });


    }

    // Post comment
    public void postComment(String commentId,String user_id, String recipe_id, String user_id_notif, String comment) {
        DataApi.getClient().create(InterfaceComment.class).createComment(commentId, user_id, recipe_id, user_id_notif, comment).enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {

                if (response.body().getSuccess().equals("1")) {
                    // refresh comment
                    getComment(recipe_id);
                    et_comment.setText("");

                    Toasty.success(getContext(), "Comment added", Toasty.LENGTH_SHORT).show();

                } else {
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }

        });
    }

    // get info user
    private void getProfile() {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.getProfile(user_id).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.body().size() > 0 ) {

                    // IF USER IS VERIFIED THAN SHOW VERIFIED BADGE
                    if (response.body().get(0).getVerified().equals("1")) {
                        ivVerified.setVisibility(View.VISIBLE);
                    } else {
                        ivVerified.setVisibility(View.GONE);

                    }
                } else {
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }


    // get load photo profile
    public void getPhotoProfile(String user_id) {
        DataApi.getClient().create(InterfaceProfile.class).getProfile(user_id).enqueue(new Callback<List<ProfileModel>>() {
            @Override
            public void onResponse(Call<List<ProfileModel>> call, Response<List<ProfileModel>> response) {
                if (response.isSuccessful()) {
                    profileModels = response.body();
                    photoProfile = profileModels.get(0).getPhoto_profile();
                    Glide.with(getContext())
                            .load(photoProfile)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(ivMyProfile);



                    Glide.with(getContext())
                            .load(photoProfile)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(ivProfile2);
                }
            }

            @Override
            public void onFailure(Call<List<ProfileModel>> call, Throwable t) {
                getPhotoProfile(useridx);

            }
        });
    }


    // method for if recipe is saved
    private void checkSavedRecipe(String userid, String recipeid) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getSavedRecipe(userid);
        call.enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getRecipe_id().equals(recipeid)) {
                            btnFav.setBackground(getResources().getDrawable(R.drawable.btn_favorite));
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
            }
        });
    }


    // method for save recipe
    private void saveRecipe(String recipeid, String userid) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<RecipeModel> call = interfaceRecipe.saveSavedRecipe(recipeid, userid);
        call.enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    Toasty.success(getContext(), "Recipe saved!", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        });
    }


    //   method for delete recipe
    private void deleteSavedRecipe(String recipe_id, String useridx) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<RecipeModel> call = interfaceRecipe.deleteSavedRecipe(recipe_id, useridx);
        call.enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    Toasty.warning(getContext(), "Recipe Unsaved!", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    // method for if recipe is saved
    private void checkLikedRecipe(String userid, String recipeid) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        Call<List<RecipeModel>> call = interfaceRecipe.getLikeRecipe(userid);
        call.enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i).getRecipe_id().equals(recipeid)) {
                            btnLike.setBackground(getResources().getDrawable(R.drawable.btn_liked));
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
            }
        });
    }

    // method untuk like recipe

    private void likedRecipe(String recipeid, String useridd, String user_id_notif) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.saveLikeRecipe(recipeid, useridd, user_id_notif).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {

                    interfaceRecipe.getRecipe(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
                        @Override
                        public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                            if (response.body().size() > 0 ) {

                            } else {
                                tvLikes.setText("0");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

                        }
                    });

                } else {
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    // method untuk delete like recipe

    private void deleteLikeRecipe(String recipeid, String userid, String user_id_report) {

        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.deleteLikedRecipe(recipeid, userid, user_id_report).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {



                    interfaceRecipe.getRecipe(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
                        @Override
                        public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                            if (response.body().size() > 0 ) {

                            } else {
                                tvLikes.setText("0");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

                        }
                    });

                } else {
                    Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        });

    }

    // count like
    private void countLike(String recipe_id, Integer code) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.countLikeRecipe(recipe_id, code).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    getTotalLikes(recipe_id);


                } else {

                }

            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {


            }
        });
    }


    // method untuk menghapus recipe

    private void deleteRecipe(String recipe_id) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.deleteRecipe(recipe_id).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                    Toasty.success(getContext(), "Succesfully delete recipe", Toasty.LENGTH_SHORT).show();
                    fm.popBackStack();
                } else {
                    pd.dismiss();
                    Snackbar.make(getView(), "Delete recipe failed", Snackbar.LENGTH_LONG).show();
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }

    // method untuk report recipe
    private void reportRecipe(String recipe_id, String user_id, String title, String image, String reason) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.reportRecipe(recipe_id, user_id, title, image, reason).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                RecipeModel recipeModel = response.body();
                if (recipeModel.getStatus().equals("1")) {
                    Toasty.success(getContext(), "User reported!", Toasty.LENGTH_SHORT).show();
                    pd.dismiss();
                    reportForm.dismiss();

                } else {
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
                pd.dismiss();

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == TAG_GALLERY && data != null && data.getData() != null) {

            Uri uri_path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri_path);
                ivReport.setImageBitmap(bitmap);
                rlImagePicker.setVisibility(View.VISIBLE);
                lrImagePicker.setVisibility(View.GONE);

                Snackbar.make(getView(), "Successfully load image", Snackbar.LENGTH_LONG).show();
                Toasty.success(getContext(), "Successfuly load image", Toasty.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toasty.error(getContext(), "Failed to load image", Toasty.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    // set shimmer
    private void setShimmer() {
        recyclerView.setItemViewType((type, position) -> {
            switch (type) {
                case ShimmerRecyclerView.LAYOUT_GRID:
                    return position % 2 == 0
                            ? R.layout.template_comments
                            : R.layout.template_comments;

                default:
                case ShimmerRecyclerView.LAYOUT_LIST:
                    return position == 0 || position % 2 == 0
                            ? R.layout.template_comments
                            : R.layout.template_comments;
            }
        });
        recyclerView.showShimmer();     // to start showing shimmer


    }

    @Override
    public void onResume() {
        checkConnection();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onCommentCLick(View view, int position) {
        CommentModel commentModel = commentModelsList.get(position);
        String userid_new = commentModel.getUser_id();




        // saat reply comment di klik
        switch (view.getId()) {
            case R.id.btn_reply:
                relativeLayout.setVisibility(View.VISIBLE);
                et_comment.requestFocus();
                showKeyboard();

                String reply_id = UUID.randomUUID().toString();

                btnSend.setOnClickListener(view1 -> {

                    // if field is empty
                    if (et_comment.getText().toString().isEmpty()) {
                        Toasty.error(getContext(), "Please fill the field", Toasty.LENGTH_SHORT).show();
                    } else {

                        InterfaceReplyComment interfaceReplyComment = DataApi.getClient().create(InterfaceReplyComment.class);
                        interfaceReplyComment.postCommentReply(
                                reply_id,
                                useridx,
                                commentModel.getComment_id(),
                                commentModel.getRecipe_id(),
                                commentModel.getUser_id(),
                                et_comment.getText().toString()

                        ).enqueue(new Callback<ReplyCommentModel>() {
                            @Override
                            public void onResponse(Call<ReplyCommentModel> call, Response<ReplyCommentModel> response) {
                                if (response.body().getSuccess().equals("1")) {
                                    Toasty.success(getContext(), "Success replied", Toasty.LENGTH_SHORT).show();
                                    et_comment.setText("");
                                    relativeLayout.setVisibility(View.GONE);
                                    hideKeyboard();
                                    getComment(commentModel.getRecipe_id());



                                    // if btn reply comment is click and success than reset button send to  new comment

                                    btnSend.setOnClickListener(View -> {
                                        String comment = et_comment.getText().toString();
                                        if (comment.isEmpty()) {
                                            Toasty.warning(getContext(), "Please fill comment", Toasty.LENGTH_SHORT).show();
                                        } else {
                                            postComment(commentId, useridx, recipe_id, user_id, et_comment.getText().toString());
                                            hideKeyboard();
                                        }
                                    });
                                } else {
                                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReplyCommentModel> call, Throwable t) {
                                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

                            }
                        });

                    }



                });

                break;

        }



        // muncul opsi edit dan delete jika user comment
        if (user_id.equals(userid_new) || useridx.equals(userid_new)) {




            switch (view.getId()) {


                case R.id.btn_edit:

                    relativeLayout.setVisibility(View.VISIBLE);
                    et_comment.setText(commentModel.getComment());
                    et_comment.requestFocus();
                    showKeyboard();
                    btnSend.setOnClickListener(view1 -> {
                        InterfaceComment interfaceComment = DataApi.getClient().create(InterfaceComment.class);
                        interfaceComment.editComment(commentModel.getComment_id(),
                                        et_comment.getText().toString(),
                                        recipe_id,
                                        useridx,
                                        user_id,
                                        commentModel.getComment_date(),
                                        commentModel.getComment_time())
                                .enqueue(new Callback<CommentModel>() {
                                    @Override
                                    public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                                        if (response.body().getSuccess().equals("1")) {
                                            Toasty.success(getContext(), "Comment updated!", Toasty.LENGTH_SHORT).show();
                                            commentModel.setComment(et_comment.getText().toString());
                                            relativeLayout.setVisibility(View.GONE);
                                            commentAdapter.notifyItemChanged(position);
                                            et_comment.setText("");
                                            commentAdapter.notifyItemRangeChanged(position, commentModelsList.size());
                                            relativeLayout.setVisibility(View.GONE);
                                            hideKeyboard();

                                            // if btn update comment is click and success than reset button send to create
                                            // new comment
                                            btnSend.setOnClickListener(View -> {
                                                String comment = et_comment.getText().toString();
                                                if (comment.isEmpty()) {
                                                    Toasty.warning(getContext(), "Please fill comment", Toasty.LENGTH_SHORT).show();
                                                } else {

                                                    postComment(commentId, useridx, recipe_id, user_id, et_comment.getText().toString());
                                                    hideKeyboard();
                                                }
                                            });


                                        } else {
                                            Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<CommentModel> call, Throwable t) {
                                        Toasty.error(getContext(), "Error no connection", Toasty.LENGTH_SHORT).show();

                                    }
                                });
                    });
                    break;

                case R.id.btn_delete:
                    InterfaceComment interfaceComment = DataApi.getClient().create(InterfaceComment.class);
                    interfaceComment.deleteComment(commentModel.getComment_id(), recipe_id, useridx, user_id,
                                    commentModel.getComment_date(), commentModel.getComment_time())
                            .enqueue(new Callback<CommentModel>() {
                                @Override
                                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                                    if (response.body().getSuccess().equals("1")) {
                                        Toasty.success(getContext(), "Comment deleted!", Toasty.LENGTH_SHORT).show();
                                        getComment(recipe_id);

                                        // remove from response buy postion
//                                                        commentModelsList.remove(position);
//                                                        commentAdapter.notifyItemChanged(position);
//                                                        commentAdapter.notifyItemRangeChanged(position, commentModelsList.size());
                                    } else {
                                        Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<CommentModel> call, Throwable t) {
                                    Toasty.error(getContext(), "No connection", Toasty.LENGTH_SHORT).show();

                                }

                            });

                    break;


            }




        }





        // Jika pemilik recipe yang klik maka ada opsi untuk delete comment
        else {
            switch (view.getId()) {
                case R.id.btn_delete:

                    InterfaceComment interfaceComment = DataApi.getClient().create(InterfaceComment.class);
                    interfaceComment.deleteComment(commentModel.getComment_id(), recipe_id, useridx, user_id,
                                    commentModel.getComment_date(), commentModel.getComment_time())
                            .enqueue(new Callback<CommentModel>() {
                                @Override
                                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                                    if (response.body().getSuccess().equals("1")) {
                                        Toasty.success(getContext(), "Comment deleted!", Toasty.LENGTH_SHORT).show();
                                        getComment(recipe_id);

                                        // remove from response by postion
//                                                        commentModelsList.remove(position);
//                                                        commentAdapter.notifyItemChanged(position);
//                                                        commentAdapter.notifyItemRangeChanged(position, commentModelsList.size());
                                    } else {
                                        Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<CommentModel> call, Throwable t) {
                                    Toasty.error(getContext(), "No connection", Toasty.LENGTH_SHORT).show();

                                }
                            });


                    break;


            }

        }

        if (getArguments().getString("admin") != null) {
            switch (view.getId()) {
                case R.id.tv_username:


                    Fragment fragment = new ShowProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("admin", "admin");
                    bundle.putString("user_id", commentModel.getUser_id());
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_admin, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;

                case R.id.iv_profile:
                    Fragment fragment1 = new ShowProfileFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", commentModel.getUser_id());
                    bundle1.putString("admin", "admin");
                    fragment1.setArguments(bundle1);
                    FragmentTransaction sp  = getFragmentManager().beginTransaction();
                    sp.replace(R.id.fragment_admin, fragment1);
                    sp.addToBackStack(null);
                    sp.commit();
                    break;

            }



        } else {
            switch (view.getId()) {
                case R.id.tv_username:


                    Fragment fragment = new ShowProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", commentModel.getUser_id());
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;

                case R.id.iv_profile:
                    Fragment fragment2 = new ShowProfileFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("user_id", commentModel.getUser_id());
                    fragment2.setArguments(bundle1);

                    FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                    ft2.replace(R.id.fragment_container, fragment2);
                    ft2.addToBackStack(null);
                    ft2.commit();
                    break;

            }

        }

    }


    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    // get recipe id to get total likes
    private void getTotalLikes(String recipe_id) {
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.getRecipe(recipe_id).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.body().size() > 0) {
                    Integer totalLikes = Integer.parseInt(response.body().get(0).getLikes());

                   prettyNumber(totalLikes, tvLikes);

                } else {
                    tvLikes.setText("");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

            }
        });
    }

    // method check connection
    private void checkConnection() {
        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (connectivityManager.getActiveNetworkInfo() != null
                    &&
                    connectivityManager.getActiveNetworkInfo().isAvailable()
                    &&
                    connectivityManager.getActiveNetworkInfo().isConnected()) {
            } else {
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();
            }
        }
    }

    // Method untuk pretty number
    private void prettyNumber(Integer number, TextView tv_likes) {
        if (number < 1000) {
            tv_likes.setText(number + "");
        } else if (number < 1000000) {
            tv_likes.setText(number/1000 + "K");
        } else if (number < 1000000000) {
            tv_likes.setText(number/1000000 + "M");
        } else {
            tv_likes.setText(number/1000000000 + "B");
        }
    }






}


