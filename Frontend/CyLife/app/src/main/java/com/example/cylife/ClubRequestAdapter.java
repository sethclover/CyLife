package com.example.cylife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ClubRequestAdapter extends RecyclerView.Adapter<ClubRequestAdapter.ViewHolder> {

    private ArrayList<ClubRequest> clubRequests;

    public ClubRequestAdapter(ArrayList<ClubRequest> clubRequests) {
        this.clubRequests = clubRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_club_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClubRequest clubRequest = clubRequests.get(position);
        holder.clubName.setText(clubRequest.getClubName());
        holder.description.setText(clubRequest.getDescription());
        holder.clubEmail.setText("Email: " + clubRequest.getClubEmail());
        holder.status.setText("Status: " + clubRequest.getStatus());
    }


    @Override
    public int getItemCount() {
        return clubRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clubName, description, clubEmail, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.tvClubName);
            description = itemView.findViewById(R.id.tvDescription);
            clubEmail = itemView.findViewById(R.id.tvClubEmail);
            status = itemView.findViewById(R.id.tvStatus);
        }
    }
}
