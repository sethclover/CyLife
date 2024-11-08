package com.example.cylife;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChats;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;
    private static final String CHATS_URL = "http://10.0.2.2:8080/chats";  // Replace with your actual server IP if testing on a device


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChats = findViewById(R.id.recycler_view_chats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatList);
        recyclerViewChats.setAdapter(chatAdapter);

        fetchChats();

        Button bottomSettingsButton = findViewById(R.id.btn_back);
        bottomSettingsButton.setOnClickListener(v -> finish());
    }

    private void fetchChats() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, CHATS_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseChatData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log error for debugging purposes
                        error.printStackTrace();
                        Toast.makeText(ChatActivity.this, "Error fetching chats: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }

    private void parseChatData(JSONArray chatArray) {
        chatList.clear();  // Clear previous data if needed
        try {
            for (int i = 0; i < chatArray.length(); i++) {
                JSONObject chatObject = chatArray.getJSONObject(i);

                // Assuming the server sends chat data with 'id' and 'name'
                String chatName = chatObject.optString("name", "Unknown Chat");
                int chatId = chatObject.optInt("id", -1);

                // Add chat item to the list
                chatList.add(new Chat(chatName, R.drawable.cy, chatId));
            }
            chatAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing chat data", Toast.LENGTH_SHORT).show();
        }
    }
}
