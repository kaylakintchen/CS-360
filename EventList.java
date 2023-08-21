package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Intent;


public class EventList extends AppCompatActivity {

    private static final int ADD_EVENT_REQUEST_CODE = 1;
    private static final int EDIT_EVENT_REQUEST_CODE = 2;

    private RecyclerView recyclerView;
    private EventListAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHelper = new DatabaseHelper(this);

        final List<Event> eventList = databaseHelper.getAllEvents();
        adapter = new EventListAdapter(eventList, databaseHelper); // Assuming your adapter takes these parameters
        recyclerView.setAdapter(adapter);

        adapter.setOnEditClickListener(new EventListAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position) {
                Event eventToEdit = eventList.get(position);
                Intent intent = new Intent(EventList.this, EditEventActivity.class);
                intent.putExtra("eventId", eventToEdit.getId());
                startActivityForResult(intent, EDIT_EVENT_REQUEST_CODE);
            }
        });

        Button buttonNewEvent = findViewById(R.id.buttonNewEvent);
        buttonNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventList.this, AddEventActivity.class);
                startActivityForResult(intent, ADD_EVENT_REQUEST_CODE);
            }
        });

        Button buttonPermissions = findViewById(R.id.buttonPermissions);
        buttonPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventList.this, Permissions.class);
                startActivity(intent);
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle adding new event
            String eventTitle = data.getStringExtra("eventTitle");
            String eventDate = data.getStringExtra("eventDate");
            String eventLocation = data.getStringExtra("eventLocation");
            Event newEvent = new Event(eventTitle, eventDate, eventLocation);
            adapter.addEvent(newEvent);
            adapter.notifyDataSetChanged();
        } else if (requestCode == EDIT_EVENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Event editedEvent = (Event) data.getSerializableExtra("editedEvent");
            if (editedEvent != null) {
                // Find the position of the edited event in the list
                int editedPosition = adapter.findEventPositionById(editedEvent.getId());
                if (editedPosition != -1) {
                    adapter.updateEvent(editedPosition, editedEvent);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
