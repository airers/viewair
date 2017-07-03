package com.chaijiaxun.pm25tracker;


import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.chaijiaxun.pm25tracker.database.SensorReading;
import com.chaijiaxun.pm25tracker.materialcalendar.EventDecorator;
import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.utils.DataUtils;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
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
        ArrayList<Integer> datesWithStuff = new ArrayList<>();
        HashSet<CalendarDay> cdh = new HashSet<>();

        cv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                showDateDots(widget, date);
            }
        });
        CalendarDay today = new CalendarDay();
        showDateDots(cv, today.today());

        cv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay());
                updateMap();
                mapFragment.getMapAsync(MapHistoryFragment.this);
            }
        });
        return v;
    }

    public void showDateDots(MaterialCalendarView widget, CalendarDay date){
        int selectedMonth = date.getMonth();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        HashSet<CalendarDay> selectedMonthDisplay = new HashSet<>();
        Date start = new Date();
        Date end = new Date();
        try {
            selectedMonth += 1;
            start = sdf.parse("1/" + selectedMonth +"/" + date.getYear());
            end = sdf.parse("1/" + (selectedMonth + 1) +"/" + date.getYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(start != null && end != null) {
            for (long currDate = start.getTime(); currDate < end.getTime(); currDate += 86400000) {
                Log.d("Dates", String.valueOf(new Date(currDate)));
                int countDate = (int) SensorReading.count(SensorReading.class, "time > " + currDate + " AND time <" + (currDate + 86400000), null);
                if (countDate > 0) {
                    Calendar saveDate = new GregorianCalendar();
                    saveDate.setTimeInMillis(currDate);
                    selectedMonthDisplay.add(new CalendarDay(saveDate.get(Calendar.YEAR), saveDate.get(Calendar.MONTH), saveDate.get(Calendar.DATE)));
                }
            }
            EventDecorator ed = new EventDecorator(0xFF555555, selectedMonthDisplay);
            widget.removeDecorators();
            widget.addDecorator(ed);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("ASDASDASD", Calendar.YEAR + " " + Calendar.MONTH + " " + Calendar.DAY_OF_MONTH);
        LatLng singapore = new LatLng(1.354977, 103.806936);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore,10));

        if ( selectedDate == null ) {
            selectedDate = DataUtils.getStartOfToday();
        }

        List<SensorReading> readingsList = DataUtils.getDayReadings(selectedDate);

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
