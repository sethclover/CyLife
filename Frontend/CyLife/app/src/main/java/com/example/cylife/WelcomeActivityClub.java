package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivityClub extends AppCompatActivity {

    private TextView welcomeText;
    private Button entButton;
    private Button logoutButton;
    private Button clubButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_club);

        // Initialize views
        welcomeText = findViewById(R.id.welcome_text);
        entButton = findViewById(R.id.ent_button);
        clubButton = findViewById(R.id.edit_club_button);
        logoutButton = findViewById(R.id.logout_button);

        entButton.setOnClickListener(view -> {
            // Start the Events Activity
            Intent intent = new Intent(WelcomeActivityClub.this, EventsActivity.class);
            startActivity(intent);
        });
        clubButton.setOnClickListener(view -> {
            // Start the Club Activity
            Intent intent = new Intent(WelcomeActivityClub.this, EditClub.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            finish();
        });
    }
}
