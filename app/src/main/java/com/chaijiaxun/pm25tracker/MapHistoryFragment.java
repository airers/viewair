package com.chaijiaxun.pm25tracker;


import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.chaijiaxun.pm25tracker.database.SensorReading;
import com.chaijiaxun.pm25tracker.utils.AppData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapHistoryFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Calendar selectedDate;
    private SupportMapFragment mapFragment;

    TileOverlay mOverlay;

    public MapHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapHistoryFragment newInstance(String param1, String param2) {
        MapHistoryFragment fragment = new MapHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);


        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(MapHistoryFragment.this); // Calls the onMapReady method
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_history, container, false);
        MaterialCalendarView cv = (MaterialCalendarView) v.findViewById(R.id.calendarView);
        /*cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = new GregorianCalendar(year, month, dayOfMonth);
                int d = dayOfMonth;
                int m = month;
                int y = year;
                Log.d("Load Readings", String.valueOf(d));
                Log.d("Load Readings", String.valueOf(m));
                updateMap();
                //FragmentManager fm = getChildFragmentManager();
                //mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapHistoryFragment.this);
            }
        });*/
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("ASDASDASD", Calendar.YEAR + " " + Calendar.MONTH + " " + Calendar.DAY_OF_MONTH);
        LatLng singapore = new LatLng(1.354977, 103.806936);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,10));

        //List<SensorReading> readingsList = SensorReading.listAll(SensorReading.class);
        long sDate;
        if(selectedDate == null){
            selectedDate = Calendar.getInstance();
             sDate = new GregorianCalendar(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).getTime().getTime();
        }
        else sDate = selectedDate.getTime().getTime();

        List<SensorReading> readingsList = SensorReading.findWithQuery(SensorReading.class, "Select * from SENSOR_READING where time > " + sDate + " AND time < " + (sDate + 86400000));
        //if (readingsList.size() < 1)
        ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
        if ( readingsList.size() > 0 ) {
            for (int i = 0; i < readingsList.size(); i++) {
                //Date startDate = new Date(y, m, d);
                //long readingDate = readingsList.get(i).getTime();
                list.add(new WeightedLatLng(new LatLng(readingsList.get(i).getLocationLat(),
                        readingsList.get(i).getLocationLon()), readingsList.get(i).getPollutantLevel() / 5));
            }

            int[] colors = {
                    Color.rgb(102, 225, 0), // green
                    Color.rgb(255, 0, 0)    // red
            };

            float[] startPoints = {
                    0.2f, 1f
            };

            Gradient gradient = new Gradient(colors, startPoints);
            HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                    .weightedData(list)
                    .gradient(gradient)
                    .radius(30)
                    .opacity(0.8)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }
    private void updateMap(){
        if(mOverlay != null) mOverlay.remove();
    }
}
