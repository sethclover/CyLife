package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;


public class EventsActivity extends AppCompatActivity {

    private final String URL = "http://coms-3090-065.class.las.iastate.edu:8080/events";
//    private final String URL = "https://9e3fe20e-1ffb-409a-816f-4f2a2f121fab.mock.pstmn.io";


    private EditText etName, etLocation, etTime, etDescription, etEditID, etDeleteID;
    private Button createEventButton, editEventButton, getEventButton, deleteEventButton, logoutButton;
    private TextView orgListTextView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        // Implement event-related logic here

        logoutButton = findViewById(R.id.logout_button);

        etName = findViewById(R.id.event_name_input);
        etLocation = findViewById(R.id.location_input);
        etTime = findViewById(R.id.time_input);
        etDescription = findViewById(R.id.event_description_input);
        createEventButton = findViewById(R.id.CE);
        etEditID = findViewById(R.id.event_id_input);
        editEventButton = findViewById(R.id.EE);
        getEventButton = findViewById(R.id.GE);
        etDeleteID = findViewById(R.id.event_id_input2);
        deleteEventButton = findViewById(R.id.DE);

        requestQueue = Volley.newRequestQueue(this);
        logoutButton = findViewById(R.id.logout_button);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orgListTextView.setText("");
                createEvent();
            }
        });

        getEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orgListTextView.setText("");
                getEvents();
            }
        });

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = etDeleteID.getText().toString().trim();
                deleteEvent(eventId);
            }

        });


        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(EventsActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }


    // Function to post data and create an organization
    private void createEvent() {
        String url = URL + "/postEvent";

        // Get values from EditTexts
        String name = etName.getText().toString();
        String location = etLocation.getText().toString();
        String time = etTime.getText().toString();
        String description = etDescription.getText().toString();

        if (name.isEmpty() || location.isEmpty() || time.isEmpty() || description.isEmpty()) {
            orgListTextView.setText("All fields are required.");
            return;
        }

        // Prepare POST request data
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);
            postData.put("location", location);
            postData.put("time", time);
            postData.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a StringRequest to post the data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response (e.g., show success message)
                        orgListTextView.setText("Organization Created Successfully!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error (e.g., show error message)
                        orgListTextView.setText("Error creating organization: " + error.getMessage());
                    }
                }) {
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
        requestQueue.add(stringRequest);
    }

    // Function to get the list of organizations
    private void getEvents() {
        String url = URL + "/events";  // Use the correct API URL

        // Create a JsonObjectRequest (since the response is a JSON object)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Check if the response is successful
                            boolean success = response.getBoolean("success");

                            if (success) {
                                // Extract the organizations array
                                JSONArray organizations = response.getJSONArray("organizations");

                                // Prepare a string builder to store the organization names
                                StringBuilder orgNames = new StringBuilder();

                                // Iterate over the organizations array
                                for (int i = 0; i < organizations.length(); i++) {
                                    JSONObject org = organizations.getJSONObject(i);
                                    String orgName = org.getString("name");  // Extract the organization name

                                    // Append the organization name to the builder
                                    orgNames.append(orgName).append("\n");
                                }

                                // Update the TextView and make it visible
                                orgListTextView.setVisibility(View.VISIBLE);
                                orgListTextView.setText(orgNames.toString());

                            } else {
                                // If success is false, show an error message
                                orgListTextView.setVisibility(View.VISIBLE);
                                orgListTextView.setText("Failed to fetch organizations.");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            orgListTextView.setVisibility(View.VISIBLE);
                            orgListTextView.setText("Error parsing response.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log and display error
                        Log.e("VolleyError", error.toString());
                        orgListTextView.setVisibility(View.VISIBLE);
                        orgListTextView.setText("Error fetching organizations.");
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void deleteEvent(String eventId) {
        String url = URL + eventId;

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Instead of checking for "success", we'll just display the raw response for debugging
                        Toast.makeText(EventsActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMsg = new String(error.networkResponse.data);  // Extract error message from response
                            Toast.makeText(EventsActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EventsActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(deleteRequest);
    }


}

