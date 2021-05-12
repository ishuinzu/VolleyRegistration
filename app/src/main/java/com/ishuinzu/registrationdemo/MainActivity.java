package com.ishuinzu.registrationdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView txtUserName, txtUserEmail, txtUserGender, txtUserID;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUserName = findViewById(R.id.txtUserName);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserGender = findViewById(R.id.txtUserGender);
        txtUserID = findViewById(R.id.txtUserID);
        btnLogout = findViewById(R.id.btnLogout);

        //Set User Details
        txtUserName.setText(Preferences.getInstance(this).getUser().getName());
        txtUserEmail.setText(Preferences.getInstance(this).getUser().getEmail());
        txtUserGender.setText(Preferences.getInstance(this).getUser().getGender());
        txtUserID.setText(String.valueOf(Preferences.getInstance(this).getUser().getId()));

        btnLogout.setOnClickListener(v -> {
            //Logout User
            Preferences.getInstance(MainActivity.this).logout();
            finish();
        });
    }
}