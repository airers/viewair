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
import android.widget.TextView;

import com.chaijiaxun.pm25tracker.database.DatabaseSeed;
import com.chaijiaxun.pm25tracker.database.SensorReading;
import com.chaijiaxun.pm25tracker.utils.DataUtils;
import com.chaijiaxun.pm25tracker.utils.UIUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class ReadingsFragment extends Fragment {
    EditText sensorReading;
    ListView readingList;
    double lat, lon;

    Button prevButton, nextButton;
    Calendar selectedDate;
    TextView dateText, filteredText, totalText;

    int selectedMicroclimate = -1;

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

        prevButton = (Button) view.findViewById(R.id.button_prev);
        nextButton = (Button) view.findViewById(R.id.button_next);
        dateText = (TextView) view.findViewById(R.id.text_date);
        filteredText = (TextView) view.findViewById(R.id.text_filteredNumber);
        totalText = (TextView) view.findViewById(R.id.text_totalNumber);

        selectedDate = new GregorianCalendar();
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);
        selectedDate.set(Calendar.HOUR, 0);
        selectedDate.set(Calendar.MINUTE, 0);

        View.OnClickListener dateChange = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int direction = 1;
                if (v == prevButton) {
                    direction = -1;
                }
                selectedDate.add(Calendar.DAY_OF_MONTH, direction);
                dateText.setText(UIUtils.dayString(selectedDate));
                updatePage();
            }
        };

        prevButton.setOnClickListener(dateChange);
        nextButton.setOnClickListener(dateChange);


        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_microclimate);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.microclimate_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String selected = (String) parent.getItemAtPosition(pos);
                Log.d("Load Readings", selected);
                switch (selected) {
                    case "Indoors":
                        selectedMicroclimate = 0;
                        break;
                    case "Outdoors":
                        selectedMicroclimate = 1;
                        break;
                    case "All":
                        selectedMicroclimate = -1;
                        break;
                }
                updatePage();
                Log.d("OnDateChangeListener", (String) parent.getItemAtPosition(pos));
                //Toast.makeText(MyActivity.this, "Hello Toast",Toast.LENGTH_SHORT).show();
                //loadReadings();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });

        sensorReading = (EditText) view.findViewById(R.id.sensor_reading);
        readingList = (ListView) view.findViewById(R.id.readings_view);
        updatePage();

        return view;

    }

    /**
     * Updates the page based on the selectedDate
     */
    private void updatePage() {
        int filtered = 0;
        List<SensorReading> list = DataUtils.getDayReadings(selectedDate);
        String[] values;
        String outputPattern = "dd MMM h:mm a";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Log.d("Load Readings", list.size() + " ");
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) { // Count the filtered first
                SensorReading reading = list.get(i);
                if ( selectedMicroclimate != -1 && selectedMicroclimate != reading.getMicroclimate() ) {
                    continue; // Skip non selected microclimates
                }
                filtered++;
            }
            values = new String[filtered];
            int valI = 0;
            for (int i = 0; i < list.size(); i++) {
                SensorReading reading = list.get(i);
                if ( selectedMicroclimate != -1 && selectedMicroclimate != reading.getMicroclimate() ) {
                    continue; // Skip non selected microclimates
                }
                values[valI] = "Time: " + String.valueOf(outputFormat.format(reading.getTime()));
                values[valI] += "\nReading: " + String.valueOf(reading.getPollutantLevel());
                values[valI] += "\nmClimate: " + UIUtils.microclimateString(reading.getMicroclimate());
                valI++;
            }
        } else {
            values = new String[]{"Nothing in database"};
        }

        filteredText.setText(String.valueOf(filtered));
        totalText.setText(String.valueOf(list.size()));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        readingList.setAdapter(adapter);

    }
}
