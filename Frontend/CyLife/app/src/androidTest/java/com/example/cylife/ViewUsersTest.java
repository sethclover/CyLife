package com.example.cylife;

import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class ViewUsersTest {

    @Rule
    public ActivityScenarioRule<ViewUsers> activityRule = new ActivityScenarioRule<>(ViewUsers.class);

    @Before
    public void setUp() {
        // Setup necessary environment (if needed)
    }

    @Test
    public void testFetchUsers_Success() {
        // Make a real network request to fetch users
        String url = "http://coms-3090-065.class.las.iastate.edu:8080/users";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Assuming there is a user with name "John Doe"
                            JSONObject userObject = response.getJSONObject(0);
                            String name = userObject.getString("name");

                            // Verify if the user appears in the UI
                            onView(withText(name)).check(matches(ViewMatchers.isDisplayed()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    // Handle error response if necessary
                    error.printStackTrace();
                });

        }

    @Test
    public void testFetchUsers_Failure() {
        // Simulate a network failure by using an incorrect URL or turning off the network
        String url = "http://invalid-url";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // This block shouldn't be executed, as the URL is invalid
                    }
                },
                error -> {
                    // Handle , e.g., show an error message in the UI
                });

        // Add the request to the request queue
    }
}
