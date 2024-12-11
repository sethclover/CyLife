package com.example.cylife;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

public class ClubCreateEvent extends AppCompatActivity {

    private EditText etEventName, etEventLocation, etEventDescription;
    private Button createEventButton, exitButton;

    private Spinner day, month, year;

    private TextView statusText;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_create_event);

        // Initialize fields and buttons
        etEventName = findViewById(R.id.etEventName);
        etEventLocation = findViewById(R.id.etLocation);
        day = findViewById(R.id.date);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        etEventDescription = findViewById(R.id.etDesc);

        createEventButton = findViewById(R.id.createEventButton);
        exitButton = findViewById(R.id.exitButton);

        statusText = findViewById(R.id.eventCreationStatus);


        String[] dayItems = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_list, dayItems);
        day.setAdapter(adapter);

        String[] monthItems = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_list, monthItems);
        month.setAdapter(adapter2);

        String[] yearItems = new String[]{"2024", "2025"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.spinner_list, yearItems);
        year.setAdapter(adapter3);

        // Create Volley request queue
//        requestQueue = Volley.newRequestQueue(this);

        // Create event
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Now onto function call", "Now onto function call");
                String url = "http://coms-3090-065.class.las.iastate.edu:8080/events";

                // Get values from EditTexts
                String eventName = etEventName.getText().toString();
                String eventTimeStr = year.getSelectedItem().toString() + "-" + month.getSelectedItem().toString() + "-" + day.getSelectedItem().toString();
                LocalDate eventTime = null;

                eventTime = LocalDate.parse(eventTimeStr);
                String eventLocation = etEventLocation.getText().toString();
                String eventDescription = etEventDescription.getText().toString();

                if (eventName.isEmpty() || eventLocation.isEmpty() || eventDescription.isEmpty()) {
                    Toast.makeText(ClubCreateEvent.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Prepare put request data
                JSONObject postData = new JSONObject();
                try {
                    postData.put("eventName", eventName);
                    postData.put("date", eventTime);
                    postData.put("eventLocation", eventLocation);
                    postData.put("description", eventDescription);
                    //Log.i("JSON Object", String.valueOf(postData));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Create a request to post the data
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("JSON Object after Response", String.valueOf(postData));
                                Toast.makeText(ClubCreateEvent.this, "It's working", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse != null) {
                                    String responseData = new String(error.networkResponse.data);
                                    Log.e("Volley Error", "Status Code: " + error.networkResponse.statusCode);
                                    Log.e("Volley Error", "Response Body: " + responseData);
                                } else {
                                    Log.e("Volley Error", "Unknown error occurred");
                                }
                            }

                        }){

                    @Override
                    public byte[] getBody() {
                        return postData.toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };


                // Add the request to the RequestQueue
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                createEvent();
            }
        });
//        createEventButton.setOnClickListener(view -> {
//            createEvent();
//        });

        // Logout functionality
        exitButton.setOnClickListener(view -> {
            finish();
        });
    }

    // Function to post data and create an event
    private void createEvent() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/event";

        // Get values from EditTexts
        String eventName = etEventName.getText().toString();
        String eventTimeStr = year.getSelectedItem().toString() + "-" + month.getSelectedItem().toString() + "-" + day.getSelectedItem().toString();
        LocalDate eventTime = null;

        eventTime = LocalDate.parse(eventTimeStr);

        String eventLocation = etEventLocation.getText().toString();
        String eventDescription = etEventDescription.getText().toString();

        if (eventName.isEmpty() || eventLocation.isEmpty() || eventDescription.isEmpty()) {
            Toast.makeText(ClubCreateEvent.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare put request data
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", eventName);
            postData.put("date", eventTime);
            postData.put("location", eventLocation);
            postData.put("description", eventDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a request to post the data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ClubCreateEvent.this, "Event Created", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ClubCreateEvent.this, "Error when posting", Toast.LENGTH_SHORT).show();
                        // Handle error (e.g., show error message)
                    }
                }){

            @Override
            public byte[] getBody() {
                return postData.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };


        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}
