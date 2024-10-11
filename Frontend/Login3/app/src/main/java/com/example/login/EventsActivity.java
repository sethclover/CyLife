package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventsActivity extends AppCompatActivity {

    private EditText etEventName, etEventLocation, etEventDescription, etEventNameDelete;
    private Button getEventButton, createEventButton;
    private Button deleteEventButton;
    private Button logoutButton;
    private TextView eventListTextView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize fields and buttons
        etEventName = findViewById(R.id.event_name_input);
        etEventLocation = findViewById(R.id.location_time_input);
        etEventDescription = findViewById(R.id.event_description_input);
        etEventNameDelete = findViewById(R.id.event_name_input2);
        createEventButton = findViewById(R.id.CE);
        getEventButton = findViewById(R.id.GE);
        deleteEventButton = findViewById(R.id.DE);
        eventListTextView = findViewById(R.id.eventListTextView);
        logoutButton = findViewById(R.id.logout_button);

        // Create Volley request queue
//        requestQueue = Volley.newRequestQueue(this);

        // Create event
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
                Toast.makeText(EventsActivity.this, "Event created", Toast.LENGTH_SHORT).show();
            }
        });
//        createEventButton.setOnClickListener(view -> {
//            createEvent();
//        });

        // Get event
        getEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEvent();
            }
        });

        // Delete event
        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = etEventNameDelete.getText().toString().trim();
                deleteEvent(eventName);
            }
        });

        // Logout functionality
        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(EventsActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Function to post data and create an event
    private void createEvent() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/events";

        // Get values from EditTexts
        String eventName = etEventName.getText().toString();
        String eventLocation = etEventLocation.getText().toString();
        String eventDescription = etEventDescription.getText().toString();

        if (eventName.isEmpty() || eventLocation.isEmpty() || eventDescription.isEmpty()) {
            Toast.makeText(EventsActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare POST request data
        JSONObject postData = new JSONObject();
        try {
            postData.put("eventName", eventName);
            postData.put("eventLocation", eventLocation);
            postData.put("description", eventDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a request to post the data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EventsActivity.this, "It's working", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error (e.g., show error message)
                        eventListTextView.setText("Error creating organization: " + error.getMessage());
                    }
                }){

            @Override
            public byte[] getBody() {
                return postData.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };


        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    // Function to get an event by name
    private void getEvent() {
        String url ="http://coms-3090-065.class.las.iastate.edu:8080/events";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Check if the "success" field is true
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // Extract the "events" array from the response
                                JSONArray events = response.getJSONArray("events");
                                StringBuilder eventDetails = new StringBuilder();

                                // Iterate over the events array and extract each event's details
                                for (int i = 0; i < events.length(); i++) {
                                    JSONObject event = events.getJSONObject(i);

                                    // Get event details
                                    String eventName = event.getString("eventName");
                                    String eventLocation = event.getString("eventLocation");
                                    String eventDescription = event.getString("eventDescription");

                                    // Append event details to the StringBuilder
                                    eventDetails.append("Event Name: ").append(eventName).append("\n")
                                            .append("Location: ").append(eventLocation).append("\n")
                                            .append("Description: ").append(eventDescription).append("\n\n");
                                }

                                // Update the TextView with the event details
                                eventListTextView.setVisibility(View.VISIBLE);
                                eventListTextView.setText(eventDetails.toString());
                            } else {
                                eventListTextView.setText("Failed to retrieve events.");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            eventListTextView.setText("Error parsing event data.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        eventListTextView.setText("Error fetching events: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    // Function to delete an event by name
    private void deleteEvent(String eventName) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/events/" + eventName;

        // Create a StringRequest for a DELETE request
        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EventsActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMsg = new String(error.networkResponse.data);
                            Toast.makeText(EventsActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EventsActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(deleteRequest);
    }
}
