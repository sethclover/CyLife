package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cylife.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowEvents extends AppCompatActivity {
    private TextView eventsTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        // Initialize the TextView and Button
        eventsTextView = findViewById(R.id.eventListTextView);
        logoutButton = findViewById(R.id.logout_button);

        fetchEvents();

        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(ShowEvents.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Function to fetch events from the server
    private void fetchEvents() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/events"; // Update to the correct server URL

        // Create a JsonObjectRequest (since the response is a JSON object)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Log the response for debugging purposes
                            Log.d("API Response", response.toString());

                            // Get the events array from the JSON response
                            JSONArray eventsArray = response.optJSONArray("events");

                            if (eventsArray != null) {
                                // Prepare a string builder to store the event details
                                StringBuilder eventDetails = new StringBuilder();

                                // Iterate over the events array and append details to the StringBuilder
                                for (int i = 0; i < eventsArray.length(); i++) {
                                    JSONObject events = eventsArray.getJSONObject(i);
                                    String eventName = events.optString("eventName", "Unknown Event");
                                    String eventLocation = events.optString("eventLocation", "Location not available");
                                    String eventDescription = events.optString("Description", "Description not available");

                                    // Append the event details to the builder
                                    eventDetails.append("Event: ").append(eventName)
                                            .append("\nLocation: ").append(eventLocation)
                                            .append("\nDescription: ").append(eventDescription)
                                            .append("\n\n");
                                }

                                // Display the event details in the TextView
                                eventsTextView.setText(eventDetails.toString());

                            } else {
                                // Handle the case where there are no events
                                eventsTextView.setText("N events available.");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            eventsTextView.setText("Error parsing response.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error for debugging
                        Log.e("VolleyError", error.toString());

                        // Show a user-friendly message in case of an error
                        eventsTextView.setText("Error fetching events: " + error.getMessage());
                    }
                }
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}
