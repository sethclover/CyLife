package com.example.cylife;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import com.example.cylife.EditClub;

public class EditClubTest {

    @Test
    public void testEditClub() {
        // Create an Intent to pass the clubId to the EditClub activity
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EditClub.class);
        intent.putExtra("userId", 153); // Replace with the actual club ID for the test

        try (ActivityScenario<EditClub> scenario = ActivityScenario.launch(intent)) {
            // Verify that the welcome message displays the correct clubId
            onView(withId(R.id.club_name_text)).check(matches(withText("Welcome 153")));

            // Simulate user input for club name and description
            onView(withId(R.id.etClubName)).perform(replaceText("New Club Name"));
            onView(withId(R.id.etDesc)).perform(replaceText("Updated Club Description"));

            // Click the Save button to trigger the edit operation
            onView(withId(R.id.SaveButton)).perform(click());
        }
    }
}
