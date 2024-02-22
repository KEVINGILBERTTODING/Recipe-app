package com.example.recipe_app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static com.example.recipe_app.ui.activities.LoginActivity.my_shared_preferences;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMyRecipeFragment extends Fragment {



    EditText recipeName, description, serves, duration, ingredients, steps, notes;
    ImageButton btn_image_picker, btnBack;
    Button btn_save;
    SwitchMaterial switch_private;
    private RadioGroup rgCategory;
    ImageView img_recipe, img_recipe2;
    RadioButton rb_vegetable, rb_meat, rb_noodle, rb_drink, rb_other;
    LottieAnimationView add_animation;
    Dialog dialog;



    Bitmap bitmap;

    GalleryPhoto mGalery;
    private final int TAG_GALLERY = 2222;
    String selected_photo = null;

    public static final int progress_bar_type = 0;
    private ProgressDialog pd;

    String imageString, userid, recipeNames, recipe_id;

    String recipe_name, description_, serves_, duration_, ingredients_, steps_, notes_, category_,  status_;


    public EditMyRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_my_recipe, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", null);

        recipeName = view.findViewById(R.id.edt_recipe_name);
        description = view.findViewById(R.id.edt_description);
        serves = view.findViewById(R.id.edt_serves);
        duration = view.findViewById(R.id.edt_duration);
        ingredients = view.findViewById(R.id.edt_ingredients);
        steps = view.findViewById(R.id.edt_steps);
        notes = view.findViewById(R.id.edt_notes);
        btn_image_picker = view.findViewById(R.id.btn_image_picker);
        btn_save = view.findViewById(R.id.btn_save);
        switch_private = view.findViewById(R.id.switch_privacy);
        rgCategory = view.findViewById(R.id.rg_category);
        img_recipe = view.findViewById(R.id.img_recipe);

        rb_vegetable = view.findViewById(R.id.rb_vegetable);
        rb_meat = view.findViewById(R.id.rb_meat);
        rb_noodle = view.findViewById(R.id.rb_noodle);
        rb_drink = view.findViewById(R.id.rb_drinks);
        rb_other = view.findViewById(R.id.rb_other);
        btnBack = view.findViewById(R.id.btn_back);


        recipeNames = getArguments().getString("title");
        recipe_id = getArguments().getString("recipe_id");
        String recipeIngredients = getArguments().getString("ingredients");
        String recipeSteps= getArguments().getString("steps");
        String recipeRating= getArguments().getString("ratings");
        String recipeDuration= getArguments().getString("duration");
        String recipeServings= getArguments().getString("servings");
        String recipeDescription= getArguments().getString("description");
        String recipeUsername= getArguments().getString("username");
        String recipeEmail= getArguments().getString("email");
        String recipeDate= getArguments().getString("upload_date");
        String recipeTime= getArguments().getString("upload_time");
        String photoRecipe= getArguments().getString("image");
        String photoProfile= getArguments().getString("photo_profile");
        String recipeNOtes= getArguments().getString("notes");
        String status  = getArguments().getString("status");
        String totalLikes= getArguments().getString("likes");
        String categoryRecipe = getArguments().getString("category");

        pd = new ProgressDialog(getContext());


        recipeName.setText(recipeNames);
        description.setText(recipeDescription);
        serves.setText(recipeServings);
        duration.setText(recipeDuration);
        ingredients.setText(recipeIngredients);
        steps.setText(recipeSteps);
        notes.setText(recipeNOtes);

        // if btn back is click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();


            }
        });

        // load image recipe
        Glide. with(getContext())
                .load(photoRecipe)
                .thumbnail(0.5f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .placeholder(R.drawable.template_img)
                .override(1024, 768)
                .fitCenter()
                .centerCrop()
                .into(img_recipe);

        btn_image_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.edit_image_recipe);
                img_recipe2 = dialog.findViewById(R.id.img_recipe);
                add_animation = dialog.findViewById(R.id.add_anim);
                final Button btnEdit = dialog.findViewById(R.id.btn_update);

                img_recipe2.setOnClickListener(view -> {

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, TAG_GALLERY);



                    }
                });

                btnEdit.setOnClickListener(view1 -> {
                    // if bitmap == null, then show toast
                    if (bitmap == null) {
                        Toast.makeText(getContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else {
                        // if bitmap != null, then upload image to server

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        pd.setMessage("Saving...");
                        pd.show();
                        pd.setCancelable(false);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setProgressStyle(progress_bar_type);

                        uploadPhoto();
                    }
                });
                dialog.show();
            }
        });

        // Set bullet point when enter new line in edittext ingredients

        ingredients.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable e) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
            {
                if (lengthAfter > lengthBefore) {
                    if (text.toString().length() == 1) {
                        text = "• " + text;
                        ingredients.setText(text);
                        ingredients.setSelection(ingredients.getText().length());
                    }
                    if (text.toString().endsWith("\n")) {
                        text = text.toString().replace("\n", "\n• ");
                        text = text.toString().replace("• •", "•");
                        ingredients.setText(text);
                        ingredients.setSelection(ingredients.getText().length());
                    }
                }
            }
        });

        // Set bullet point when enter new line in edittext steps

        steps.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable e) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
            {
                if (lengthAfter > lengthBefore) {
                    if (text.toString().length() == 1) {
                        text = "• " + text;
                        steps.setText(text);
                        steps.setSelection(steps.getText().length());
                    }
                    if (text.toString().endsWith("\n")) {
                        text = text.toString().replace("\n", "\n• ");
                        text = text.toString().replace("• •", "•");
                        steps.setText(text);
                        steps.setSelection(steps.getText().length());
                    }
                }
            }
        });


        // jika status "1" maka switch_private akan off

        if (status.equals("1")) {
            switch_private.setChecked(false);
        } else {
            switch_private.setChecked(true);

        }

        // set check radio button

        if (categoryRecipe.equals(rb_vegetable.getText())) {
            rb_vegetable.setChecked(true);
        } else if (categoryRecipe.equals(rb_meat.getText())) {
            rb_meat.setChecked(true);
        } else if (categoryRecipe.equals(rb_noodle.getText())) {
            rb_noodle.setChecked(true);
        } else if (categoryRecipe.equals(rb_drink.getText())) {
            rb_drink.setChecked(true);
        } else if (categoryRecipe.equals(rb_other.getText())) {
            rb_other.setChecked(true);
        }

        btn_save.setOnClickListener(view1 -> {
            // gettext from edittext
            recipe_name = recipeName.getText().toString();
            description_ = description.getText().toString();
            serves_ = serves.getText().toString();
            duration_ = duration.getText().toString();
            ingredients_ = ingredients.getText().toString();
            steps_ = steps.getText().toString();
            notes_ = notes.getText().toString();

            // if switch private is checked, status = 1

            if (switch_private.isChecked()) {
                status_ = "2"; // meaning private
            } else {
                status_ = "1"; // meaning public
            }

            // Function for get selected radio button from radio group
            int checkedButtonId = rgCategory.getCheckedRadioButtonId();

            RadioButton checkedButton = (RadioButton) getView().findViewById(checkedButtonId);

            category_ = checkedButton.getText().toString();



            // if field is empty, then show toast
            if (recipe_name.isEmpty() || description_.isEmpty() || serves_.isEmpty() || duration_.isEmpty() || ingredients_.isEmpty() || steps_.isEmpty()) {

                Toast.makeText(getContext(), "Field cannot empty", Toast.LENGTH_SHORT).show();
            } else {
                // if field is not empty, then upload data to server
                pd.setMessage("Updating...");
                pd.show();
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.setProgressStyle(progress_bar_type);
                updateRecipe(recipe_id, recipe_name, description_,  category_, serves_, duration_, ingredients_, steps_, status_, notes_);
            }

        });




        return view;
    }

    // method untuk upload foto
    private void uploadPhoto(){
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.updateImageRecipe(recipe_id, userid, imageString, recipeNames).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()){
                    pd.dismiss();
                    dialog.dismiss();
                    Toasty.success(getContext(), "Upload image success", Toasty.LENGTH_SHORT).show();
                    img_recipe.setImageBitmap(bitmap);
                } else {
                    pd.dismiss();
                    Toasty.error(getContext(), "Upload image failed", Toasty.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==  RESULT_OK && requestCode == TAG_GALLERY && data != null && data.getData() != null){

            Uri uri_path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri_path);
                img_recipe2.setImageBitmap(bitmap);
                add_animation.cancelAnimation();
                add_animation.setVisibility(View.GONE);
                Snackbar.make(getView(), "Successfully load image", Snackbar.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Snackbar.make(getView(), "Failed to load image", Snackbar.LENGTH_LONG).show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // method untuk update recipe

    private void updateRecipe(String recipe_id, String recipeNames, String description, String category, String servings,
                              String duration, String ingredients, String steps, String status, String notes){
        InterfaceRecipe interfaceRecipe = DataApi.getClient().create(InterfaceRecipe.class);
        interfaceRecipe.updateRecipe(recipe_id, recipeNames, description, category, servings, duration, ingredients,
                steps, status, notes).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()){
                    pd.dismiss();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();


                    Snackbar.make(getView(), "Successfully update recipe", Snackbar.LENGTH_LONG).show();
                    fm.popBackStack();

                } else {
                    pd.dismiss();
                    Snackbar.make(getView(), "Update recipe failed", Snackbar.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Snackbar.make(getView(), "Check your connection", Snackbar.LENGTH_LONG).show();
                pd.dismiss();
                Log.d("onFailure", t.toString());
            }
        });
    }

}