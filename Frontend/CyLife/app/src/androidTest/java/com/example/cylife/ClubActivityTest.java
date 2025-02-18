package com.example.cylife;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ClubActivityTest {

    private ActivityScenario<clubActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(clubActivity.class);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void testCreateClub() {
        onView(withId(R.id.etClubNameCreate)).perform(typeText("Test Club"));
        onView(withId(R.id.etEmailCreate)).perform(typeText("testclub@example.com"));
        onView(withId(R.id.etClubPass)).perform(typeText("password123"));
        onView(withId(R.id.etClubPass)).perform(closeSoftKeyboard());
        onView(withId(R.id.createClubButton)).perform(click());
    }

    @Test
    public void testEditClub() {
        onView(withId(R.id.etClubIdEdit)).perform(replaceText("156"));
        onView(withId(R.id.etClubNameEdit)).perform(replaceText("Updated Club"));
        onView(withId(R.id.etEmailEdit)).perform(replaceText("updatedclub@example.com"));

        onView(withId(R.id.editClubButton)).perform(click());

        onView(withId(R.id.etClubIdEdit)).check(matches(withText("156")));
    }

    @Test
    public void testDeleteClub() {
        onView(withId(R.id.etClubIdD)).perform(replaceText("154"));
        onView(withId(R.id.DC)).perform(click());
    }

    @Test
    public void testBackButton() {
        onView(withId(R.id.backButton)).perform(click());

        scenario.onActivity(activity -> {
            assertNotNull(activity);
            assert (activity.isFinishing());
        });
    }

    @Test
    public void testLayoutElements() {
        // Check if all the required elements are present
        onView(withId(R.id.backButton)).check(matches(isVisible()));
        onView(withId(R.id.screenTitle)).check(matches(withText("Club Management")));
        onView(withId(R.id.createClubButton)).check(matches(isVisible()));
        onView(withId(R.id.editClubButton)).check(matches(isVisible()));
        onView(withId(R.id.DC)).check(matches(isVisible()));
    }

    private static Matcher<View> isVisible() {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("is visible on the screen to the user");
            }

            @Override
            public boolean matchesSafely(View view) {
                return view.getVisibility() == View.VISIBLE;
            }
        };
    }
}
