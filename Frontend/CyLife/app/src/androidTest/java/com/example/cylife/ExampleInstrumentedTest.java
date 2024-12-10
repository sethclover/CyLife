//package com.example.cylife;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.espresso.intent.Intents;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.platform.app.InstrumentationRegistry;
//
//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.TypeSafeMatcher;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.example.cylife", appContext.getPackageName());
//    }
//
////    @Test
////    public void testWelcomeMessageDisplaysCorrectly() {
////        // Launch WelcomeActivityStudent with a mock userId
////        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), WelcomeActivityStudent.class);
////        intent.putExtra("userID", 1); // Mock userId
////        try (ActivityScenario<WelcomeActivityStudent> scenario = ActivityScenario.launch(intent)) {
////            onView(withId(R.id.welcomeMessage)).check(matches(withText("")));
////        }
////    }
//
//    @Test
////    public void testLoginRedirectsToCorrectActivity() {
////        // Launch LoginActivity
////        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), LoginActivity.class);
////        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(intent)) {
////
////            // Mock valid login credentials input
////            onView(withId(R.id.editTextEmail))
////                    .perform(typeText("tester@gmail.com"), closeSoftKeyboard());
////            onView(withId(R.id.editTextPassword))
////                    .perform(typeText("123"), closeSoftKeyboard());
////
////            // Perform login action
////            onView(withId(R.id.buttonLogin)).perform(click());
////
////            // Mock backend response to simulate successful login
////            onView(withId(R.id.welcomeMessage))
////                    .check(matches(withTextIgnoreCase("Welcome Gamma")));
////        }
////    }
//
//    public static Matcher<View> withTextIgnoreCase(final String text) {
//        return new TypeSafeMatcher<View>() {
//            @Override
//            protected boolean matchesSafely(View item) {
//                if (!(item instanceof TextView)) {
//                    return false;
//                }
//                String actualText = ((TextView) item).getText().toString();
//                return actualText.equalsIgnoreCase(text);
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with text ignoring case: " + text);
//            }
//        };
//    }
//
//    @Test
//    public void testLogoutRedirectsToLoginActivity() {
//        // Launch AccountActivity
//        Intents.init();
//        try {
//            Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), AccountActivity.class);
//            try (ActivityScenario<AccountActivity> scenario = ActivityScenario.launch(intent)) {
//                onView(withId(R.id.btnLogout)).perform(click());
//                intended(hasComponent(LoginActivity.class.getName()));
//            }
//        } finally {
//            Intents.release();
//        }
//    }
//
//
//}
//
