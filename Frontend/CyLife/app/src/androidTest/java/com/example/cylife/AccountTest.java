package com.example.cylife;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class AccountTest {

    @Rule
    public ActivityScenarioRule<AccountActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AccountActivity.class);

    @Test
    public void testUIElementsAreDisplayed() {
        onView(withId(R.id.profileImage)).check(matches(isDisplayed()));
        onView(withId(R.id.userName)).check(matches(isDisplayed()));
        onView(withId(R.id.userEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.clubName)).check(matches(isDisplayed()));
        onView(withId(R.id.editProfilePictureText)).check(matches(isDisplayed()));
        onView(withId(R.id.btnChangePassword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnDeleteUser)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogout)).check(matches(isDisplayed()));
    }

    @Test
    public void testEditProfilePictureNavigation() {
        onView(withId(R.id.editProfilePictureText)).perform(click());
    }

    @Test
    public void testChangePasswordDialog() {
        onView(withId(R.id.btnChangePassword)).perform(click());
        onView(withText("Change Password")).check(matches(isDisplayed()));
    }

    @Test
    public void testLogoutButton() {
        onView(withId(R.id.btnLogout)).perform(click());
    }

    @Test
    public void testDeleteUser() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AccountActivity.class);
        intent.putExtra("userID", 149);
        try (
                ActivityScenario<AccountActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.btnDeleteUser)).perform(click());

        }

}}

