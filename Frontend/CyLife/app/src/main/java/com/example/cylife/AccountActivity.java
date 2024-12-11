package com.example.cylife;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final Logger log = LoggerFactory.getLogger(AccountActivity.class);

    private ImageView profileImage;
    private TextView userName, userEmail, clubName, editProfilePictureText, clubDescriptionTextView;
    private Button btnChangePassword, btnDeleteUser, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intentAcc = getIntent();
        int userId = intentAcc.getIntExtra("userID", -1);
        Log.d("UserID Debug", String.format("userID: %d", userId));

        // Initialize UI components
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        clubName = findViewById(R.id.clubName);
//        clubDescriptionTextView = findViewById(R.id.clubDescription);
        editProfilePictureText = findViewById(R.id.editProfilePictureText);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
        btnLogout = findViewById(R.id.btnLogout);

        fetchUserDetails(userId);

        editProfilePictureText.setOnClickListener(v -> openImagePicker());

        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog(userId));

        btnDeleteUser.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://coms-3090-065.class.las.iastate.edu:8080/delete/" + userId;
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                    response -> {
                        Toast.makeText(this, "User account deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    },
                    error -> {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            if (statusCode == 500) {
                                Toast.makeText(this, "Server error. Please contact support.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Error " + statusCode + ": Unable to delete account.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("AccountActivity", "Error deleting user: " + error.toString());
                    });
            requestQueue.add(stringRequest);
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(AccountActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void fetchUserDetails(int userId) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/user/" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONObject user = response.getJSONObject("user");

                        String name = user.optString("name", "N/A");
                        String email = user.optString("email", "N/A");

                        // Extract clubs array
                        JSONArray clubsArray = user.optJSONArray("clubs");

                        // Prepare a list to store club names
                        List<String> clubNames = new ArrayList<>();

                        if (clubsArray != null) {
                            for (int i = 0; i < clubsArray.length(); i++) {
                                JSONObject club = clubsArray.getJSONObject(i);

                                String clubName = club.optString("clubName", "Unknown Club");
                                clubNames.add(clubName);
                            }
                        }

                        // Display user details
                        userName.setText(name);
                        userEmail.setText(email);

                        // Display clubs
                        String clubsText = "Clubs: " + String.join(", ", clubNames);
                        clubName.setText(clubsText); // Display the list of club names in your TextView

                    } catch (JSONException e) {
                        // Log the exception for debugging purposes
                        Log.e("fetchUserDetails", "Error parsing user details", e);
                        Toast.makeText(AccountActivity.this, "Error parsing user details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Log the error for debugging purposes
                    Log.e("fetchUserDetails", "Error fetching user details", error);
                    Toast.makeText(AccountActivity.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }



    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void showChangePasswordDialog(int userId) {
        // Create a dialog to take input for current and new passwords
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Change Password");

        // Create a layout for the dialog
        @SuppressLint("InflateParams")
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);

        // Input fields
        android.widget.EditText currentPasswordInput = dialogView.findViewById(R.id.currentPasswordInput);
        android.widget.EditText newPasswordInput = dialogView.findViewById(R.id.newPasswordInput);
        android.widget.EditText confirmPasswordInput = dialogView.findViewById(R.id.confirmPasswordInput);

        builder.setPositiveButton("Change", (dialog, which) -> {
            String currentPassword = currentPasswordInput.getText().toString();
            String newPassword = newPasswordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (newPassword.isEmpty() || confirmPassword.isEmpty() || currentPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call a method to update the password on the server
            changePassword(userId, newPassword);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

    private void changePassword(int userId, String password) {
        String putURL = "http://coms-3090-065.class.las.iastate.edu:8080/update/byId/" + userId;

        // Create JSON object with the input data
        JSONObject updatedUserData = new JSONObject();
        try {
            updatedUserData.put("password", password);
            Log.i("Updated Password: ", updatedUserData.toString());
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
                        Toast.makeText(AccountActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);

            // TODO: Add logic to upload the new profile picture to the server
            Toast.makeText(this, "Profile picture updated locally. Add server upload functionality.", Toast.LENGTH_SHORT).show();
        }
    }
}