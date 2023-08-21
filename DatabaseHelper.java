package com.example.my;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_app_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_EVENTS = "events";

    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS +
                    "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_USERNAME + " TEXT NOT NULL UNIQUE," +
                    KEY_PASSWORD + " TEXT NOT NULL" +
                    ")";

    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS +
                    "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "event_title TEXT NOT NULL," +
                    "event_date TEXT NOT NULL," +
                    "event_location TEXT NOT NULL" +
                    ")";


    private static final String COLUMN_EVENT_TITLE = "event_title";
    private static final String COLUMN_EVENT_DATE = "event_date";
    private static final String COLUMN_EVENT_LOCATION = "event_location";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public long insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID};
        String selection = KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public long insertEvent(String eventTitle, String eventDate, String eventLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, eventTitle);
        values.put(COLUMN_EVENT_DATE, eventDate);
        values.put(COLUMN_EVENT_LOCATION, eventLocation);
        return db.insert(TABLE_EVENTS, null, values);
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                KEY_ID, // Add the ID column
                COLUMN_EVENT_TITLE,
                COLUMN_EVENT_DATE,
                COLUMN_EVENT_LOCATION
        };
        Cursor cursor = db.query(TABLE_EVENTS, columns, null, null, null, null, null);

        int columnIndexId = cursor.getColumnIndex(KEY_ID); // Get the index for the ID column
        int columnIndexEventTitle = cursor.getColumnIndex(COLUMN_EVENT_TITLE);
        int columnIndexEventDate = cursor.getColumnIndex(COLUMN_EVENT_DATE);
        int columnIndexEventLocation = cursor.getColumnIndex(COLUMN_EVENT_LOCATION);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(columnIndexId); // Retrieve the ID
            String eventTitle = cursor.getString(columnIndexEventTitle);
            String eventDate = cursor.getString(columnIndexEventDate);
            String eventLocation = cursor.getString(columnIndexEventLocation);

            // Create an Event object and add it to the eventList
            Event event = new Event(eventTitle, eventDate, eventLocation);
            event.setId(id); // Set the ID
            eventList.add(event);
        }

        cursor.close();
        return eventList;
    }




    public boolean updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_TITLE, event.getEventTitle());
        values.put(COLUMN_EVENT_DATE, event.getEventDate());
        values.put(COLUMN_EVENT_LOCATION, event.getEventLocation());

        int rowsAffected = db.update(TABLE_EVENTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});

        db.close();
        Log.d("DatabaseHelper", "Event update rows affected: " + rowsAffected);
        return rowsAffected > 0;


    }
    public Event getEventById(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, KEY_ID + "=?", new String[]{String.valueOf(eventId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID)); // Retrieve the ID
            String eventTitle = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_TITLE));
            String eventDate = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_DATE));
            String eventLocation = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_LOCATION));

            cursor.close();
            Event event = new Event(eventTitle, eventDate, eventLocation); // Create Event object
            event.setId(id); // Set the ID
            return event;
        } else {
            Log.d("DatabaseHelper", "No event found with ID: " + eventId);

        }

        return null; // Return null if no event found

    }


    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = KEY_ID + "=?";
        String[] whereArgs = {String.valueOf(event.getId())};

        db.delete(TABLE_EVENTS, whereClause, whereArgs);

        db.close();
    }
}
