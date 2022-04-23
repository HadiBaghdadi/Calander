package com.example.calander;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EventList extends Fragment {
    //initializing variables and adapters and databses
    View view;
    private MySQLiteDBHandler dbHandler;
    private String userEmail;
    private String userSelectedDate;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayAdapter<String> adapter;
    public EventList(String email, String selectedDate) {
        this.userEmail = email;
        this.userSelectedDate = selectedDate;
    }
    //blank  constructor

    public EventList() {

    }

    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and calls the database created and creates the listview and adapter

        view = inflater.inflate(R.layout.fragment_event_list, container, false);
        dbHandler = new MySQLiteDBHandler(getContext(), "CalendarDatabase", null, 1);
        sqLiteDatabase = dbHandler.getReadableDatabase();
        ArrayList<String> arEvents = new ArrayList<String>();
        arEvents = ReadDatabase(userEmail, userSelectedDate);

        ListView listView = (ListView) view.findViewById(R.id.eventList);
        adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                arEvents
        );
        listView.setAdapter(adapter);

        return view;
    }


    //notifies the listview when data is changed
    @Override
    public void onResume() {
        super.onResume();
        ListView listView = (ListView) view.findViewById(R.id.eventList);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    //reads the database and returns the events into the listbox
    @SuppressLint("Range")
    public ArrayList<String> ReadDatabase(String email, String selectedDate) {
        ArrayList<String> events = new ArrayList<String>();
        try {

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT Event FROM EventCalendar WHERE Date = ? AND Email = ?", new String[]{selectedDate, email});

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                events.add(cursor.getString(cursor.getColumnIndex("Event")));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;

    }


}