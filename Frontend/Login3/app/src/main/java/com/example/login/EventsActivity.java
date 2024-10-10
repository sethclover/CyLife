package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EventsActivity extends AppCompatActivity {

    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        // Implement event-related logic here

        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(EventsActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

