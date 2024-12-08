package com.example.cylife;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
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

public class ViewUsers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch users from the server
        fetchUsers();
    }

    private void fetchUsers() {
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/users";

        // Initialize a request queue
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Parse the JSON response
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);

                                String name = userObject.getString("name");
                                String email = userObject.getString("email");
                                String type = userObject.getString("type");
                                // Assuming you have a method to fetch the profile picture URL
//                                String profilePicUrl = "profilePicUrl"; // You can modify this if it's part of the response.

                                // Add the user to the list
                                userList.add(new User(name, email, type));
                            }

                            // Set up the adapter with the fetched data
                            userAdapter = new UserAdapter(userList);
                            recyclerView.setAdapter(userAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("ViewUsersActivity", "Error parsing user data", e);
                        }
                    }
                },
                error -> {
                    // Handle error
                    Log.e("ViewUsersActivity", "Error fetching users", error);
                });

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}
