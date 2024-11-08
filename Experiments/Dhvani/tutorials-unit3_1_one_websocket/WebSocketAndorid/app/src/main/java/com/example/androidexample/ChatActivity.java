package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn;
    private EditText msgEtx;
    private TextView chatHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendBtn = findViewById(R.id.sendBtn);
        msgEtx = findViewById(R.id.msgEdt);
        chatHistory = findViewById(R.id.tx1);

        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String message = msgEtx.getText().toString();
                    if (!message.isEmpty()) {
                        WebSocketManager.getInstance().sendMessage(message);
                        appendMessageToChat("You: " + message);
                        msgEtx.setText("");  // Clear input after sending
                    }
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                appendMessageToChat("Server: " + message);
            }
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appendMessageToChat("System: Connection closed by " + closedBy + ". Reason: " + reason);
            }
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appendMessageToChat("System: Connected to server.");
            }
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appendMessageToChat("System: Error - " + ex.getMessage());
            }
        });
    }

    private void appendMessageToChat(String message) {
        String currentText = chatHistory.getText().toString();
        chatHistory.setText(currentText + "\n" + message);
    }
}
