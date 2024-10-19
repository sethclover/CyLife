package com.example.cylife;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class clubActivity extends AppCompatActivity {

    private EditText etClubName, etEmail, etClubId, ClubIdD, etClubIdE, etClubNameE, etClubEmail;
    private Button createClubButton, getClubButton, editClub, btnDeleteClub, logoutButton;
    private TextView clubListTextView, clubListTextView2;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        // Initialize fields and buttons
        etClubName = findViewById(R.id.etClubName);
        etEmail = findViewById(R.id.etEmail);
        createClubButton = findViewById(R.id.CC);
        getClubButton = findViewById(R.id.GC);
        clubListTextView = findViewById(R.id.clubListTextView);
        clubListTextView2 = findViewById(R.id.clubListTextView2);
        etClubId = findViewById(R.id.etClubId);
        editClub = findViewById(R.id.EC);
        btnDeleteClub = findViewById(R.id.DC);
        ClubIdD = findViewById(R.id.etClubIdD);
        requestQueue = Volley.newRequestQueue(this);
        logoutButton = findViewById(R.id.logout_button);
        etClubNameE = findViewById(R.id.etClubNameE);
        etClubEmail = findViewById(R.id.etClubEmail);
        etClubIdE = findViewById(R.id.etClubIdE);
        etClubNameE = findViewById(R.id.etClubNameE);
        etEmail = findViewById(R.id.etEmail);

        createClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clubListTextView2.setText("");
                createClubs();
            }
        });

        getClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clubListTextView.setText("");
                getClubs();
            }
        });

        editClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClub();
            }
        });


        btnDeleteClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clubId = ClubIdD.getText().toString().trim();
                deleteClub(clubId);
            }

        });

        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(clubActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });

    }

    // Function to post data and create an club
    private void createClubs() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/clubs";

        // Get values from EditTexts
        String clubName = etClubName.getText().toString();
        String email = etEmail.getText().toString();
        String clubId = etClubId.getText().toString();

        if (clubName.isEmpty() || email.isEmpty() || clubId.isEmpty()) {
            clubListTextView.setText("All fields are required.");
            return;
        }

        // Prepare POST request data
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", clubName);
            postData.put("email", email);
            postData.put("clubId", clubId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a StringRequest to post the data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        clubListTextView2.setText("Club Created Successfully!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        clubListTextView2.setText("Error creating club: " + error.getMessage());
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

    // Function to get the list of clubs
    private void getClubs() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/clubs";  // Ensure this is the correct URL

        // Create a JsonArrayRequest (since the response is a JSON array)
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Log the response for debugging purposes
                            Log.d("API Response", response.toString());


                            // Prepare a string builder to store the club names
                            StringBuilder clubDetails = new StringBuilder();

                            // Iterate over the clubs array
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject club = response.getJSONObject(i);
                                String clubId = club.optString("clubId", "Unknown ID");
                                String clubName = club.optString("name", "Unknown Club");  // Extract the club name (with fallback)

                                // Append the club name to the builder
                                clubDetails.append("ID: ").append(clubId).append(" - Name: ").append(clubName).append("\n");
                            }

                            // Update the TextView and make it visible
                            clubListTextView.setVisibility(View.VISIBLE);
                            clubListTextView.setText(clubDetails.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            clubListTextView.setVisibility(View.VISIBLE);
                            clubListTextView.setText("Error parsing response.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error for debugging
                        Log.e("VolleyError", error.toString());

                        // Show a user-friendly message in case of an error
                        clubListTextView.setVisibility(View.VISIBLE);
                        clubListTextView.setText("Error fetching clubs: " + error.getMessage());
                    }
                }
        );

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }


    private void editClub() {
        String clubId = etClubIdE.getText().toString();
        String clubName = etClubNameE.getText().toString();
        String email = etClubEmail.getText().toString();

        JSONObject updatedClubData = new JSONObject();
        try {
            updatedClubData.put("name", clubName);
            updatedClubData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String url = "http://coms-3090-065.class.las.iastate.edu:8080/clubs/" + clubId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                updatedClubData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log the response for debugging
                        Log.d("Edit Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error for debugging
                        error.printStackTrace();

                        // Display a user-friendly error message
                        Toast.makeText(getApplicationContext(), "Error updating club: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void deleteClub(String clubId) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/clubs/" + clubId;

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Instead of checking for "success", we'll just display the raw response for debugging
                        Toast.makeText(clubActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMsg = new String(error.networkResponse.data);  // Extract error message from response
                            Toast.makeText(clubActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(clubActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(deleteRequest);
    }



}
