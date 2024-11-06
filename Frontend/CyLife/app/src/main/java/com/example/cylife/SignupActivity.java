package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText emailField, nameField, passwordField, confirmPasswordField;
    private CheckBox internationalCheckbox, multiculturalCheckbox;
    private Button signupButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        emailField = findViewById(R.id.email);
        nameField = findViewById(R.id.name);

        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_password);

        internationalCheckbox = findViewById(R.id.international_checkbox);
        multiculturalCheckbox = findViewById(R.id.multicultural_checkbox);

        signupButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.buttonLogin);

        // Handle sign-up logic
        signupButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String name = nameField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();
            boolean isInternational = internationalCheckbox.isChecked();
            boolean isMulticultural = multiculturalCheckbox.isChecked();

            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // If inputs are valid, send data to the backend server
                signupUser(email, name, password, isInternational, isMulticultural);
            }
        });

        loginButton.setOnClickListener(v -> {
            // Redirect to Login Activity
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void signupUser(String email, String name, String password, boolean isInternational, boolean isMulticultural) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/signup";

        // Create JSON object with the input data
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("name", name);
            jsonBody.put("password", password);
            jsonBody.put("userType", "STUDENT");
            jsonBody.put("international", isInternational);
            jsonBody.put("multicultural", isMulticultural);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send POST request using Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup failed. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}

