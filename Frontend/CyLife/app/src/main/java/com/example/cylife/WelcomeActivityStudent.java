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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_student);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userID", 84); // Same uppercase "ID" key
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
        chatButton.setOnClickListener(v -> startActivity(new Intent(WelcomeActivityStudent.this, ChatActivity.class)));

        Button bottomAccountButton = findViewById(R.id.bottomAccountButton);
        bottomAccountButton.setOnClickListener(v -> startActivity(new Intent(WelcomeActivityStudent.this, AccountActivity.class)));

        Button bottomSettingsButton = findViewById(R.id.bottomLogoutButton);
        bottomSettingsButton.setOnClickListener(v -> finish());
        welcomeMessage = findViewById(R.id.welcomeMessage);
    }

    private void fetchUserDetails(int userId) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/user/" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject userObj = response.getJSONObject("user");
                        String studentName = userObj.optString("name", "Student"); // Provide a default value if "name" is null
                        welcomeMessage.setText("Welcome " + studentName); // Set the welcome message
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("FetchUserDetails", "Error parsing user details: " + e.getMessage());
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("FetchUserDetails", "Error fetching user details: " + error.toString());
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