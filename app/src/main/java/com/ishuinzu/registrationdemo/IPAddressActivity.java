package com.ishuinzu.registrationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IPAddressActivity extends AppCompatActivity {
    private EditText edtIPAddress;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_p_address);

        //Check if User already Logged in
        if (Preferences.getInstance(this).isLoggedIn()) {
            Toast.makeText(this, "User Already Logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(IPAddressActivity.this, MainActivity.class));
            finish();
        }

        edtIPAddress = findViewById(R.id.edtIPAddress);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            //Get IP Address
            String ip = edtIPAddress.getText().toString();

            if (ip.isEmpty() || !Patterns.IP_ADDRESS.matcher(ip).matches()) {
                edtIPAddress.setError("Invalid IP");
                edtIPAddress.requestFocus();
                return;
            }

            //Start Login Activity
            startActivity(new Intent(IPAddressActivity.this, LoginActivity.class).putExtra("IP", ip));
            finish();
        });
    }
}