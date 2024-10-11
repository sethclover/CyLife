package com.example.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowEvents extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> eventList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        listView = findViewById(R.id.list);
        eventList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.listview_item, R.id.listViewID, eventList);
        listView.setAdapter(arrayAdapter);


        String url = "http://10.0.2.2:3000/events";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                JSONArray eventsArray = jsonResponse.getJSONArray("events");
                                // Process the events array
                            } else {
                                String message = jsonResponse.getString("message");
                                // Handle error message
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                }
        );
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                };

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}


//package com.example.androidexample;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.AdapterView;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.example.login.VolleySingleton;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ShowEvents extends AppCompatActivity {
//    private ListView listView;
//    private ArrayAdapter<String> arrayAdapter;
//    private ArrayList<String> userList;
//    private ImageView back;
//    private TextView userType;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_events);
//        String userCategory = "Attendees";
//        userType.setText(userCategory);
//        listView = findViewById(R.id.list);
//        userList = new ArrayList<>();
//        arrayAdapter = new ArrayAdapter<>(this, R.layout.listview_item, userList);
//        listView.setAdapter(arrayAdapter);
//        back = findViewById(R.id.backArrow);
//
//        //final String urlGroupListings = "http://coms-309-017.class.las.iastate.edu:8080/attendeedetails";
//        final String urlGroupListings = "http://coms-309-017.class.las.iastate.edu:8080/attendeedetails";
//        Log.i("URL", urlGroupListings);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGroupListings, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray jsonArray) {
//                Log.i("Message Returned", jsonArray.toString());
//                try {
//                    userList.clear();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        String email = jsonArray.getString(i);  // Get the email directly from the JSONArray
//                        Log.i("Attendee Email", email);
//                        userList.add(email);
//                    }
//                    arrayAdapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                volleyError.printStackTrace();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
//
//    }
//}