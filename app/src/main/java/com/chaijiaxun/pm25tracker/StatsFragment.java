package com.chaijiaxun.pm25tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaijiaxun.pm25tracker.database.SensorReading;
import com.chaijiaxun.pm25tracker.lists.StatsItem;
import com.chaijiaxun.pm25tracker.lists.StatsItemAdapter;
import com.chaijiaxun.pm25tracker.utils.DataUtils;
import com.chaijiaxun.pm25tracker.utils.UIUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
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
                dateText.setText(UIUtils.dayString(selectedDate));
                updatePage();
            }
        };

        prevButton.setOnClickListener(dateChange);
        nextButton.setOnClickListener(dateChange);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.microclimate_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        datepickerSpinner.setAdapter(adapter);

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
        updatePage();

        return fragmentView;
    }

    /**
     * Updates the page based on the selectedDate
     */
    private void updatePage() {
        List<BarEntry> entries = new ArrayList<>();
        ArrayList<StatsItem> statsItems = new ArrayList<>();

        for (int i = 0; i < 24; i++ ) { // For each hour in the day
            List<SensorReading> readingList = DataUtils.getHourReadings(selectedDate, i);
            if ( readingList.size() == 0 ) {
                continue;
            }
            int low = 0;
            int med = 0;
            int high = 0;
            double total = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for ( SensorReading reading : readingList ) {
                double level = reading.getPollutantLevel();
                max = Math.max(max, level);
                min = Math.min(min, level);
                total += level;
                if ( level < 2 ) {
                    low++;
                } else if ( level < 4 ) {
                    med++;
                } else {
                    high++;
                }
            }
            double average = total / readingList.size();

            entries.add(new BarEntry(i, new float[]{low,med,high}));
            String time = String.format("%02d", i) + ":00 - " + String.format("%02d", i+1) + ":00";
            StatsItem statsItem = new StatsItem(time, (float)min, (float)max, (float)average);
            statsItem.setAirQuality(low,med,high);
            statsItems.add(statsItem);
        }

        int[] colors = new int[3];
        colors[0] = ColorTemplate.MATERIAL_COLORS[0];
        colors[1] = ColorTemplate.MATERIAL_COLORS[1];
        colors[2] = ColorTemplate.MATERIAL_COLORS[2];

        BarDataSet dataSet = new BarDataSet(entries, "Air quality"); // add entries to dataset
        dataSet.setColors(colors);
        dataSet.setStackLabels(new String[]{"Good", "Warning", "Bad"});

        BarData bardata = new BarData(dataSet);
        statsChart.setData(bardata);
        statsChart.invalidate(); // refresh

        statsItemAdapter = new StatsItemAdapter(statsItems);
        statsListView.setAdapter(statsItemAdapter);
        statsListView.invalidate();
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {

        MyXAxisValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return String.format("%02d", (int)value) + ":00";
        }
    }
}
