package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.appcompat.app.AppCompatActivity;

public class EventsActivity extends AppCompatActivity {

    private EditText etName, etLocation, etTime, etDescription, etEditID, etDeleteID;
    private Button createEventButton, editEventButton, getEventButton, deleteEventButton, logoutButton;
    private TextView orgListTextView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        // Implement event-related logic here

        logoutButton = findViewById(R.id.logout_button);

        etName = findViewById(R.id.event_name_input);
        etLocation = findViewById(R.id.location_input);
        etTime = findViewById(R.id.time_input);
        etDescription = findViewById(R.id.event_description_input);
        createEventButton = findViewById(R.id.CE);
        etEditID = findViewById(R.id.event_id_input);
        editEventButton = findViewById(R.id.EE);
        getEventButton = findViewById(R.id.GE);
        etDeleteID = findViewById(R.id.event_id_input2);
        deleteEventButton = findViewById(R.id.DE);

        requestQueue = Volley.newRequestQueue(this);
        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(EventsActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

