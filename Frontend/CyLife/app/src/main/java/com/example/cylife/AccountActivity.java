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
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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

        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show();
            // TODO complete the change password logic
        });

        btnDeleteUser.setOnClickListener(v -> {
            Toast.makeText(this, "Delete User clicked", Toast.LENGTH_SHORT).show();
            //TODO delete the user account
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
                            Picasso.get().load(profilePicUrl).placeholder(R.drawable.account).into(profileImage);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AccountActivity.this, "Error parsing user details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(AccountActivity.this, "Error fetching user details", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }


// old fetcher code from welcomeStudents
//    private void fetchUserDetails(int userId) {
//        String url = "http://coms-3090-065.class.las.iastate.edu:8080/user/" + userId;
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                response -> {
//                    try {
//                        // Log the entire response for debugging
//                        Log.d("FetchUserDetails", "Response: " + response.toString());
//
//                        // Get the "user" object
//                        JSONObject user = response.optJSONObject("user");
//                        if (user == null) {
//                            throw new JSONException("User object is missing in response");
//                        }
//
//                        // Get the user's name with a fallback value
//                        String name = user.optString("name", "Student");
//
//                        // Get the "club" object
//                        JSONObject club = user.optJSONObject("club");
//                        int clubId = (club != null) ? club.optInt("clubId", -1) : -1;
//
//                        Log.d("FetchUserDetails", "Retrieved Club ID: " + clubId);
//                        Log.d("FetchUserDetails", "UserName: " + name);
//
//
//                        // Set up the chat button click listener
//                        Button chatButton = findViewById(R.id.chatButton);
//                        chatButton.setOnClickListener(v -> {
//                            Intent chatIntent = new Intent(AccountActivity.this, ChatActivity.class);
//                            chatIntent.putExtra("clubId", clubId); // Pass the clubId
//                            chatIntent.putExtra("userID", userId); // Ensure this key matches exactly
//                            chatIntent.putExtra("studentName", name);
//                            startActivity(chatIntent);
//                        });
//
//                    } catch (JSONException e) {
//                        Log.e("FetchUserDetails", "Error parsing JSON response: " + e.getMessage());
//                        Toast.makeText(AccountActivity.this, "Invalid user data received", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> {
//                    error.printStackTrace();
//                    Log.e("FetchUserDetails", "Error fetching user details: " + error.toString());
//                    Toast.makeText(AccountActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
//                });
//
//        queue.add(jsonObjectRequest);
//    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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