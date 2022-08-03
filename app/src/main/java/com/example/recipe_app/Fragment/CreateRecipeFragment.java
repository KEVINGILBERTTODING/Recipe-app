package com.example.recipe_app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;
import static com.example.recipe_app.Util.DataApi.BASE_URL;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipe_app.Model.RecipeModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceRecipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreateRecipeFragment extends Fragment {

    EditText recipeName, description, serves, duration, ingredients, steps, notes;
    ImageButton btn_image_picker;
    Button btn_save;
    SwitchMaterial switch_private;
    private RadioGroup rgCategory;

    String recipe_name, description_, serves_, duration_, ingredients_, steps_, notes_, category_,
            username, userid, status;


    ImageView img_recipe;

    Bitmap bitmap;

    GalleryPhoto mGalery;
    private final int TAG_GALLERY = 2222;
    String selected_photo = null;

    public static final int progress_bar_type = 0;
    private ProgressDialog pd;

    String imageString;


    public CreateRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_recipe, container, false);


        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
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

        pd = new ProgressDialog(getContext());
        mGalery = new GalleryPhoto(getContext());

        btn_image_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, TAG_GALLERY);
                }
            }
        });

        btn_save.setOnClickListener(View -> {

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
                status = "2"; // meaning private
            } else {
                status = "1"; // meaning public
            }

            // Validate data

            // if bitmap == null, then show toast
            if (bitmap == null) {
                Toast.makeText(getContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
            }

            // if field is empty, then show toast
            if (recipe_name.isEmpty() || description_.isEmpty() || serves_.isEmpty() || duration_.isEmpty() || ingredients_.isEmpty() || steps_.isEmpty()) {

                Toast.makeText(getContext(), "Field cannot empty", Toast.LENGTH_SHORT).show();
            }

            else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                 imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);


                // Function for get selected radio button from radio group
                int checkedButtonId = rgCategory.getCheckedRadioButtonId();

                RadioButton checkedButton = (RadioButton) getView().findViewById(checkedButtonId);

                category_ = checkedButton.getText().toString();



                    pd.setMessage("Saving...");
                    pd.show();
                    pd.setCancelable(false);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setProgressStyle(progress_bar_type);

                    // Mengirim data ke server
                    createRecipe(recipe_name, description_, serves_, duration_, ingredients_, steps_, notes_, category_, status);

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




        return view;
    }

    private  void createRecipe( String recipe_name, String description_, String serves_, String duration_, String ingredients_, String steps_, String notes_, String category_, String status) {



        // Sending data to server

        DataApi.getClient().create(InterfaceRecipe.class)
                .createRecipe(userid, recipe_name, description_, category_, serves_, duration_, ingredients_, steps_,
                        imageString, status, notes_).enqueue(new retrofit2.Callback<RecipeModel>() {
            @Override
            public void onResponse(retrofit2.Call<RecipeModel> call, retrofit2.Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();

                    Toast.makeText(getContext(), "Berhasil menambahkan resep", Toast.LENGTH_SHORT).show();
//                    recipeModelList = response.body();
//                    Gson gson = new GsonBuilder().create();
//                    String json = gson.toJson(recipeModelList);
//                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(TAG_RESPONSE, json);
//                    editor.apply();
//                    getActivity().finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RecipeModel> call, Throwable t) {
                pd.dismiss();
                Log.d("onFailure", t.getMessage());
                Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();
            }
        });


    }


   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == TAG_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, TAG_GALLERY);
            }
            else {
                Snackbar.make(getView(), "Permission Denied", Snackbar.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==  RESULT_OK && requestCode == TAG_GALLERY && data != null && data.getData() != null){

            Uri uri_path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri_path);
                img_recipe.setImageBitmap(bitmap);

                Snackbar.make(getView(), "Successfully load image", Snackbar.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

//                Snackbar.make(findViewById(android.R.id.content), "Something Wrong", Snackbar.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}