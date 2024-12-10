package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private TextView tv_username;
    private int userId;
    private List<Integer> clubIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChats = findViewById(R.id.recycler_view_chats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        tv_username = findViewById(R.id.tv_username);
        chatList = new ArrayList<>();

        // Retrieve student name, clubId, and userId from Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("studentName");
        userId = intent.getIntExtra("userID", -1);

        if (name != null) {
            tv_username.setText(name);
        } else {
            setTitle("Username");
        }

        if (userId != -1) {
            Log.d("ChatActivity", "User ID: " + userId);
            fetchClubs(userId);  // Fetch clubs for the student
        } else {
            Log.d("ChatActivity", "User ID is missing");
        }

        Button bottomSettingsButton = findViewById(R.id.btn_back);
        bottomSettingsButton.setOnClickListener(v -> finish());
    }

    private void fetchClubs(int userId) {
        String clubsUrl = "http://coms-3090-065.class.las.iastate.edu:8080/user/" + userId + "/clubs";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Fetch clubs request
        JsonObjectRequest clubsRequest = new JsonObjectRequest(Request.Method.GET, clubsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray clubsArray = response.optJSONArray("clubs");
                            if (clubsArray == null || clubsArray.length() == 0) {
                                throw new JSONException("No clubs found for this user");
                            }

                            // Extract club IDs
                            for (int i = 0; i < clubsArray.length(); i++) {
                                JSONObject club = clubsArray.getJSONObject(i);
                                clubIds.add(club.optInt("clubId", -1));
                            }

                            // Now fetch active chats
                            fetchActiveChats();

                        } catch (JSONException e) {
                            Log.e("ChatActivity", "Error parsing clubs response: " + e.getMessage());
                            Toast.makeText(ChatActivity.this, "Failed to fetch clubs", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ChatActivity", "Error fetching clubs: " + error.getMessage());
                        Toast.makeText(ChatActivity.this, "Failed to fetch clubs", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(clubsRequest);
    }

    private void fetchActiveChats() {
        String CHATS_URL = "http://coms-3090-065.class.las.iastate.edu:8080/api/chats/active";
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

                // Get clubId and clubName
                int clubId = chatObject.optInt("clubId", -1);
                String clubName = chatObject.optString("clubName", "Unknown Chat");

                // If the clubId matches one of the student's clubIds, add the chat
                if (clubIds.contains(clubId)) {
                    chatList.add(new Chat(clubName, R.drawable.cy, clubId));
                }
            }
            chatAdapter = new ChatAdapter(this, chatList, userId, tv_username.getText().toString());
            recyclerViewChats.setAdapter(chatAdapter);

        } catch (JSONException e) {
            Log.e("ChatActivity", "Error parsing chat data: " + e.getMessage());
            Toast.makeText(this, "Error parsing chat data", Toast.LENGTH_SHORT).show();
        }
    }
}
