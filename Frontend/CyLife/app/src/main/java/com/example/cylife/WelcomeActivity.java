//package com.example.cylife;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class WelcomeActivity extends AppCompatActivity {
//
//    private TextView welcomeText;
//    private Button orgButton;
//    private Button entButton;
//    private Button logoutButton;
//    private Button clubButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);
//
//        // Initialize views
//        welcomeText = findViewById(R.id.welcome_text);
//        orgButton = findViewById(R.id.org_button);
//        entButton = findViewById(R.id.ent_button);
//        clubButton = findViewById(R.id.club_button);
//        logoutButton = findViewById(R.id.logout_button);
//
//        // Set onClickListeners for buttons
//        orgButton.setOnClickListener(view -> {
//            // Start the Organization Activity
//            Intent intent = new Intent(WelcomeActivity.this, organizationActivity.class);
//            startActivity(intent);
//        });
//
//        entButton.setOnClickListener(view -> {
//            // Start the Events Activity
//            Intent intent = new Intent(WelcomeActivity.this, EventsActivity.class);
//            startActivity(intent);
//        });
//        clubButton.setOnClickListener(view -> {
//            // Start the Club Activity
//            Intent intent = new Intent(WelcomeActivity.this, clubActivity.class);
//            startActivity(intent);
//        });
//
//        logoutButton.setOnClickListener(view -> {
//            finish();
//        });
//    }
//}


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

public class WelcomeActivity extends AppCompatActivity {

    private TextView welcomeMessage;
    private Button viewEventsButton;
    private Button manageClubsButton;
    private Button viewRequestsButton;
    private Button viewClubsButton;
    private Button viewMembersButton;
    private Button chatButton;
    private Button accountButton;
    private Button logoutButton;
    private RecyclerView upcomingEventsRecyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userID", -1); // Same uppercase "ID" key
        if (userId != -1) {
            Log.d("WelcomeActivityStudent", "User ID: " + userId);
            fetchUserDetails(userId); // Fetching user details
        } else {
            Log.d("WelcomeActivityStudent", "User ID is null");
        }

        // Initialize views
        welcomeMessage = findViewById(R.id.welcomeMessage);
        viewEventsButton = findViewById(R.id.viewEventsButton);
        manageClubsButton = findViewById(R.id.manageClubsButton);
        viewRequestsButton = findViewById(R.id.viewRequestsButton);
        viewClubsButton = findViewById(R.id.viewClubs);
        viewMembersButton = findViewById(R.id.viewMembers);
        chatButton = findViewById(R.id.chatButton);
        accountButton = findViewById(R.id.bottomAccountButton);
        logoutButton = findViewById(R.id.bottomLogoutButton);
        upcomingEventsRecyclerView = findViewById(R.id.upcomingEventsRecyclerView);


        // Set welcome message
        String userName = getIntent().getStringExtra("userName"); // Pass userName from previous activity
        welcomeMessage.setText("Welcome " + (userName != null ? userName : "Admin"));

        // Set button actions
        viewEventsButton.setOnClickListener(view -> {
            Intent intentView = new Intent(WelcomeActivity.this, ShowEvents.class);
            startActivity(intentView);
        });

        manageClubsButton.setOnClickListener(view -> {
            Intent intent2 = new Intent(WelcomeActivity.this, clubActivity.class);
            startActivity(intent2);
        });

        viewRequestsButton.setOnClickListener(view -> {
            Intent intent3 = new Intent(WelcomeActivity.this, AccountActivity.class);
            startActivity(intent3);
        });

        viewClubsButton.setOnClickListener(view -> {
            Intent intent4 = new Intent(WelcomeActivity.this, ShowEvents.class);
            startActivity(intent4);
        });

        viewMembersButton.setOnClickListener(view -> {
            Intent intent5 = new Intent(WelcomeActivity.this, AccountActivity.class);
            startActivity(intent5);
        });

        chatButton.setOnClickListener(view -> {
            Intent intent6 = new Intent(WelcomeActivity.this, ChatActivity.class);
            startActivity(intent6);
        });

        accountButton.setOnClickListener(view -> {
            Intent intent7 = new Intent(WelcomeActivity.this, AccountActivity.class);
            startActivity(intent7);
        });

        logoutButton.setOnClickListener(view -> {
            // Log out and navigate back to login screen
            Intent intent8 = new Intent(WelcomeActivity.this, LoginActivity.class);
            intent8.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent8);
            finish();
        });

        upcomingEventsRecyclerView = findViewById(R.id.upcomingEventsRecyclerView);
        upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList);
        upcomingEventsRecyclerView.setAdapter(eventAdapter);


        fetchEvents("http://coms-3090-065.class.las.iastate.edu:8080/upcomingEvents");
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

                        // Get the "club" object
                        JSONObject club = user.optJSONObject("club");
                        int clubId = (club != null) ? club.optInt("clubId", -1) : -1;

                        Log.d("FetchUserDetails", "Retrieved Club ID: " + clubId);
                        Log.d("FetchUserDetails", "UserName: " + name);

                        // Update the welcome message
                        welcomeMessage.setText("Welcome " + name);

                        // Set up the chat button click listener
                        Button chatButton = findViewById(R.id.chatButton);
                        chatButton.setOnClickListener(v -> {
                            Intent chatIntent = new Intent(WelcomeActivity.this, ChatActivity.class);
                            chatIntent.putExtra("clubId", clubId); // Pass the clubId
                            chatIntent.putExtra("userID", userId); // Ensure this key matches exactly
                            chatIntent.putExtra("studentName", name);
                            startActivity(chatIntent);
                        });

                    } catch (JSONException e) {
                        Log.e("FetchUserDetails", "Error parsing JSON response: " + e.getMessage());
                        Toast.makeText(WelcomeActivity.this, "Invalid user data received", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("FetchUserDetails", "Error fetching user details: " + error.toString());
                    Toast.makeText(WelcomeActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
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
