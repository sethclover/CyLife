package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class EditClub extends AppCompatActivity {

    private TextView welcomeText;
    private EditText nameField, etDesc;
    private Button saveButton, backButton;

    private Integer clubId = -4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_club);

        // Initialize views
        welcomeText = findViewById(R.id.club_name_text);

        nameField = findViewById(R.id.etClubName);

        etDesc = findViewById(R.id.etDesc);
        backButton = findViewById(R.id.BackButton);

        saveButton = findViewById(R.id.SaveButton);


        Intent intent = getIntent();
        clubId = intent.getIntExtra("clubId", -3); // Same uppercase "ID" key

        welcomeText.setText("Welcome " + clubId);

        // Handle sign-up logic
        saveButton.setOnClickListener(v -> {
            String name = nameField.getText().toString();
            String desc = etDesc.getText().toString();

            editUser(name, desc);

        });

        backButton.setOnClickListener(v -> {
            // Redirect to Login Activity
            finish();
        });
    }

    private void editUser(String name, String desc) {
        String putURL = "http://coms-3090-065.class.las.iastate.edu:8080/clubs/" + clubId;

        // Create JSON object with the input data
        JSONObject updatedUserData = new JSONObject();
        try {
            updatedUserData.put("name", name);
            updatedUserData.put("description", desc);
            Log.i("Updated Organisation Data JSON Object Before: ", updatedUserData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Send POST request using Volley
        JsonObjectRequest putRequest = new JsonObjectRequest(
                Request.Method.PUT,
                putURL,
                updatedUserData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log the response for debugging
                        Log.i("Success Response: ", updatedUserData.toString());
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(putRequest);
    }
}
