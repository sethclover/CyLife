package com.example.cylife;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class ClubRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClubRequestAdapter adapter;
    private ArrayList<ClubRequest> clubRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_request);

        recyclerView = findViewById(R.id.recyclerViewClubRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and set it to the RecyclerView
        adapter = new ClubRequestAdapter(clubRequests);
        recyclerView.setAdapter(adapter);

        // Fetch club requests from the server
        fetchClubRequests();

    }

    private void fetchClubRequests() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/club-requests";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int requestId = jsonObject.getInt("requestId");
                                int studentId = jsonObject.getInt("studentId");
                                String clubName = jsonObject.getString("clubName");
                                String description = jsonObject.getString("description");
                                String clubEmail = jsonObject.getString("clubEmail");
                                String status = jsonObject.getString("status");

                                // Add to the list
                                clubRequests.add(new ClubRequest(requestId, studentId, clubName, description, clubEmail, status));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("ClubRequestsActivity", "Error parsing club requests", e);
                        }
                    }
                },
                error -> Log.e("ClubRequestsActivity", "Error fetching club requests", error));

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

}