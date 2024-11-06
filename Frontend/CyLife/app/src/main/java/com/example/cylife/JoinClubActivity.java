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
import java.util.ArrayList;
import java.util.List;

public class JoinClubActivity extends AppCompatActivity {
    private EditText searchBar;
    private RecyclerView recyclerView;
    private ClubAdapter clubAdapter;
    private List<Club> clubList;
    private RequestQueue requestQueue;
    private Button backButton;

    private String username;
    private int studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_club);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(JoinClubActivity.this, EditClub.class);
            startActivity(intent);
        });

        Bundle extras = getIntent().getExtras();
        studentID = extras.getInt("userId");  // this will come from Welcome
        username = extras.getString("username");  // this will come from Welcome

        searchBar = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.clubListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        clubList = new ArrayList<>();
        clubAdapter = new ClubAdapter(clubList, club -> {
            //NEED TO IMPLEMENT JOIN FUNCTION< CURRENTLY ON CLICKING JOIN BUTTON IT WILL SAY CLUB JOINED
            String serverUrl = "http://coms-3090-065.class.las.iastate.edu:8080/joinClub/" + club + "/" + username; // club is club ID here
            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            Toast.makeText(this, "Joined " + club.getName(), Toast.LENGTH_SHORT).show();
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
//        String url = "http://coms-3090-065.class.las.iastate.edu:8080/clubs";
        String url = "http://10.0.2.2:3000/club";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray clubsArray = response.getJSONArray("clubs");
                                clubList.clear();

                                for (int i = 0; i < clubsArray.length(); i++) {
                                    JSONObject clubObject = clubsArray.getJSONObject(i);
                                    String clubName = clubObject.getString("clubName"); // Ensure this matches JSON key
                                    clubList.add(new Club(clubName));
                                }

                                clubAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(JoinClubActivity.this, "No clubs found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(JoinClubActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(JoinClubActivity.this, "Failed to fetch clubs", Toast.LENGTH_SHORT).show();
                        Log.e("Fetch Clubs", error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
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
}
