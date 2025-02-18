package com.example.cylife;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ClubRequestsActivityTest {

    @Before
    public void setup() {
        // Ensure that any setup required before tests is done here
    }

    @Test
    public void testClubRequestsAreDisplayed() {
        // Launch the activity
        try (ActivityScenario<ClubRequestsActivity> scenario = ActivityScenario.launch(ClubRequestsActivity.class)) {
            // Wait to let the network call finish (simplified for demonstration purposes)
            Thread.sleep(1000);

            // Check if RecyclerView is populated
            onView(withId(R.id.recyclerViewClubRequests))
                    .check((view, noViewFoundException) -> {
                        if (noViewFoundException != null) throw noViewFoundException;
                        RecyclerView recyclerView = (RecyclerView) view;
                        assert recyclerView.getAdapter() != null;
                        assert recyclerView.getAdapter().getItemCount() > 0;
                    });

            onView(withText("Proposed Club")).check(matches(withText("Proposed Club")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
