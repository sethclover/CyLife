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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class createOrganization extends AppCompatActivity {

    private EditText etOrgName, etEmail, etOrgId, OrgIdD, etOrgIdE, etOrgNameE, etOgEmail;;
    private Button createOrgButton, getOrgButton, editOrg, btnDeleteOrg, logoutButton;
    private TextView orgListTextView, orgListTextView2;
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
        orgListTextView2 = findViewById(R.id.orgListTextView2);
        etOrgId = findViewById(R.id.etOrgId);
        editOrg = findViewById(R.id.EO);
        btnDeleteOrg = findViewById(R.id.DO);
        OrgIdD = findViewById(R.id.etOrgIdD);
        requestQueue = Volley.newRequestQueue(this);
        logoutButton = findViewById(R.id.logout_button);
        etOrgNameE = findViewById(R.id.etOrgNameE);
        etOrgIdE = findViewById(R.id.etOrgIdE);
        etOrgNameE = findViewById(R.id.etOrgNameE);
        etEmail = findViewById(R.id.etEmail);

        createOrgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orgListTextView2.setText("");
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

        editOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOrganization();
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
                        orgListTextView2.setText("Organization Created Successfully!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        orgListTextView2.setText("Error creating organization: " + error.getMessage());
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
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/organisations";  // Ensure this is the correct URL

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


                            // Prepare a string builder to store the organization names
                            StringBuilder orgDetails = new StringBuilder();

                            // Iterate over the organizations array
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject org = response.getJSONObject(i);
                                String orgId = org.optString("orgId", "Unknown ID");
                                String orgName = org.optString("name", "Unknown Organization");  // Extract the organization name (with fallback)

                                // Append the organization name to the builder
                                orgDetails.append("ID: ").append(orgId).append(" - Name: ").append(orgName).append("\n");
                            }

                            // Update the TextView and make it visible
                            orgListTextView.setVisibility(View.VISIBLE);
                            orgListTextView.setText(orgDetails.toString());

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
                        // Log the error for debugging
                        Log.e("VolleyError", error.toString());

                        // Show a user-friendly message in case of an error
                        orgListTextView.setVisibility(View.VISIBLE);
                        orgListTextView.setText("Error fetching organizations: " + error.getMessage());
                    }
                }
        );

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }


    private void editOrganization() {
        String orgId = etOrgIdE.getText().toString();
        String orgName = etOrgNameE.getText().toString();
        String email = etEmail.getText().toString();

        JSONObject updatedOrgData = new JSONObject();
        try {
            updatedOrgData.put("name", orgName);
            updatedOrgData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String url = "http://coms-3090-065.class.las.iastate.edu:8080/organisations/" + orgId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                updatedOrgData,
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
                        Toast.makeText(getApplicationContext(), "Error updating organization: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

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
