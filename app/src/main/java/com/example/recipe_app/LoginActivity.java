package com.example.recipe_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.recipe_app.Admin.DashboardActivity;
import com.example.recipe_app.Util.AppController;
import com.example.recipe_app.Util.DataApi;
import com.example.recipe_app.Util.InterfaceProfile;
import com.example.recipe_app.Util.ServerAPI;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout til_user, til_pass;
    TextInputEditText ti_user, ti_pass;
    Button signin, signinFb, signinGoogle;
    TextView signup;



    ProgressDialog pDialog;

    int success;
    ConnectivityManager conMgr;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_USERNAME = "username";
    String tag_json_obj = "json_obj_req";

    private static final String TAGname = LoginActivity.class.getSimpleName();

    SharedPreferences sharedpreferences;
    Boolean session = false;
    Boolean sessionAdmin = false;
    String username;
    Integer role;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    public static final String session_admin = "session_admin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initilize();



        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    &&
                    conMgr.getActiveNetworkInfo().isAvailable()
                    &&
                    conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        sessionAdmin = sharedpreferences.getBoolean(session_admin, false);
        username = sharedpreferences.getString(TAG_USERNAME,null);


        if (session) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(TAG_USERNAME, username);
            startActivity(intent);
            finish();
        }


        if (sessionAdmin) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra(TAG_USERNAME, username);
            startActivity(intent);
            finish();
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = ti_user.getText().toString();
                String password = ti_pass.getText().toString();

                //reset indikator error
                til_user.setErrorEnabled(false);
                til_user.setError(null);
                til_pass.setErrorEnabled(false);
                til_pass.setError(null);

                //validasi form
                if(Objects.requireNonNull(ti_user.getText()).toString().isEmpty() || ti_pass.getText().length() < 8){
                    if(ti_user.getText().toString().isEmpty()){
                        til_user.setErrorEnabled(true);
                        til_user.setError("Masukan Nama Pengguna");
                    }
                    if(ti_pass.getText().length() < 6) {
                        til_pass.setErrorEnabled(true);
                        til_pass.setError("Password berisi minimal 8 karakter");
                    }
                }else {
                    try {
                        if (conMgr.getActiveNetworkInfo() !=
                                null
                                &&
                                conMgr.getActiveNetworkInfo().isAvailable()
                                &&
                                conMgr.getActiveNetworkInfo().isConnected()) {
                            checkLogin(username, password);
                        } else {
                            Toast.makeText(getApplicationContext(), "Periksa Koneksi Internet", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initilize() {

        ti_user         = findViewById(R.id.ti_user_signin);
        ti_pass         = findViewById(R.id.ti_pass_signin);
        til_user        = findViewById(R.id.til_user_signin);
        til_pass        = findViewById(R.id.til_pass_signin);
        signin          = findViewById(R.id.button_signinSignin);
        signup          = findViewById(R.id.button_signupSignup);
    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Tunggu Sebentar ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, ServerAPI.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    role = jObj.getInt("role");

                    // Check for error node in json
                    if (success == 1) {


                        if (role == 1){
                            String username = jObj.getString(TAG_USERNAME);
                            String user_id = jObj.getString("user_id");

                            Log.e("Berhasil Masuk!", jObj.toString());
                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_admin, true);
                            editor.putString(TAG_USERNAME, username);
                            editor.putString("user_id", user_id);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra(TAG_USERNAME, username);
                            startActivity(intent);
                            finish();

                        }else {
                            String username = jObj.getString(TAG_USERNAME);
                            String user_id = jObj.getString("user_id");
                            Log.e("Berhasil Masuk!", jObj.toString());
                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            // menyimpan login ke session
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString(TAG_USERNAME, username);
                            editor.putString("user_id", user_id);
                            editor.commit();
                            // Memanggil main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(TAG_USERNAME, username);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //hide keyboard when touch other view
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}