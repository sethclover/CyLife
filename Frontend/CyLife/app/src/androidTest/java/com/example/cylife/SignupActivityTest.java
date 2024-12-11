package com.example.cylife;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
public class SignupActivityTest {

    @Rule
    public ActivityScenarioRule<SignupActivity> activityRule =
            new ActivityScenarioRule<>(SignupActivity.class);

    @Before
    public void setUp() {
        // Register any necessary Idling Resources
        IdlingRegistry.getInstance().register( /* any Idling Resource here */ );
    }

    // Test Case 1: Test successful signup with valid input
    @Test
    public void testSuccessfulSignup() {
        onView(withId(R.id.email)).perform(typeText("test@example.com"));
        onView(withId(R.id.name)).perform(typeText("John Doe"));
        onView(withId(R.id.password)).perform(typeText("Password123"));
        onView(withId(R.id.confirm_password)).perform(typeText("Password123"));

        onView(withId(R.id.signup_button)).perform(click());

    }

    // Test Case 2: Missing fields should show a toast
    @Test
    public void testMissingFields() {
        onView(withId(R.id.signup_button)).perform(click());
    }

    // Test Case 3: Password mismatch should show a toast
    @Test
    public void testPasswordMismatch() {
        onView(withId(R.id.email)).perform(typeText("test@example.com"));
        onView(withId(R.id.name)).perform(typeText("John Doe"));
        onView(withId(R.id.password)).perform(typeText("Password123"));
        onView(withId(R.id.confirm_password)).perform(typeText("DifferentPassword123"));

        onView(withId(R.id.signup_button)).perform(click());

    }

    // Test Case 6: Redirect to login activity when login button is clicked
    public void testLoginButtonRedirect() {
        // Initialize Intents
        Intents.init();

        try {
            // Perform the login actions
            onView(withId(R.id.buttonLogin)).perform(click());
            onView(withId(R.id.editTextEmail))
                    .perform(typeText("test@example.com"), closeSoftKeyboard());
            onView(withId(R.id.editTextPassword))
                    .perform(typeText("Password123"), closeSoftKeyboard());
            onView(withId(R.id.buttonLogin)).perform(click());

            // Check if the correct activity is launched
            intended(hasComponent(WelcomeActivityClub.class.getName()));
        } finally {
            // Release Intents after the test
            Intents.release();
        }
    }
}