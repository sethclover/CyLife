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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivityClub extends AppCompatActivity implements WebSocketListener {

    private TextView joiningText;
    private TextView welcomeText;
    private Button entButton;
    private Button logoutButton;
    private Button clubButton;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private RecyclerView upcomingEventsRecyclerView;

    private String email;
    private String clubName;
    private Integer clubId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_club);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userID", -1); // Same uppercase "ID" key
        if (userId != -1) {
            Log.d("WelcomeActivityStudent", "User ID: " + userId);
            fetchUserDetails(userId); // Fetching user details
        } else {
            Log.d("WelcomeActivityStudent", "User ID is null");
        }

        upcomingEventsRecyclerView = findViewById(R.id.upcomingEventsRecyclerView);
        upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList);
        upcomingEventsRecyclerView.setAdapter(eventAdapter);

        String serverUrl = "http://coms-3090-065.class.las.iastate.edu:8080/joinClub/" + clubId + "/" + clubId;

        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(WelcomeActivityClub.this);

        // Initialize views
        joiningText = findViewById(R.id.joining_text);
        entButton = findViewById(R.id.ent_button);
        clubButton = findViewById(R.id.edit_club_button);
        logoutButton = findViewById(R.id.logout_button);
        welcomeText = findViewById(R.id.welcomeMessage);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> {
            Intent chatIntent = new Intent(WelcomeActivityClub.this, ChatActivity.class);
            chatIntent.putExtra("clubId", clubId);
            chatIntent.putExtra("userID", userId);
            chatIntent.putExtra("name", clubName);// Ensure this key matches exactly
            startActivity(chatIntent);
        });

        entButton.setOnClickListener(view -> {
            // Start the Events Activity
            Intent intent1 = new Intent(WelcomeActivityClub.this, ClubCreateEvent.class);
            intent1.putExtra("clubId", clubId);
            startActivity(intent1);
        });
        clubButton.setOnClickListener(view -> {
            // Start the edit Club Activity
            Intent intent1 = new Intent(WelcomeActivityClub.this, EditClub.class);
            intent1.putExtra("clubId", clubId);
            startActivity(intent1);
        });

        logoutButton.setOnClickListener(view -> {
            finish();
        });

        Log.d("DEBUGGING", "Club user " + userId + " " + clubId + " has logged in");

    }


    private void fetchUserDetails(int userId) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/user/" + userId;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Log the entire response for debugging
                        Log.d("FetchUserDetails", "Response: " + response.toString());

                        // Get the "user" object
                        JSONObject user = response.optJSONObject("user");
                        if (user == null) {
                            throw new JSONException("User object is missing in response");
                        }

                        // Get the user's name with a fallback value
                        String name = user.optString("name", "Student");

                        email = user.optString("email", "noEmail");
                        fetchClub("http://coms-3090-065.class.las.iastate.edu:8080/clubId/" + email);

                        // Get the "club" object
                        JSONObject club = user.optJSONObject("club");
                        int clubId = (club != null) ? club.optInt("clubId", -1) : -1;

                        Log.d("FetchUserDetails", "Retrieved Club ID: " + clubId);
                        Log.d("FetchUserDetails", "UserName: " + name);
                        fetchEvents(userId);
                        welcomeText.setText(name);

                        // Set up the chat button click listener
                        Button chatButton = findViewById(R.id.chatButton);
                        chatButton.setOnClickListener(v -> {
                            Intent chatIntent = new Intent(WelcomeActivityClub.this, ChatActivity.class);
                            chatIntent.putExtra("clubId", clubId); // Pass the clubId
                            chatIntent.putExtra("userID", userId); // Ensure this key matches exactly
                            chatIntent.putExtra("studentName", name);
                            startActivity(chatIntent);
                        });

                    } catch (JSONException e) {
                        Log.e("FetchUserDetails", "Error parsing JSON response: " + e.getMessage());
                        Toast.makeText(WelcomeActivityClub.this, "Invalid user data received", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("FetchUserDetails", "Error fetching user details: " + error.toString());
                    Toast.makeText(WelcomeActivityClub.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonObjectRequest);
    }


    private void fetchClub(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        // Parse the integer response as a String
                        String clubID = response.trim(); // Trim any extra spaces
                        clubId = Integer.parseInt(clubID);
                    } catch (Exception e) {
                        Log.e("FetchClubIDByEmail", "Parsing error: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("FetchClubIDByEmail", "Network error: " + error.toString());
                });


        queue.add(stringRequest);
    }

    private void fetchEvents(int userId) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/upcomingEvents/" + userId;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (eventList != null)
                                eventList.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject eventObj = response.getJSONObject(i);

                                String eventName = eventObj.optString("eventName", "No name");
                                String date = eventObj.optString("date", "No date");
                                String location = eventObj.optString("eventLocation", "No location");
                                String eventDescription = eventObj.optString("description", "No description");

                                Event event = new Event(eventName, date, location, eventDescription);
                                eventList.add(event);
                            }

                            eventAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchEvents", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("FetchEvents", "Network error: " + error.toString());
                    }
                });

        queue.add(jsonArrayRequest);
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
