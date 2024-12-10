package com.example.cylife;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.cylife", appContext.getPackageName());
    }

    @Test
    public void testWelcomeMessageDisplaysCorrectly() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), WelcomeActivityStudent.class);
        intent.putExtra("userID", 92);
        try (ActivityScenario<WelcomeActivityStudent> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.welcomeMessage))
                    .check(matches(withText("Welcome Gamma")));
        }
    }

    @Test
    public void testStudentLoginRedirectsToWelcomeStudent() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), LoginActivity.class);
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.editTextEmail))
                    .perform(typeText("tester@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.editTextPassword))
                    .perform(typeText("pass"), closeSoftKeyboard());
            onView(withId(R.id.buttonLogin)).perform(click());
        }
    }

    @Test
    public void testClubLoginRedirectsToWelcomeClub() {
        Intents.init();
        try {
            Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), LoginActivity.class);
            try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
                onView(withId(R.id.editTextEmail))
                        .perform(typeText("dhavi.m@example.com"), closeSoftKeyboard());
                onView(withId(R.id.editTextPassword))
                        .perform(typeText("securePassword123"), closeSoftKeyboard());
                onView(withId(R.id.buttonLogin)).perform(click());
                intended(hasComponent(WelcomeActivityClub.class.getName()));
            }
        } finally {
            Intents.release();
        }
    }


    @Test
    public void testStaffLoginRedirectsToWelcomeAdmin() {
        Intents.init();
        try {
            Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), LoginActivity.class);
            try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
                onView(withId(R.id.editTextEmail))
                        .perform(typeText("elonmussk@gmail.com"), closeSoftKeyboard());
                onView(withId(R.id.editTextPassword))
                        .perform(typeText("123"), closeSoftKeyboard());
                onView(withId(R.id.buttonLogin)).perform(click());
                intended(hasComponent(WelcomeActivity.class.getName()));
            }
        } finally {
            Intents.release();
        }
    }


    @Test
    public void testLogoutRedirectsToLoginActivity() {
        Intents.init();
        try {
            Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), AccountActivity.class);
            try (ActivityScenario<AccountActivity> scenario = ActivityScenario.launch(intent)) {
                onView(withId(R.id.btnLogout)).perform(click());
                intended(hasComponent(LoginActivity.class.getName()));
            }
        } finally {
            Intents.release();
        }
    }


    public static Matcher<View> withTextIgnoreCase(final String text) {
        return new TypeSafeMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof TextView)) {
                    return false;
                }
                String actualText = ((TextView) view).getText().toString();
                return actualText.equalsIgnoreCase(text);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with text ignoring case: " + text);
            }
        };
    }

}

