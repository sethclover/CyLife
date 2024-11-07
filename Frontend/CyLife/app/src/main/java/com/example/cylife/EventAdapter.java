package com.example.cylife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventNameTextView.setText(event.getEventName());
        holder.dateTextView.setText(event.getDate());
        holder.locationTextView.setText(event.getLocation());
        holder.descriptionTextView.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView;
        TextView dateTextView;
        TextView descriptionTextView;
        TextView locationTextView; // Add location TextView

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventTitleTextView);
            dateTextView = itemView.findViewById(R.id.eventDateTextView);
            locationTextView = itemView.findViewById(R.id.eventLocationTextView);
            descriptionTextView = itemView.findViewById(R.id.eventDescriptionTextView);
        }
    }
}
