package com.example.recipe_app;

import static com.example.recipe_app.LoginActivity.TAG_USERNAME;
import static com.example.recipe_app.LoginActivity.session_status;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.recipe_app.Util.AppController;
import com.example.recipe_app.Util.ServerAPI;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    ConnectivityManager conMgr;
    ProgressDialog pDialog;
    int success;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tanggal, time;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    String tag_json_obj = "json_obj_req";

    TextInputEditText ti_email, ti_user, ti_pass, ti_confpass;
    TextView signin;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedpreferences = getSharedPreferences(my_shared_preferences,Context.MODE_PRIVATE);

        ti_email = findViewById(R.id.ti_email_signup);
        ti_user = findViewById(R.id.edt_username);
        ti_pass  = findViewById(R.id.edt_pass);
        ti_confpass  = findViewById(R.id.edt_pass_conf);
        signin = findViewById(R.id.tv_ToLogin);
        signup = findViewById(R.id.button_register);

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


        // saat button signin diklik, maka akan menjalankan fungsi ini
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ti_email.getText().toString();
                String username = ti_user.getText().toString();
                String password = ti_pass.getText().toString();
                String confir_password = ti_confpass.getText().toString();

                //validasi form
                if( !isValidEmail(email) || username.isEmpty() || password.length() < 8 || !confir_password.equals(password)){
                    if(!isValidEmail(email)){
                        ti_email.setError("Email tidak valid");
                    }
                    if(username.isEmpty()){
                        ti_user.setError("Masukan Nama Pengguna");
                    }
                    if(password.length() < 8) {
                        ti_pass.setError("Password berisi minimal 8 karakter");
                        Toast.makeText(RegisterActivity.this, "Password berisi minimal 8 karakter", Toast.LENGTH_SHORT).show();
                    }
                    if(!confir_password.equals(password)) {
                        ti_confpass.setError("Password tidak sama");
                    }
                }else{
                    try {
                        if (conMgr.getActiveNetworkInfo() !=
                                null
                                &&
                                conMgr.getActiveNetworkInfo().isAvailable()
                                &&
                                conMgr.getActiveNetworkInfo().isConnected()) {
                            register(email, username, password);
                        } else {
                            Toast.makeText(getApplicationContext(), "Periksa Koneksi Internet", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void register(final String email, final String username, final String password) {

        getDateTime();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Tunggu Sebentar ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, ServerAPI.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    // Check for error node in json
                    if (success == 1) {

                        // Memanggil main activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra(TAG_USERNAME, username);
                        startActivity(intent);

                        String username = jObj.getString(TAG_USERNAME);
                        Log.e("Berhasil Mendaftar!", jObj.toString());
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status,true);
                        editor.putString(TAG_USERNAME, username);
                        editor.putString("user_id", jObj.getString("user_id"));
                        editor.commit();

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
                Log.e(TAG, "Register Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);
                params.put("date", tanggal);
                params.put("time",time);
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

    private void getDateTime() {
        tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    //validasi email
    public final static boolean isValidEmail(CharSequence target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //hide keyboard when touch other view
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}