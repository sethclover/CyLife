package com.example.cylife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ClubAdapterTest {

    private Context context;
    private ClubAdapter clubAdapter;
    private List<Club> initialClubs;
    private boolean isJoinClicked;

    @Before
    public void setUp() {
        // Initialize context and mock data
        context = ApplicationProvider.getApplicationContext();
        initialClubs = new ArrayList<>();
        initialClubs.add(new Club("Chess Club", "Join"));
        initialClubs.add(new Club("Math Club", "Join"));

        // Flag to check if onJoinClick is triggered
        isJoinClicked = false;

        // Initialize the adapter with mock data and listener
        clubAdapter = new ClubAdapter(initialClubs, club -> {
            isJoinClicked = true;
            assertEquals("Chess Club", club.getName()); // Verify clicked club
        });
    }

    @Test
    public void testGetItemCount() {
        // Verify initial item count
        assertEquals(2, clubAdapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder() {
        // Create a RecyclerView and set a LayoutManager
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Create a View using the LayoutInflater
        View view = LayoutInflater.from(context).inflate(R.layout.club_item, recyclerView, false);

        // Create a ViewHolder and bind data
        ClubAdapter.ClubViewHolder viewHolder = new ClubAdapter.ClubViewHolder(view);
        clubAdapter.onBindViewHolder(viewHolder, 0);

        // Assert that the data is correctly set in the ViewHolder
        assertEquals("Chess Club", viewHolder.clubName.getText().toString());
        assertEquals("Join", viewHolder.joinButton.getText().toString());
    }


    @Test
    public void testOnJoinClick() {
        // Simulate a button click
        clubAdapter.onBindViewHolder(new ClubAdapter.ClubViewHolder(
                LayoutInflater.from(context).inflate(R.layout.club_item, null, false)
        ), 0);

        clubAdapter.onJoinClickListener.onJoinClick(initialClubs.get(0));
        assertEquals(true, isJoinClicked); // Ensure the callback was triggered
    }

    @Test
    public void testUpdateClubList() {
        // Update club list
        List<Club> updatedClubs = new ArrayList<>();
        updatedClubs.add(new Club("Science Club", "Join"));
        clubAdapter.updateClubList(updatedClubs);

        // Verify the new list
        assertEquals(1, clubAdapter.getItemCount());
        assertEquals("Science Club", updatedClubs.get(0).getName());
    }
}
