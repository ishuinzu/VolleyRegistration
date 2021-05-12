package com.ishuinzu.registrationdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    private EditText edtUserEmail, edtUserPassword;
    private Button btnLogin, btnSignup;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Check is user already logged in
        if (Preferences.getInstance(this).isLoggedIn()) {
            Toast.makeText(this, "User Already Logged In", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        //Get IP
        ip = getIntent().getExtras().getString("IP");

        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserPassword = findViewById(R.id.edtUserPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {
            //Redirect user to Signup Screen
            startActivity(new Intent(LoginActivity.this, SignupActivity.class).putExtra("IP", ip));
            finish();
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log user in
                logUserIn();
            }
        });
    }

    private void logUserIn() {
        //Get User Details For Login
        String email = edtUserEmail.getText().toString();
        String password = edtUserPassword.getText().toString();

        //Checking input fields for validation
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtUserEmail.setError("Invalid Email");
            edtUserEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtUserPassword.setError("Invalid Password");
            edtUserPassword.requestFocus();
            return;
        }

        String url = URLs.HTTP_OR_HTTPS + ip + URLs.URL_LOGIN;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    JSONObject userObject = jsonObject.getJSONObject("user");

                    //Set User Details and add in Preferences
                    User user = new User(
                            userObject.getInt("user_id"),
                            userObject.getString("user_name"),
                            userObject.getString("user_email"),
                            userObject.getString("user_gender")
                    );

                    Preferences.getInstance(LoginActivity.this).setUser(user);

                    //Redirect user to Main Screen - Dashboard
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_email", email);
                params.put("user_password", password);

                return params;
            }
        };

        //Adding request to queuq
        VolleyAssistant.getInstance(this).addToRequestQueue(stringRequest);
    }
}