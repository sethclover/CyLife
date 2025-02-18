package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JoinClubActivity extends AppCompatActivity {
    private static final Logger log = LoggerFactory.getLogger(JoinClubActivity.class);
    private EditText searchBar;
    private RecyclerView recyclerView;
    private ClubAdapter clubAdapter;
    private List<Club> clubList;
    private RequestQueue requestQueue;
    private Button backButton;

    private int studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_club);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            finish();
        });

        Bundle extras = getIntent().getExtras();
        studentID = extras.getInt("userId");  // this will come from Welcome

        searchBar = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.clubListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        clubList = new ArrayList<>();
//        clubAdapter = new ClubAdapter(clubList, club -> {
//            //NEED TO IMPLEMENT JOIN FUNCTION< CURRENTLY ON CLICKING JOIN BUTTON IT WILL SAY CLUB JOINED
//            String serverUrl = "http://coms-3090-065.class.las.iastate.edu:8080/joinClub/" + club + "/" + username; // club is club ID here
//            WebSocketManager.getInstance().connectWebSocket(serverUrl);
//        });


        clubAdapter = new ClubAdapter(clubList, club -> {
            if ("Join".equals(club.getButtonText())) {
                joinClub(club);  // Call joinClub method if the button shows "Join"
            } else if ("Leave".equals(club.getButtonText())) {
                leaveClub(club);  // Call leaveClub method if the button shows "Leave"
            }
        });


        recyclerView.setAdapter(clubAdapter);

        requestQueue = Volley.newRequestQueue(this);

        fetchAllClubs();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchClub(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }


    private void fetchAllClubs() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/clubs";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        clubList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject clubObject = response.getJSONObject(i);

                            String clubName = clubObject.optString("clubName", "Unknown Club");
                            String clubId = clubObject.optString("clubId");
                            Log.d("ClubData", "Club Name: " + clubName + ", Club ID: " + clubId);

                            // Create Club object
                            Club club = new Club(clubName, clubId);
                            clubList.add(club);

                            // Check membership status for each club
                            checkMembershipStatus(club);
                        }

                        clubAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("Fetch Clubs", "Error parsing JSON response: " + e.getMessage());
                        Toast.makeText(JoinClubActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Fetch Clubs", "Error fetching clubs: " + error.toString());
                    Toast.makeText(JoinClubActivity.this, "Failed to fetch clubs", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonArrayRequest);
    }


    private void searchClub(String query) {
        List<Club> filteredClubs = new ArrayList<>();
        for (Club club : clubList) {
            if (club.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredClubs.add(club);
            }
        }
        clubAdapter.updateClubList(filteredClubs);
    }

    private void checkMembershipStatus(Club club) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/checkMembershipStatus/" + studentID + "/" + club.getId();

        JsonObjectRequest checkRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        boolean isMember = response.getBoolean("isMember");
                        if (isMember) {
                            // User is a member, set button to "Leave"
                            club.setButtonText("Leave");
                        } else {
                            // User is not a member, set button to "Join"
                            club.setButtonText("Join");
                        }
                        clubAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error checking membership", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Check Membership", "Error: " + error.toString());
                    Toast.makeText(this, "Failed to check membership", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(checkRequest);
    }

    private void joinClub(Club club) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/joinClub/" + studentID + "/" + club.getId();

        JsonObjectRequest joinRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(this, "Joined " + club.getName(), Toast.LENGTH_SHORT).show();
                            club.setButtonText("Leave");
                            clubAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Failed to join club", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error joining club", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Join Club", "Error: " + error.toString());
                    Toast.makeText(this, "Failed to join club", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(joinRequest);
    }

    private void leaveClub(Club club) {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/leaveClub/" + studentID + "/" + club.getId();

        JsonObjectRequest leaveRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(this, "Left " + club.getName(), Toast.LENGTH_SHORT).show();
                            club.setButtonText("Join");
                            clubAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Failed to leave club", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error leaving club", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Leave Club", "Error: " + error.toString());
                    Toast.makeText(this, "Failed to leave club", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(leaveRequest);
    }

}
