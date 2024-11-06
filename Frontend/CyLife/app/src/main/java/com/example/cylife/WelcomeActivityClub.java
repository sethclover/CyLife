package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivityClub extends AppCompatActivity {

    private TextView welcomeText;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_club);

        // Initialize views
        welcomeText = findViewById(R.id.welcomeTextView);


        logoutButton.setOnClickListener(view -> {
            finish();
        });
    }
}
