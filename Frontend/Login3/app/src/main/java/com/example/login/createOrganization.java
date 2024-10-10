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

import java.text.BreakIterator;

public class createOrganization extends AppCompatActivity {

    private EditText etOrgName, etEmail, etOrgId, OrgIdD;
    private Button createOrgButton, getOrgButton, btnDeleteOrg, logoutButton;
    private TextView orgListTextView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organisation);

        // Initialize fields and buttons
        etOrgName = findViewById(R.id.etOrgName);
        etEmail = findViewById(R.id.etEmail);
        createOrgButton = findViewById(R.id.CO);
        getOrgButton = findViewById(R.id.GO);
        orgListTextView = findViewById(R.id.orgListTextView);
        etOrgId = findViewById(R.id.etOrgId);
        btnDeleteOrg = findViewById(R.id.DO);
        OrgIdD = findViewById(R.id.etOrgIdD);
        requestQueue = Volley.newRequestQueue(this);
        logoutButton = findViewById(R.id.logout_button);


        createOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orgListTextView.setText("");
                createOrganization();
            }
        });

        getOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orgListTextView.setText("");
                getOrganizations();
            }
        });

        btnDeleteOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgId = OrgIdD.getText().toString().trim();
                    deleteOrganization(orgId);
                }

        });

        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(createOrganization.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });

    }

    // Function to post data and create an organization
    private void createOrganization() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/organisations";

        // Get values from EditTexts
        String orgName = etOrgName.getText().toString();
        String email = etEmail.getText().toString();
        String orgId = etOrgId.getText().toString();

        if (orgName.isEmpty() || email.isEmpty() || orgId.isEmpty()) {
            orgListTextView.setText("All fields are required.");
            return;
        }

        // Prepare POST request data
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", orgName);
            postData.put("email", email);
            postData.put("orgId", orgId);
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
    private void getOrganizations() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/organisations";  // Use the correct API URL

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

    private void deleteOrganization(String orgId) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/organisations/" + orgId;

        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Instead of checking for "success", we'll just display the raw response for debugging
                        Toast.makeText(createOrganization.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMsg = new String(error.networkResponse.data);  // Extract error message from response
                            Toast.makeText(createOrganization.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(createOrganization.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(deleteRequest);
    }



}
