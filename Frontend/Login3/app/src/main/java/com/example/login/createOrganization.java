package com.example.login;

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

public class createOrganization extends AppCompatActivity {

    private EditText etOrgName, etEmail, etDirector, etAdvisor, etOrgId, dtOrg, OrgIdD;
    private Button createOrgButton, getOrgButton, btnDeleteOrg;
    private TextView orgListTextView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organisation);  // Set the layout first

        // Initialize fields and buttons
        etOrgName = findViewById(R.id.etOrgName);
        etEmail = findViewById(R.id.etEmail);
        etDirector = findViewById(R.id.etDirector);
        etAdvisor = findViewById(R.id.etAdvisor);
        createOrgButton = findViewById(R.id.CO);
        getOrgButton = findViewById(R.id.GO);
        orgListTextView = findViewById(R.id.orgListTextView);
        etOrgId = findViewById(R.id.etOrgId);
        btnDeleteOrg = findViewById(R.id.DO);
        dtOrg = findViewById(R.id.etOrgNameD);
        OrgIdD = findViewById(R.id.etOrgIdD);



        // Initialize the request queue for Volley
        requestQueue = Volley.newRequestQueue(this);

        // Set click listener for creating an organization
        createOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrganization();
            }
        });

        // Set click listener for fetching the list of organizations
        getOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrganizations();
            }
        });

        btnDeleteOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgId = OrgIdD.getText().toString().trim();
                String orgName = dtOrg.getText().toString().trim();

                // Validate inputs
                if (orgId.isEmpty() || orgName.isEmpty()) {
                    Toast.makeText(createOrganization.this, "Please enter both Organization ID and Name", Toast.LENGTH_SHORT).show();
                } else {
                    // Call method to delete organization
                    deleteOrganization(orgId, orgName);
                }
            }
        });
    }

    // Function to post data and create an organization
    private void createOrganization() {
        String url = "http://10.0.2.2:3000/org";  // Replace with your API endpoint

        // Get values from EditTexts
        String orgName = etOrgName.getText().toString();
        String email = etEmail.getText().toString();
        String orgId = etOrgId.getText().toString();
        String director = etDirector.getText().toString();
        String advisor = etAdvisor.getText().toString();

        // Validate input fields
        if (orgName.isEmpty() || email.isEmpty() || orgId.isEmpty() || director.isEmpty() || advisor.isEmpty()) {
            orgListTextView.setText("All fields are required.");
            return;
        }

        // Prepare POST request data
        JSONObject postData = new JSONObject();
        try {
            postData.put("orgName", orgName);
            postData.put("orgEmail", email);
            postData.put("orgId", orgId);
            postData.put("directorName", director);
            postData.put("studentAdvisorName", advisor);
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
        String url = "http://10.0.2.2:3000/org";  // Use the correct API URL

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
                                    String orgName = org.getString("orgName");  // Extract the organization name

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

    private void deleteOrganization(String orgId, String orgName) {
        // API URL for deleting the organization (replace with your actual API URL)
        String url = "http://10.0.2.2:3000/org";

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("orgID", orgId);
            jsonBody.put("orgName", orgName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create DELETE request
        JsonObjectRequest deleteRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(createOrganization.this, "Organization deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(createOrganization.this, "Failed to delete organization", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(createOrganization.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(createOrganization.this, "Error deleting organization", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        );

        // Add the request to the request queue
        requestQueue.add(deleteRequest);
    }


}
