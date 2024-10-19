package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button orgButton;
    private Button entButton;
    private Button logoutButton;
    private Button clubButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize views
        welcomeText = findViewById(R.id.welcome_text);
        orgButton = findViewById(R.id.org_button);
        entButton = findViewById(R.id.ent_button);
        clubButton = findViewById(R.id.club_button);
        logoutButton = findViewById(R.id.logout_button);

        // Set onClickListeners for buttons
        orgButton.setOnClickListener(view -> {
            // Start the Organization Activity
            Intent intent = new Intent(WelcomeActivity.this, createOrganization.class);
            startActivity(intent);
        });

        entButton.setOnClickListener(view -> {
            // Start the Events Activity
            Intent intent = new Intent(WelcomeActivity.this, EventsActivity.class);
            startActivity(intent);
        });
        clubButton.setOnClickListener(view -> {
            // Start the Club Activity
            Intent intent = new Intent(WelcomeActivity.this, createClub.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            finish();
        });
    }
}
