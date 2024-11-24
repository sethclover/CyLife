package com.example.cylife;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.java_websocket.handshake.ServerHandshake;

public class ChatScreenActivity extends AppCompatActivity implements WebSocketListener {
  private TextView chatHistory;
  private EditText msgInput;
  private Button sendBtn, backButton;
  private int clubId;
  private int userId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_screen);

    // Initialize UI elements
    chatHistory = findViewById(R.id.chat_history);
    msgInput = findViewById(R.id.message_input);
    sendBtn = findViewById(R.id.send_button);

    backButton = findViewById(R.id.btn_back);
    backButton.setOnClickListener(v -> finish());

    // Get clubId and userId passed from the previous activity
    clubId = getIntent().getIntExtra("clubId", -1);
    userId = getIntent().getIntExtra("userID", -1);
    Log.d("ChatScreenActivity", "Received clubId: " + clubId);
    Log.d("ChatScreenActivity", "Received userId: " + userId);

    String chatName = getIntent().getStringExtra("chatName");

    if (clubId == -1 || userId == -1) {
      Log.e("ChatScreenActivity", "Invalid clubId or userId passed to ChatScreenActivity");
    }

    setTitle(chatName);

    // Construct WebSocket URL
    String serverUrl =
        "ws://coms-3090-065.class.las.iastate.edu:8080/chat/" + clubId + "/" + userId;
    Log.d("ChatScreenActivity", "Connecting to WebSocket URL: " + serverUrl);

    // Set WebSocket listener and connect
    WebSocketManager.getInstance().setWebSocketListener(this);
    WebSocketManager.getInstance().connectWebSocket(serverUrl);

    // Handle sending message
    sendBtn.setOnClickListener(
        v -> {
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Remove listener and close WebSocket connection
    WebSocketManager.getInstance().removeWebSocketListener();
    WebSocketManager.getInstance().disconnectWebSocket();
  }
}
