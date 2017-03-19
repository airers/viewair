package com.chaijiaxun.pm25tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        View view = inflater.inflate(R.layout.content_main, container, false);

        final Button saveButton = (Button) view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveReading();
            }
        });

        final Button locateButton = (Button) view.findViewById(R.id.button_locate);
        locateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickLocation();
            }
        });

        sensorReading = (EditText) view.findViewById(R.id.sensor_reading);
        readingList = (ListView) view.findViewById(R.id.readings_view);
        loadReadings();

        return view;

    }

    public void clickLocation() {
        SensorReading.deleteAll(SensorReading.class);
        loadReadings();
    }

    public void saveReading() {
        Log.d("MainActivity", "Saving reading");
        String text = sensorReading.getText().toString();
        int readingInt;
        try {
            readingInt = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            readingInt = 0;
        }
        Date date = new Date();
        SensorReading reading = new SensorReading(date, readingInt, 0, (float)lat, (float)lon, 0);

        reading.save();
        Log.d("MainActivity", reading.toString());

        loadReadings();
    }

    public void loadReadings() {
        ArrayList<SensorReading> list = (ArrayList)SensorReading.getList();
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
