package com.chaijiaxun.pm25tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.chaijiaxun.pm25tracker.database.SensorReading;

import java.util.ArrayList;
import java.util.Date;

public class DevFragment extends Fragment {

    EditText sensorReading;
    ListView readingList;
    double lat, lon;

    public DevFragment() {

    }

    public static DevFragment newInstance() {
        DevFragment fragment = new DevFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sensorReading = (EditText) container.findViewById(R.id.sensor_reading);
        readingList = (ListView) container.findViewById(R.id.readings_view);

        return inflater.inflate(R.layout.content_main, container, false);

    }

    public void clickLocation(View button) {
        SensorReading.deleteAll();
        loadReadings();
    }

    public void saveReading(View button) {
        Log.d("MainActivity", "Saving reading");
        String text = sensorReading.getText().toString();
        int readingInt;
        try {
            readingInt = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            readingInt = 0;
        }
        Date date = new Date();
        Log.d("MainActivity", text + " " + lat + " " + lon + " " + date.toString());

        SensorReading reading = new SensorReading(date, readingInt, 0, (float)lat, (float)lon, 0);

        reading.save();
        Log.d("MainActivity", reading.toString());

        loadReadings();
    }

    public void loadReadings() {
        ArrayList<SensorReading> list =(ArrayList)SensorReading.getList();
        String[] values;
        Log.d("Load Readings", list.size() + " ");
        if ( list.size() > 0 ) {
            values = new String[list.size()];
            for ( int i = 0; i < list.size(); i++ ) {
                values[i] = list.get(i).toString();
            }
        } else {
            values = new String[] { "Nothing in database" };
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        readingList.setAdapter(adapter);

    }
}
