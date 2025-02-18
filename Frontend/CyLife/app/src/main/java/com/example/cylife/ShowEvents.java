package com.example.cylife;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowEvents extends AppCompatActivity {

    private LinearLayout eventListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        eventListLayout = findViewById(R.id.eventListLayout);

        // Fetch and display events
        fetchEvents("http://coms-3090-065.class.las.iastate.edu:8080/events");

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchEvents(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        eventListLayout.removeAllViews(); // Clear existing views

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject eventObj = response.getJSONObject(i);
                                String eventName = eventObj.optString("eventName", "No name");
                                String date = eventObj.optString("date", "No date");
                                String location = eventObj.optString("eventLocation", "No location");
                                String description = eventObj.optString("description", "No description");

                                // Create TextView for each event
                                TextView eventView = new TextView(ShowEvents.this);
                                eventView.setText(String.format("Event: %s\nDate: %s\nLocation: %s\nDescription: %s",
                                        eventName, date, location, description));
                                eventView.setTextSize(16);
                                eventView.setPadding(16, 16, 16, 16);

                                // Add TextView to LinearLayout
                                eventListLayout.addView(eventView);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("FetchEvents", "JSON parsing error: " + e.getMessage());
                            }
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
