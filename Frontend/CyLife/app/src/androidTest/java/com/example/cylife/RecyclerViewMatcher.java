package com.example.cylife;

import androidx.recyclerview.widget.RecyclerView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.view.View;

public class RecyclerViewMatcher {

    public static Matcher<View> hasAtLeastOneItem() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) view;
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    return adapter != null && adapter.getItemCount() > 0;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView should have at least one item.");
            }
        };
    }
}

