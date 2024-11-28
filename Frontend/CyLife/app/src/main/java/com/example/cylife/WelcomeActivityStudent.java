package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivityStudent extends AppCompatActivity {

  private RecyclerView upcomingEventsRecyclerView;
  private EventAdapter eventAdapter;
  private List<Event> eventList = new ArrayList<>();
  private TextView welcomeMessage;
  private int clubId = -1;
  private String studentName = "Guest"; // Default name in case fetch fails

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome_student);

    Intent intent = getIntent();
    int userId = intent.getIntExtra("userID", -1); // Same uppercase "ID" key
    if (userId != -1) {
      Log.d("WelcomeActivityStudent", "User ID: " + userId);
      fetchUserDetails(userId); // Fetching user details
    } else {
      Log.d("WelcomeActivityStudent", "User ID is null");
    }

    upcomingEventsRecyclerView = findViewById(R.id.upcomingEventsRecyclerView);
    upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    eventAdapter = new EventAdapter(eventList);
    upcomingEventsRecyclerView.setAdapter(eventAdapter);

    fetchEvents("http://coms-3090-065.class.las.iastate.edu:8080/events");

    Button viewEventsButton = findViewById(R.id.viewEventsButton);
    viewEventsButton.setOnClickListener(
        v -> startActivity(new Intent(WelcomeActivityStudent.this, ShowEvents.class)));

    Button joinClubButton = findViewById(R.id.joinClubButton);
    joinClubButton.setOnClickListener(
        v -> {
          Intent joinIntent = new Intent(WelcomeActivityStudent.this, JoinClubActivity.class);
          joinIntent.putExtra("userId", userId);
          joinIntent.putExtra("name", studentName);
          startActivity(joinIntent);
        });

    Button chatButton = findViewById(R.id.chatButton);
    chatButton.setOnClickListener(
        v -> {
          Intent chatIntent = new Intent(WelcomeActivityStudent.this, ChatActivity.class);
          chatIntent.putExtra("clubId", clubId);
          chatIntent.putExtra("userID", userId);
          chatIntent.putExtra("name", studentName); // Ensure this key matches exactly
          startActivity(chatIntent);
        });

    Button requestClubButton = findViewById(R.id.requestClubButton);
    requestClubButton.setOnClickListener(
        v -> startActivity(new Intent(WelcomeActivityStudent.this, RequestClub.class)));

    Button bottomAccountButton = findViewById(R.id.bottomAccountButton);
    bottomAccountButton.setOnClickListener(
        v -> {
          Intent editIntent = new Intent(WelcomeActivityStudent.this, EditUser.class);
          //            editIntent.putExtra("userId", userId);
          //            editIntent.putExtra("username", name); // Ensure this key matches exactly
          startActivity(editIntent);
        });

    Button bottomSettingsButton = findViewById(R.id.bottomLogoutButton);
    bottomSettingsButton.setOnClickListener(v -> finish());
    welcomeMessage = findViewById(R.id.welcomeMessage);
  }

  //Below is the code for when the return response for user: clubID is an INT instead of json->json
  //    private void fetchUserDetails(int userId) {
  //        String url = "http://coms-3090-065.class.las.iastate.edu:8080/user/" + userId;
  //        RequestQueue queue = Volley.newRequestQueue(this);
  //
  //        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
  // null,
  //                response -> {
  //                    // Log the entire response for debugging
  //                    Log.d("FetchUserDetails", "Response: " + response.toString());
  //
  //                    // Get the student's name and clubId from the response
  //                    String name = response.optString("name", "Student");
  //                    clubId = response.optInt("clubId", -1);  // Correctly update the class-level
  // clubId
  //                    Log.d("FetchUserDetails", "Retrieved Club ID: " + clubId);
  //                    Log.d("FetchUserDetails", "UserName: " + name);
  //                    welcomeMessage.setText("Welcome " + name);
  //
  //                    Button chatButton = findViewById(R.id.chatButton);
  //                    chatButton.setOnClickListener(v -> {
  //                        Intent chatIntent = new Intent(WelcomeActivityStudent.this,
  // ChatActivity.class);
  //                        chatIntent.putExtra("clubId", clubId); // Pass the clubId
  //                        chatIntent.putExtra("userID", userId); // Ensure this key matches
  // exactly
  //                        chatIntent.putExtra("studentName", name);
  //                        startActivity(chatIntent);
  //                    });
  //
  //                },
  //                error -> {
  //                    error.printStackTrace();
  //                    Log.e("FetchUserDetails", "Error fetching user details: " +
  // error.toString());
  //                    Toast.makeText(WelcomeActivityStudent.this, "Failed to fetch user details",
  // Toast.LENGTH_SHORT).show();
  //                });
  //
  //        queue.add(jsonObjectRequest);
  //    }

  private void fetchUserDetails(int userId) {
    String url = "http://coms-3090-065.class.las.iastate.edu:8080/user/" + userId;
    RequestQueue queue = Volley.newRequestQueue(this);

    JsonObjectRequest jsonObjectRequest =
        new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            response -> {
              try {
                // Log the entire response for debugging
                Log.d("FetchUserDetails", "Response: " + response.toString());

                // Get the "user" object
                JSONObject user = response.optJSONObject("user");
                if (user == null) {
                  throw new JSONException("User object is missing in response");
                }

                // Get the user's name with a fallback value
                String name = user.optString("name", "Student");

                // Get the "club" object
                JSONObject club = user.optJSONObject("club");
                int clubId = (club != null) ? club.optInt("clubId", -1) : -1;

                Log.d("FetchUserDetails", "Retrieved Club ID: " + clubId);
                Log.d("FetchUserDetails", "UserName: " + name);

                // Update the welcome message
                welcomeMessage.setText("Welcome " + name);

                // Set up the chat button click listener
                Button chatButton = findViewById(R.id.chatButton);
                chatButton.setOnClickListener(
                    v -> {
                      Intent chatIntent =
                          new Intent(WelcomeActivityStudent.this, ChatActivity.class);
                      chatIntent.putExtra("clubId", clubId); // Pass the clubId
                      chatIntent.putExtra("userID", userId); // Ensure this key matches exactly
                      chatIntent.putExtra("studentName", name);
                      startActivity(chatIntent);
                    });

              } catch (JSONException e) {
                Log.e("FetchUserDetails", "Error parsing JSON response: " + e.getMessage());
                Toast.makeText(
                        WelcomeActivityStudent.this,
                        "Invalid user data received",
                        Toast.LENGTH_SHORT)
                    .show();
              }
            },
            error -> {
              error.printStackTrace();
              Log.e("FetchUserDetails", "Error fetching user details: " + error.toString());
              Toast.makeText(
                      WelcomeActivityStudent.this,
                      "Failed to fetch user details",
                      Toast.LENGTH_SHORT)
                  .show();
            });

    queue.add(jsonObjectRequest);
  }

  private void fetchEvents(String url) {
    RequestQueue queue = Volley.newRequestQueue(this);

    JsonArrayRequest jsonArrayRequest =
        new JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONArray>() {
              @Override
              public void onResponse(JSONArray response) {
                try {
                  eventList.clear();

                  for (int i = 0; i < response.length(); i++) {
                    JSONObject eventObj = response.getJSONObject(i);

                    String eventName = eventObj.optString("eventName", "No name");
                    String date = eventObj.optString("date", "No date");
                    String location = eventObj.optString("eventLocation", "No location");
                    String eventDescription = eventObj.optString("description", "No description");

                    Event event = new Event(eventName, date, location, eventDescription);
                    eventList.add(event);
                  }

                  eventAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                  e.printStackTrace();
                  Log.e("FetchEvents", "JSON parsing error: " + e.getMessage());
                }
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("FetchEvents", "Network error: " + error.toString());
              }
            });

    queue.add(jsonArrayRequest);
  }
}