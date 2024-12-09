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
        // Check if all important UI elements are displayed
        onView(withId(R.id.profileImage)).check(matches(isDisplayed()));
        onView(withId(R.id.userName)).check(matches(isDisplayed()));
        onView(withId(R.id.userEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.clubName)).check(matches(isDisplayed()));
        onView(withId(R.id.clubDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.editProfilePictureText)).check(matches(isDisplayed()));
        onView(withId(R.id.btnChangePassword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnDeleteUser)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogout)).check(matches(isDisplayed()));
    }

    @Test
    public void testEditProfilePictureNavigation() {
        // Click on "Edit Profile Picture" and verify the intent to pick an image
        onView(withId(R.id.editProfilePictureText)).perform(click());
    }

    @Test
    public void testChangePasswordDialog() {
        // Click the Change Password button and verify the dialog is displayed
        onView(withId(R.id.btnChangePassword)).perform(click());
        onView(withText("Change Password")).check(matches(isDisplayed()));
    }

    @Test
    public void testLogoutButton() {
        // Click the Logout button and check for the expected result
        onView(withId(R.id.btnLogout)).perform(click());
        // Verify redirection to LoginActivity (could be checked with an Intent matcher)
    }

    @Test
    public void testDeleteUser() {
        // Simulate Delete User and check for expected behavior
        onView(withId(R.id.btnDeleteUser)).perform(click());
        // Add further verification for dialog prompts or subsequent behavior
    }

    @Test
    public void testChangePasswordForUser() {
        // Pass userId 92 to the activity
//        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AccountActivity.class);
//        intent.putExtra("userId", 92);
//        activityScenarioRule.getScenario().onActivity(activity -> activity.setIntent(intent));
//
//        // Perform the change password action
//        onView(withId(R.id.btnChangePassword)).perform(click());
//        onView(withId(R.id.currentPasswordInput)).perform(typeText("pass"));
//        onView(withId(R.id.newPasswordInput)).perform(typeText("123"));
//        onView(withId(R.id.confirmPasswordInput)).perform(typeText("123"));
//        onView(withId(R.id.btnChangePassword)).perform(click());
//        onView(withId(R.id.))
//
//        // Verify success message
//        onView(withText("Password changed successfully")).check(matches(isDisplayed()));
//    }

    // Pass userID via Intent
    Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AccountActivity.class);
        intent.putExtra("userID", 149);
        try (
    ActivityScenario<AccountActivity> scenario = ActivityScenario.launch(intent)) {
        onView(withId(R.id.btnDeleteUser)).perform(click());

    }

}}

