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

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImage;
    private TextView userName, userEmail, clubName, editProfilePictureText, clubDescriptionTextView;
    private Button btnChangePassword, btnDeleteUser, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intentAcc = getIntent();
        int userId = intentAcc.getIntExtra("userID", -1);

        // Initialize UI components
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        clubName = findViewById(R.id.clubName);
        clubDescriptionTextView = findViewById(R.id.clubDescription);
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

                        JSONObject club = user.optJSONObject("club");
                        String clubNameValue = "None";
                        String clubDescriptionValue = "No description available";

                        if (club != null) {
                            clubNameValue = club.optString("clubName", "None");
                            clubDescriptionValue = club.optString("description", "No description available");
                        }

                        userName.setText(name);
                        userEmail.setText(email);

                        clubName.setText(clubNameValue);
                        clubDescriptionTextView.setText(clubDescriptionValue);

                        String profilePicUrl = user.optString("profilePicture", null);
                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//                            Picasso.get().load(profilePicUrl).placeholder(R.drawable.account).into(profileImage);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AccountActivity.this, "Error parsing user details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(AccountActivity.this, "Error fetching user details", Toast.LENGTH_SHORT).show()
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
            changePassword(userId, currentPassword, newPassword);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

    private void changePassword(int userId, String currentPassword, String newPassword) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/update/byId/" + userId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject requestBody = new JSONObject();
        try {
//            requestBody.put("userId", userId);
            requestBody.put("currentPassword", currentPassword);
            requestBody.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating request body", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                requestBody,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");

                        if (success) {
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        Toast.makeText(this, "Error " + statusCode + ": Unable to change password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
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