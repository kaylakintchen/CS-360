package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {
    private EditText editTextEventTitle, editTextEventDate, editTextEventLocation;
    private DatabaseHelper databaseHelper;
    private Event eventToEdit; // Holds the event being edited
    private int position; // Position of the event being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        editTextEventTitle = findViewById(R.id.editTextEventTitle);
        editTextEventDate = findViewById(R.id.editTextEventDate);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        databaseHelper = new DatabaseHelper(this);

        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = editTextEventTitle.getText().toString();
                String eventDate = editTextEventDate.getText().toString();
                String eventLocation = editTextEventLocation.getText().toString();

                long eventId = databaseHelper.insertEvent(eventTitle, eventDate, eventLocation);

                if (eventId != -1) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("eventTitle", eventTitle);
                    resultIntent.putExtra("eventDate", eventDate);
                    resultIntent.putExtra("eventLocation", eventLocation);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {

                }

                finish(); // Close the AddEventActivity
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the AddEventActivity without saving
            }
        });
    }
}
