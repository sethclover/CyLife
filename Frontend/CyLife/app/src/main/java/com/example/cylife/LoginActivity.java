package com.example.cylife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);

        // Set up the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Set up the signup button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Basic input validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://coms-3090-065.class.las.iastate.edu:8080/login";  // For Android Emulator\n";  // Update with your correct login endpoint

        // Create the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send POST request to the backend
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Debug the API response
                        Log.d("API Response", response.toString());

                        // Check the response for successful login
                        boolean success = response.optString("message").equals("Login successful");
                        String userType = response.optString("userType"); // Get user type from response
                        String name = response.optString("name");
                        String userID = response.optString("userId");

                        if (success) {
                            // Open different activities based on user type
                            Intent intent;
                            switch (userType) {
                                case "student":
                                    intent = new Intent(LoginActivity.this, WelcomeActivityStudent.class);
                                    break;
                                case "admin":
                                    intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                    break;
                                case "club":
                                    intent = new Intent(LoginActivity.this, WelcomeActivityClub.class);
                                    break;
                                default:
                                    intent = new Intent(LoginActivity.this, WelcomeActivityStudent.class);
                                    break;
                            }
                            intent.putExtra("userId", userID);  // key-value to pass to the Welcome
                            intent.putExtra("username", name);  // key-value to pass to the Welcome
                            startActivity(intent);
                        } else {
                            //unsuccessful login
                            String errorMessage = response.optString("message", "Login failed");
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors like network issues, server down, etc.
                        Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
