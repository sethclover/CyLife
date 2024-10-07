package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button organizationEventsButton = findViewById(R.id.organization_events_button);
        Button joinClubButton = findViewById(R.id.join_club_button);
        Button logoutButton = findViewById(R.id.logout_button);
        ImageButton starIconButton = findViewById(R.id.star_icon_button);

        organizationEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, OrganizationEventsActivity.class);
                startActivity(intent);
            }
        });

        joinClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, JoinClubActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout
                finish(); // Or navigate to a login screen
            }
        });

        starIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle star icon button click
            }
        });
    }
}
