package com.example.cylife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> {

    private List<Club> clubList;
    private final OnJoinClickListener onJoinClickListener;

    public ClubAdapter(List<Club> clubList, OnJoinClickListener onJoinClickListener) {
        this.clubList = clubList;
        this.onJoinClickListener = onJoinClickListener;
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_item, parent, false);
        return new ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        Club club = clubList.get(position);
        holder.clubName.setText(club.getName());
        holder.joinButton.setText(club.getButtonText());

        holder.joinButton.setOnClickListener(view -> {
            onJoinClickListener.onJoinClick(club); // Call the interface method
        });
    }


    @Override
    public int getItemCount() {
        return clubList.size();
    }

    public void updateClubList(List<Club> updatedList) {
        this.clubList = updatedList;
        notifyDataSetChanged();
    }

    public static class ClubViewHolder extends RecyclerView.ViewHolder {
        TextView clubName;
        Button joinButton;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.clubName);
            joinButton = itemView.findViewById(R.id.joinButton);
        }
    }

    public interface OnJoinClickListener {
        void onJoinClick(Club club);
    }
}
