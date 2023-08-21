package com.example.my;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.util.Log;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<Event> eventList;
    private DatabaseHelper databaseHelper;
    private OnEditClickListener editClickListener;

    public EventListAdapter(List<Event> eventList, DatabaseHelper databaseHelper) {
        this.eventList = eventList;
        this.databaseHelper = databaseHelper;
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.textEventTitle.setText(event.getEventTitle());
        holder.textEventDate.setText(event.getEventDate());
        holder.textEventLocation.setText(event.getEventLocation());
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(false);

        holder.checkBoxCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    markEventAsCompleted(holder.getAdapterPosition(), databaseHelper);
                }
            }
        });

        holder.editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClick(holder.getAdapterPosition());
                }
            }
        });
    }

    public int findEventPositionById(long eventId) {
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getId() == eventId) {
                return i;
            }
        }
        return -1; // Return -1 if the event is not found
    }

    public void markEventAsCompleted(int position, DatabaseHelper databaseHelper) {
        Event completedEvent = eventList.get(position);
        databaseHelper.deleteEvent(completedEvent);
        eventList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEventList(List<Event> newEventList) {
        eventList.clear();
        eventList.addAll(newEventList);
        notifyDataSetChanged();
    }

    public void addEvent(Event event) {
        if (event != null) {
            eventList.add(event);
            notifyDataSetChanged();
        }
    }

    public void updateEvent(int position, Event updatedEvent) {
        if (updatedEvent != null) {
            eventList.set(position, updatedEvent);
            notifyItemChanged(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textEventTitle, textEventDate, textEventLocation;
        CheckBox checkBoxCompleted;
        Button editEventButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textEventTitle = itemView.findViewById(R.id.EventTitle);
            textEventDate = itemView.findViewById(R.id.EventDate);
            textEventLocation = itemView.findViewById(R.id.EventLocation);
            checkBoxCompleted = itemView.findViewById(R.id.completedEventCheck);
            editEventButton = itemView.findViewById(R.id.editEventButton);
        }
    }
}
