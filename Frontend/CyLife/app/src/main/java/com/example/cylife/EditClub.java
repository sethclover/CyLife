package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class EditClub extends AppCompatActivity {

    private EditText nameField, oldPasswordField, newPasswordField, confirmPasswordField;
    private Button saveButton, backButton;

    private int clubId = 1; // TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_club);

        // Initialize views
        nameField = findViewById(R.id.etClubName);

        oldPasswordField = findViewById(R.id.etPasswordOld);
        newPasswordField = findViewById(R.id.etPasswordNew);
        confirmPasswordField = findViewById(R.id.etPasswordNewConfirm);
        backButton = findViewById(R.id.BackButton);

        saveButton = findViewById(R.id.SaveButton);
        //   backButton = findViewById(R.id.buttonLogin);

        // Handle sign-up logic
        saveButton.setOnClickListener(v -> {
            String name = nameField.getText().toString();
            String oldPassword = oldPasswordField.getText().toString();
            String newPassword = newPasswordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(EditClub.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // If inputs are valid, send data to the backend server
                editUser(name, newPassword);
            }
        });

        backButton.setOnClickListener(v -> {
            // Redirect to Login Activity
            Intent intent = new Intent(EditClub.this, WelcomeActivityStudent.class);
            startActivity(intent);
        });
    }

    private void editUser(String name, String password) {
        String putURL = "http://coms-3090-065.class.las.iastate.edu:8080/clubs/" + clubId;

        // Create JSON object with the input data
        JSONObject updatedUserData = new JSONObject();
        try {
            updatedUserData.put("clubName: ", name);
            updatedUserData.put("password: ", password);
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
