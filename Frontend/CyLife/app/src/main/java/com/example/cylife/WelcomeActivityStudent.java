package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

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

public class WelcomeActivityStudent extends AppCompatActivity {

    private RecyclerView upcomingEventsRecyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private TextView welcomeMessage;
    private int clubId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_student);

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


        fetchEvents("http://coms-3090-065.class.las.iastate.edu:8080/events");

        Button viewEventsButton = findViewById(R.id.viewEventsButton);
        viewEventsButton.setOnClickListener(v -> startActivity(new Intent(WelcomeActivityStudent.this, ShowEvents.class)));

        Button joinClubButton = findViewById(R.id.joinClubButton);
        joinClubButton.setOnClickListener(v -> startActivity(new Intent(WelcomeActivityStudent.this, JoinClubActivity.class)));

        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> {
            Intent chatIntent = new Intent(WelcomeActivityStudent.this, ChatActivity.class);
            chatIntent.putExtra("clubId", clubId);
            chatIntent.putExtra("userID", userId); // Ensure this key matches exactly
            startActivity(chatIntent);
        });

        Button requestClubButton = findViewById(R.id.requestClubButton);
        requestClubButton.setOnClickListener(v -> startActivity(new Intent(WelcomeActivityStudent.this, RequestClub.class)));

        Button bottomAccountButton = findViewById(R.id.bottomAccountButton);
        bottomAccountButton.setOnClickListener(v -> startActivity(new Intent(WelcomeActivityStudent.this, EditUser.class)));

        Button bottomSettingsButton = findViewById(R.id.bottomLogoutButton);
        bottomSettingsButton.setOnClickListener(v -> finish());
        welcomeMessage = findViewById(R.id.welcomeMessage);
    }

    private void fetchUserDetails(int userId) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/user/" + userId;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // Log the entire response for debugging
                    Log.d("FetchUserDetails", "Response: " + response.toString());

                    // Get the student's name and clubId from the response
                    String studentName = response.optString("name", "Student");
                    clubId = response.optInt("clubId", -1);  // Correctly update the class-level clubId
                    Log.d("FetchUserDetails", "Retrieved Club ID: " + clubId);
                    welcomeMessage.setText("Welcome " + studentName);

                    // Pass clubId to ChatActivity via chat button
                    Button chatButton = findViewById(R.id.chatButton);
                    chatButton.setOnClickListener(v -> {
                        Intent chatIntent = new Intent(WelcomeActivityStudent.this, ChatActivity.class);
                        chatIntent.putExtra("clubId", clubId); // Pass the clubId
                        chatIntent.putExtra("userID", userId); // Ensure this key matches exactly
                        startActivity(chatIntent);
                    });

                },
                error -> {
                    error.printStackTrace();
                    Log.e("FetchUserDetails", "Error fetching user details: " + error.toString());
                    Toast.makeText(WelcomeActivityStudent.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonObjectRequest);
    }


    private void fetchEvents(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
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
}