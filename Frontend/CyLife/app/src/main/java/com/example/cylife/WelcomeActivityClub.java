package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;

public class WelcomeActivityClub extends AppCompatActivity implements WebSocketListener {

    private TextView joiningText;
    private TextView welcomeText;
    private Button entButton;
    private Button logoutButton;
    private Button clubButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_club);

        // Initialize views
        joiningText = findViewById(R.id.joining_text);
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


    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
            String s = joiningText.getText().toString();
            joiningText.setText(s + "\n"+message);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = joiningText.getText().toString();
            joiningText.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}
}
