package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatActivity extends AppCompatActivity {

  private static final Logger log = LoggerFactory.getLogger(ChatActivity.class);
  private RecyclerView recyclerViewChats;
  private ChatAdapter chatAdapter;
  private List<Chat> chatList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    recyclerViewChats = findViewById(R.id.recycler_view_chats);
    recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));

    chatList = new ArrayList<>();

    // Retrieve student name, clubId, and userId from Intent
    Intent intent = getIntent();
    String name = intent.getStringExtra("name");
    int clubId = intent.getIntExtra("clubId", -1);
    int userId = intent.getIntExtra("userID", -1);

    if (name != null) {
      setTitle(name);
    } else {
      setTitle("User Name");
    }

    if (clubId != -1 && userId != -1) {
      Log.d("ChatActivity", "Club ID: " + clubId);
      Log.d("ChatActivity", "User ID: " + userId);
      Log.d("ChatActivity", "UserName: " + name);
      intent.putExtra("clubId", clubId);
      intent.putExtra("userID", userId);
      fetchChats();

      // Initialize ChatAdapter with userId
      chatAdapter = new ChatAdapter(this, chatList, userId, name);
      recyclerViewChats.setAdapter(chatAdapter);

    } else {
      Log.d("Chat Activity", "Club ID or User ID is missing");
    }

    Button bottomSettingsButton = findViewById(R.id.btn_back);
    bottomSettingsButton.setOnClickListener(v -> finish());
  }

  private void fetchChats() {
    String CHATS_URL = "http://coms-3090-065.class.las.iastate.edu:8080/api/chats/active";
    RequestQueue queue = Volley.newRequestQueue(this);

    JsonArrayRequest jsonArrayRequest =
        new JsonArrayRequest(
            Request.Method.GET,
            CHATS_URL,
            null,
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
                Toast.makeText(
                        ChatActivity.this,
                        "Error fetching chats: " + error.getMessage(),
                        Toast.LENGTH_SHORT)
                    .show();
              }
            });

    queue.add(jsonArrayRequest);
  }

  private void parseChatData(JSONArray chatArray) {
    chatList.clear(); // Clear previous data if needed
    try {
      for (int i = 0; i < chatArray.length(); i++) {
        JSONObject chatObject = chatArray.getJSONObject(i);

        // Use 'clubName' instead of 'name' to get the club's name
        String clubName = chatObject.optString("clubName", "Unknown Chat");
        int clubId = chatObject.optInt("clubId", -1);

        // Add chat item to the list with clubName
        chatList.add(new Chat(clubName, R.drawable.cy, clubId));
      }
      chatAdapter.notifyDataSetChanged();
    } catch (JSONException e) {
      e.printStackTrace();
      Toast.makeText(this, "Error parsing chat data", Toast.LENGTH_SHORT).show();
    }
  }
}
