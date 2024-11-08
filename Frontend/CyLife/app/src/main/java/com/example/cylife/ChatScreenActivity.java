package com.example.cylife;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.handshake.ServerHandshake;

public class ChatScreenActivity extends AppCompatActivity implements WebSocketListener {
    private TextView chatHistory;
    private EditText msgInput;
    private Button sendBtn;
    private int chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        // Initialize UI elements
        chatHistory = findViewById(R.id.chat_history);
        msgInput = findViewById(R.id.message_input);
        sendBtn = findViewById(R.id.send_button);

        // Get chat details passed from the previous activity
        chatId = getIntent().getIntExtra("chatId", -1);
        String chatName = getIntent().getStringExtra("chatName");
        setTitle(chatName);

        // Set WebSocket listener
        WebSocketManager.getInstance().setWebSocketListener(this);

        // Handle sending message
        sendBtn.setOnClickListener(v -> {
            String message = msgInput.getText().toString();
            if (!message.isEmpty()) {
                WebSocketManager.getInstance().sendMessage(message);
                appendMessageToChat("You: " + message);
                msgInput.setText("");
            }
        });
    }

    // Append the incoming message to the chat history TextView
    private void appendMessageToChat(String message) {
        String currentText = chatHistory.getText().toString();
        chatHistory.setText(currentText + "\n" + message);
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> appendMessageToChat("Server: " + message));
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        runOnUiThread(() -> appendMessageToChat("System: Connection closed. Reason: " + reason));
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        runOnUiThread(() -> appendMessageToChat("System: Connected to server."));
    }

    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> appendMessageToChat("System: Error - " + ex.getMessage()));
    }
}
