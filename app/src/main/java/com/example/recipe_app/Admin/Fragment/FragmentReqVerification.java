package com.example.recipe_app.Admin.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.my_shared_preferences;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recipe_app.Model.ProfileModel;
import com.example.recipe_app.R;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentReqVerification extends Fragment {
    String username, userid;
    EditText etUsername;
    AutoCompleteTextView doc_type, category_type, region, link_type;
    EditText etFullName, etUrl;
    ImageView ivDocument;
    Button btnChoose, btnSubmit;
    private final int TAG_GALLERY = 2222;
    ProgressDialog pd;
    Bitmap bitmap;
    String imageString;

    ImageButton btnBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_req_verification, container, false);

        // Mengambil username dan user_id menggunakan sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(my_shared_preferences, MODE_PRIVATE);
        username = sharedPreferences.getString(TAG_USERNAME, null);
        userid = sharedPreferences.getString("user_id", null);

        pd = new ProgressDialog(getContext());


        btnBack = root.findViewById(R.id.btn_back);
        etUsername = root.findViewById(R.id.et_username);
        doc_type= root.findViewById(R.id.et_document_type);
        category_type = root.findViewById(R.id.et_category);
        region = root.findViewById(R.id.et_region);
        link_type = root.findViewById(R.id.ac_link_type);
        etUsername = root.findViewById(R.id.et_username);
        etFullName = root.findViewById(R.id.et_full_name);
        etUrl = root.findViewById(R.id.et_link);
        ivDocument = root.findViewById(R.id.iv_document);
        btnChoose = root.findViewById(R.id.btn_choose);
        btnSubmit = root.findViewById(R.id.btn_submit);




        // Adapter document type
        ArrayAdapter<String> document = new ArrayAdapter<>(
               getContext(), R.layout.drop_down_item, getResources().getStringArray(R.array.document_type)
        );

        // Adapter category type
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                getContext(), R.layout.drop_down_item, getResources().getStringArray(R.array.category)

        );

        ArrayAdapter<String> list_region = new ArrayAdapter<>(

                getContext(), R.layout.drop_down_item, getResources().getStringArray(R.array.countries)
        );

        ArrayAdapter<String> list_link_type = new ArrayAdapter<>(
                getContext(), R.layout.drop_down_item, getResources().getStringArray(R.array.link_type)
        );




        // Set adapter to array list
        doc_type.setAdapter(document);
        category_type.setAdapter(categoryAdapter);
        region.setAdapter(list_region);
        link_type.setAdapter(list_link_type);



        // Settext username data from shared preferences
        etUsername.setText(username);

        // disable
        btnChoose.setEnabled(false);
        etUrl.setEnabled(false);



        // btn back click listener
        btnBack.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });


        // set button choose is enable
        doc_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btnChoose.setEnabled(true);
            }
        });

        // set edittext url is enable untill link type is clicked
        link_type.setOnItemClickListener((adapterView, view, i, l) -> {
            etUrl.setEnabled(true);
        });


        // btn choose image
        btnChoose.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, TAG_GALLERY);
            }

        });

        btnSubmit.setOnClickListener(view -> {


            // Validation form
            if (etFullName.getText().toString().isEmpty() || etUrl.getText().toString().isEmpty() || doc_type.getText().toString().equals("")
                    ||  category_type.getText().toString().equals("") ||  region.getText().toString().equals("")
                    || link_type.getText().toString().equals("")) {
                Toasty.warning(getContext(), "Please fill all the fields", Toasty.LENGTH_SHORT).show();

            } else if(bitmap == null) {
                Toasty.warning(getContext(), "Please select image",Toasty.LENGTH_SHORT).show();
                btnChoose.setError("Please select image");
            }
            else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);



                pd.setMessage("Uploading...");
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                postVerification();



            }

        });






        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==  RESULT_OK && requestCode == TAG_GALLERY && data != null && data.getData() != null){

            Uri uri_path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri_path);
                ivDocument.setImageBitmap(bitmap);
                ivDocument.setVisibility(View.VISIBLE);

                Toasty.success(getContext(), "Success load image", Toasty.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toasty.error(getContext(), "Failed to load image", Toasty.LENGTH_SHORT).show();


            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // Method post verification
    private void postVerification() {
        InterfaceProfile interfaceProfile = DataApi.getClient().create(InterfaceProfile.class);
        interfaceProfile.postVerification(
                userid, etFullName.getText().toString(), etUsername.getText().toString(),
                doc_type.getText().toString(), category_type.getText().toString(), region.getText().toString(),
                link_type.getText().toString(), etUrl.getText().toString(), imageString
                ).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if (response.body().getMessage().equals("1")){
                    pd.dismiss();
                    Toasty.success(getContext(), "Request verification success", Toasty.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                } else {
                    pd.dismiss();
                    Toasty.error(getContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                pd.dismiss();
                Toasty.error(getContext(), "Please check your connection", Toasty.LENGTH_SHORT).show();

            }
        });
    }
}