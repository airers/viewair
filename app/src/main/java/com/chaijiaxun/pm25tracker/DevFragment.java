package com.chaijiaxun.pm25tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.chaijiaxun.pm25tracker.database.DatabaseDevice;
import com.chaijiaxun.pm25tracker.database.DatabaseSeed;
import com.chaijiaxun.pm25tracker.database.SensorReading;
import com.chaijiaxun.pm25tracker.server.ServerDataManager;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DevFragment extends Fragment {

    ListView readingList;
    ListView devicesList;

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

        View view = inflater.inflate(R.layout.fragment_dev, container, false);


        final Button deleteButton = (Button) view.findViewById(R.id.button_db_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickDbDelete();
            }
        });

        final Button dbSeedReadingButton = (Button) view.findViewById(R.id.button_db_seed_reading);
        dbSeedReadingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickDbReadingSeed();
            }
        });

        final Button dbSeedDeviceButton = (Button) view.findViewById(R.id.button_db_seed_device);
        dbSeedDeviceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickDbDeviceSeed();
            }
        });

        final Button dev1 = (Button) view.findViewById(R.id.button_dev1);
        dev1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ServerDataManager.setServerID(null);
            }
        });

        final Button dev2 = (Button) view.findViewById(R.id.button_dev2);
        dev2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ServerDataManager.sendDeviceReadings(null);
            }
        });

        final Button dev3 = (Button) view.findViewById(R.id.button_dev3);
        dev3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        readingList = (ListView) view.findViewById(R.id.readings_view);
        devicesList = (ListView) view.findViewById(R.id.devices_view);
        loadReadings();
        loadDevices();

        return view;

    }

    public void clickDbDelete() {
        SensorReading.deleteAll(SensorReading.class);
        DatabaseDevice.deleteAll(DatabaseDevice.class);
        loadReadings();
        loadDevices();
    }

    public void clickDbDeviceSeed() {
        DatabaseSeed.seedDevice();

        loadReadings();
        loadDevices();
    }

    public void clickDbReadingSeed() {
        DatabaseSeed.seedReadings(10);

        loadReadings();
        loadDevices();
    }

    public void loadReadings() {
        List<SensorReading> list = Select.from(SensorReading.class).orderBy("time").list();
        String[] values;
        String outputPattern = "dd MMM h:mm a";
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Log.d("Load Readings", list.size() + " ");
        if ( list.size() > 0 ) {
            values = new String[list.size()];
            for ( int i = 0; i < list.size(); i++ ) {
                values[i] = "Device: " + String.valueOf(list.get(i).getLocalDeviceID());
                values[i] += "\n" + String.valueOf(outputFormat.format(list.get(i).getTime()));
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
    public void loadDevices() {
        ArrayList<DatabaseDevice> list = (ArrayList)DatabaseDevice.getList();
        String[] values;
        Log.d("Load Readings", list.size() + " ");
        if ( list.size() > 0 ) {
            values = new String[list.size()];
            for ( int i = 0; i < list.size(); i++ ) {
                values[i] = "ID: " + String.valueOf(list.get(i).getId());
                values[i] += "\nUUID: " + String.valueOf(list.get(i).getUuid());

                values[i] += "\nName: " + String.valueOf(list.get(i).getName());
            }
        } else {
            values = new String[] { "Nothing in database" };
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        devicesList.setAdapter(adapter);

    }
}
