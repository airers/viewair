package com.chaijiaxun.pm25tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.chaijiaxun.pm25tracker.database.DatabaseSeed;
import com.chaijiaxun.pm25tracker.database.SensorReading;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ReadingsFragment extends Fragment{
    EditText sensorReading;
    ListView readingList;
    double lat, lon;

    public ReadingsFragment() {

    }

    public static ReadingsFragment newInstance() {
        ReadingsFragment fragment = new ReadingsFragment();
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

        View view = inflater.inflate(R.layout.fragment_readings, container, false);



        readingList = (ListView) view.findViewById(R.id.readings_view);
        loadReadings();

        return view;

    }

    public void loadReadings() {
        ArrayList<SensorReading> list =(ArrayList) SensorReading.getList();
        String[] values;
        String outputPattern = "dd/MMM h:mm a";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Log.d("Load Readings", list.size() + " ");
        if ( list.size() > 0 ) {
            values = new String[list.size()];
            for ( int i = 0; i < list.size(); i++ ) {

                values[i] = "Time: " + String.valueOf(outputFormat.format(list.get(i).getTime()));
                values[i] += "\nReading: " + String.valueOf(list.get(i).getPollutantLevel());
                values[i] += "\nmClimate: " + String.valueOf(list.get(i).getMicroclimate());
            }
        } else {
            values = new String[] { "Nothing in database" };
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        readingList.setAdapter(adapter);

    }
}
