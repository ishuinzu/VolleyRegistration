package com.ishuinzu.registrationdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText edtUserName, edtUserEmail, edtUserPassword;
    private RadioGroup groupGender;
    private Button btnLogin, btnSignup;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //If User is Already Logged in
        if (Preferences.getInstance(this).isLoggedIn()) {
            Toast.makeText(this, "User Already Logged In", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }

        //Get IP Address
        ip = getIntent().getExtras().getString("IP");

        edtUserName = findViewById(R.id.edtUserName);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserPassword = findViewById(R.id.edtUserPassword);
        groupGender = findViewById(R.id.groupGender);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(v -> {
            //Redirect user to Login Screen
            startActivity(new Intent(SignupActivity.this, LoginActivity.class).putExtra("IP", ip));
            finish();
        });

        btnSignup.setOnClickListener(v -> {
            //Create New User Account
            createUser();
        });
    }

    private void createUser() {
        //Get Values
        String name = edtUserName.getText().toString();
        String email = edtUserEmail.getText().toString();
        String password = edtUserPassword.getText().toString();
        String gender = ((RadioButton) findViewById(groupGender.getCheckedRadioButtonId())).getText().toString();

        //Validating Fields
        if (name.isEmpty()) {
            edtUserName.setError("Invalid Name");
            edtUserName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtUserEmail.setError("Invalid Email");
            edtUserEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtUserEmail.setError("Invalid Email");
            edtUserEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtUserPassword.setError("Invalid Password");
            edtUserPassword.requestFocus();
            return;
        }

        String url = URLs.HTTP_OR_HTTPS + ip + URLs.URL_REGISTER;
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(SignupActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    JSONObject userObject = jsonObject.getJSONObject("user");

                    //Creating User object and store it in Preferences
                    User user = new User(
                            userObject.getInt("user_id"),
                            userObject.getString("user_name"),
                            userObject.getString("user_email"),
                            userObject.getString("user_gender")
                    );

                    Preferences.getInstance(SignupActivity.this).setUser(user);

                    //Redirect user to Dashboard
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(SignupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", name);
                params.put("user_email", email);
                params.put("user_password", password);
                params.put("user_gender", gender);

                return params;
            }
        };

        //Add Request to Queue
        VolleyAssistant.getInstance(this).addToRequestQueue(stringRequest);
    }
}