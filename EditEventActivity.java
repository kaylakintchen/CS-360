package com.example.my;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;

public class EditEventActivity extends AppCompatActivity {

    private EditText editTextEventTitle, editTextEventDate, editTextEventLocation;
    private DatabaseHelper databaseHelper;
    private Event eventToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        editTextEventTitle = findViewById(R.id.editTextEventTitle);
        editTextEventDate = findViewById(R.id.editTextEventDate);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        databaseHelper = new DatabaseHelper(this);

        // Retrieve event ID passed from EventList activity
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
           int eventId = extras.getInt("eventId");
            Log.d("EditEventActivity", "Received event ID: " + eventId);
            eventToEdit = databaseHelper.getEventById(eventId);
            if (eventToEdit != null) {
                editTextEventTitle.setText(eventToEdit.getEventTitle());
                editTextEventDate.setText(eventToEdit.getEventDate());
                editTextEventLocation.setText(eventToEdit.getEventLocation());
            } else {
                Log.d("EditEventActivity", "Event not found");

            }

            Button buttonSave = findViewById(R.id.buttonSave);
            Button buttonCancel = findViewById(R.id.buttonCancel);

            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the edited values from the fields

                    Log.d("EditEventActivity", "Save button clicked");
                    String newEventTitle = editTextEventTitle.getText().toString();
                    String newEventDate = editTextEventDate.getText().toString();
                    String newEventLocation = editTextEventLocation.getText().toString();

                    // Update the event in the database
                    eventToEdit.setEventTitle(newEventTitle);
                    eventToEdit.setEventDate(newEventDate);
                    eventToEdit.setEventLocation(newEventLocation);

                    boolean updateSuccess = databaseHelper.updateEvent(eventToEdit);

                    Log.d("EditEventActivity", "Update event success: " + updateSuccess);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("editedEvent", eventToEdit); // Send back the edited event
                    setResult(RESULT_OK, resultIntent);

                    // Finish the activity and return to EventList
                    finish();
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Close the EditEventActivity without saving
                }
            });
        }
    }
}



