package com.example.cylife;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.concurrent.TimeUnit;

public class clubActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView screenTitle, createClubLabel, editClubLabel, deleteClubLabel;
    private EditText etClubNameCreate, etEmailCreate, etClubPass, etClubIdEdit, etClubNameEdit, etEmailEdit, etClubIdD;
    private Button createClubButton, editClubButton, deleteClubButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        screenTitle = findViewById(R.id.screenTitle);
        createClubLabel = findViewById(R.id.createClubLabel);
        editClubLabel = findViewById(R.id.editClubLabel);
        deleteClubLabel = findViewById(R.id.deleteClubLabel);

        etClubNameCreate = findViewById(R.id.etClubNameCreate);
        etEmailCreate = findViewById(R.id.etEmailCreate);
        etClubPass = findViewById(R.id.etClubPass);
        etClubIdEdit = findViewById(R.id.etClubIdEdit);
        etClubNameEdit = findViewById(R.id.etClubNameEdit);
        etEmailEdit = findViewById(R.id.etEmailEdit);
        etClubIdD = findViewById(R.id.etClubIdD);

        createClubButton = findViewById(R.id.createClubButton);
        editClubButton = findViewById(R.id.editClubButton);
        deleteClubButton = findViewById(R.id.DC);

        requestQueue = Volley.newRequestQueue(this);

        // Back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click (finish the activity or navigate back)
                finish();
            }
        });

        // Create Club button click listener
        createClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createClub();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Edit Club button click listener
        editClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editClub();
            }
        });

        // Delete Club button click listener
        deleteClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clubId = etClubIdD.getText().toString();
                deleteClub(clubId);
            }
        });
    }

    // Method to create a new club
    private void createClub() throws InterruptedException {
        // MAKING CLUB CLUB
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/clubs";

        // Get values from EditTexts
        String clubName = etClubNameCreate.getText().toString();
        String email = etEmailCreate.getText().toString();
        String pass = etClubPass.getText().toString();

        Log.d("Create Club Info", "clubName: " + clubName + ", email: " + email + ", email: " + "Password: " + pass);


        if (clubName.isEmpty() || email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare POST request data
        JSONObject postData = new JSONObject();
        try {
            postData.put("clubName", clubName);
            postData.put("clubEmail", email);
            postData.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a StringRequest to post the data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Club Created Successfully!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error creating club: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

        // MAKING CLUB USER

        String url2 = "http://coms-3090-065.class.las.iastate.edu:8080/signup";

        // Create JSON object with the input data
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("name", clubName);
            jsonBody.put("password", pass);
            jsonBody.put("type", "CLUB");
            jsonBody.put("international", false);
            jsonBody.put("multicultural", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send POST request using Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url2,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(clubActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(clubActivity.this, "Signup failed. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(clubActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(clubActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);

        requestQueue.add(stringRequest);

        // GETTING CLUB ID
        String urlClubs = "http://coms-3090-065.class.las.iastate.edu:8080/clubs";
        RequestQueue queue = Volley.newRequestQueue(this);

        final String[] newClubID = {null};

        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, urlClubs, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        TimeUnit.SECONDS.sleep(1);

                        JSONObject eventObj = response.getJSONObject(response.length()-1);
                        newClubID[0] = eventObj.optString("clubId", "No name");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("FetchEvents", "JSON parsing error: " + e.getMessage());
                    }
                    Log.e("gETTINGcLUBiD", "Got club ID " + newClubID[0]);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e("FetchEvents", "Network error: " + error.toString());
                }
            });

        queue.add(jsonArrayRequest2);


        // GETTING CLUB ID

        String urlUsers = "http://coms-3090-065.class.las.iastate.edu:8080/users";

        final String[] newUserID = {null};

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlUsers, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        JSONObject eventObj = response.getJSONObject(response.length()-1);
                        newUserID[0] = eventObj.optString("userId", "No name");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("FetchEvents", "JSON parsing error: " + e.getMessage());
                    }
                    Log.e("gETTINGuSERiD", "Got user ID " + newUserID[0]);
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

        // MAKING CLUB USER JOIN CLUB CLUB

        String url3= "http://coms-3090-065.class.las.iastate.edu:8080/joinClub/" + newUserID[0] + "/" + newClubID[0];  // TODO add actual id's

        TimeUnit.SECONDS.sleep(3);
        JsonObjectRequest joinRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url3,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(this, "Joined " + clubName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to join club", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error joining club", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Join Club", "Error: " + error.toString());
                    Toast.makeText(this, "Failed to join club", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(joinRequest);
    }
    private void editClub() {
        String clubId = etClubIdEdit.getText().toString();
        String clubName = etClubNameEdit.getText().toString();
        String email = etEmailEdit.getText().toString();

        // Log the values for debugging
        Log.d("Edit Club Info", "clubId: " + clubId + ", clubName: " + clubName + ", email: " + email);

        // Validate inputs
        if (clubId.isEmpty() || clubName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject updatedClubData = new JSONObject();
        try {
            updatedClubData.put("clubName", clubName);
            updatedClubData.put("clubEmail", email);
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
                        Log.d("Edit Response", "Response: " + response.toString());
                        Toast.makeText(getApplicationContext(), "Club updated successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
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