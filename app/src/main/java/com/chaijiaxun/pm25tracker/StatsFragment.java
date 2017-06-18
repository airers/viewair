package com.chaijiaxun.pm25tracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaijiaxun.pm25tracker.lists.StatsItem;
import com.chaijiaxun.pm25tracker.lists.StatsItemAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class StatsFragment extends Fragment {
    public StatsFragment() {

    }

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    Spinner datepickerSpinner;
    BarChart statsChart;
    ListView statsListView;

    Button prevButton, nextButton;
    Calendar selectedDate;
    TextView dateText;

    public StatsItemAdapter statsItemAdapter;

    private String dayString() {
        Calendar today = new GregorianCalendar();
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        long timeDiff = selectedDate.getTimeInMillis() - today.getTimeInMillis();
        timeDiff /= 60*60*24*1000;
        if ( timeDiff == 0 ) {
            return getString(R.string.date_today);
        } else if ( timeDiff == -1 ) {
            return getString(R.string.date_yesterday);
        } else if ( timeDiff < 0 && timeDiff >= -6 ) {
            switch (selectedDate.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    return getString(R.string.date_monday);
                case Calendar.TUESDAY:
                    return getString(R.string.date_tuesday);
                case Calendar.WEDNESDAY:
                    return getString(R.string.date_wednesday);
                case Calendar.THURSDAY:
                    return getString(R.string.date_wednesday);
                case Calendar.FRIDAY:
                    return getString(R.string.date_wednesday);
                case Calendar.SATURDAY:
                    return getString(R.string.date_wednesday);
                case Calendar.SUNDAY:
                    return getString(R.string.date_wednesday);
            }
        }
        SimpleDateFormat format;
        if ( selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) ) {
            format = new SimpleDateFormat("dd MMM");
        } else {
            format = new SimpleDateFormat("dd MMM yyyy");
        }
        return format.format(selectedDate.getTime());

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_stats, container, false);

        statsChart = (BarChart)fragmentView.findViewById(R.id.chart_history);
        statsListView = (ListView)fragmentView.findViewById(R.id.listview_stats);
        datepickerSpinner = (Spinner)fragmentView.findViewById(R.id.spinner_day);
        prevButton = (Button)fragmentView.findViewById(R.id.button_prev);
        nextButton = (Button)fragmentView.findViewById(R.id.button_next);
        dateText = (TextView)fragmentView.findViewById(R.id.text_date);
        selectedDate = new GregorianCalendar();
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);
        selectedDate.set(Calendar.HOUR, 0);
        selectedDate.set(Calendar.MINUTE, 0);


        View.OnClickListener dateChange = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int direction = 1;
                if ( v == prevButton ) {
                    direction = -1;
                }
                selectedDate.add(Calendar.DAY_OF_MONTH, direction);
                dateText.setText(dayString());
            }
        };

        prevButton.setOnClickListener(dateChange);
        nextButton.setOnClickListener(dateChange);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.microclimate_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        datepickerSpinner.setAdapter(adapter);

        int[] colors = new int[3];
        colors[0] = ColorTemplate.MATERIAL_COLORS[0];
        colors[1] = ColorTemplate.MATERIAL_COLORS[1];
        colors[2] = ColorTemplate.MATERIAL_COLORS[2];

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, new float[]{2, 1, 3}));
        entries.add(new BarEntry(2, new float[]{2, 4, 2}));
        entries.add(new BarEntry(3, new float[]{4, 1, 4}));
        entries.add(new BarEntry(4, new float[]{2, 5, 2}));
        entries.add(new BarEntry(5, new float[]{2, 1, 4}));
        entries.add(new BarEntry(7, new float[]{3, 2, 4}));
        entries.add(new BarEntry(8, new float[]{1, 4, 4}));
        entries.add(new BarEntry(10, new float[]{5, 2, 1}));

        BarDataSet dataSet = new BarDataSet(entries, "Air quality"); // add entries to dataset
        dataSet.setColors(colors);
        dataSet.setStackLabels(new String[]{"Good", "Warning", "Bad"});

        BarData bardata = new BarData(dataSet);
        statsChart.setData(bardata);
        statsChart.invalidate(); // refresh
        statsChart.setScaleYEnabled(false);
        XAxis xAxis = statsChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MyXAxisValueFormatter());

        YAxis yAxisLeft = statsChart.getAxisLeft();

        YAxis yAxisRight = statsChart.getAxisRight();
        yAxisRight.setEnabled(false);

        ArrayList<StatsItem> statsItems = new ArrayList<>();
        statsItems.add(new StatsItem("17:00 - 18:00", 12, 14, 15));
        statsItems.add(new StatsItem("18:00 - 19:00", 12, 14, 15));
        statsItems.add(new StatsItem("19:00 - 20:00", 12, 14, 15));
        statsItems.add(new StatsItem("20:00 - 21:00", 12, 14, 15));
        statsItems.add(new StatsItem("21:00 - 22:00", 12, 14, 15));
        statsItems.add(new StatsItem("22:00 - 23:00", 12, 14, 15));
        statsItems.add(new StatsItem("23:00 - 00:00", 12, 14, 15));

        statsItemAdapter = new StatsItemAdapter(statsItems);

        statsListView.setAdapter(statsItemAdapter);


        return fragmentView;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        public MyXAxisValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return String.format("%02d", (int)value) + ":00";
        }
    }
}
