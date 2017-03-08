package com.chaijiaxun.pm25tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {
    public StatsFragment() {

    }

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    BarChart barChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_stats, container, false);

        barChart = (BarChart)ret.findViewById(R.id.chart_history);

        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(1, 2));
        entries.add(new BarEntry(2, 3));
        entries.add(new BarEntry(3, 4));
        entries.add(new BarEntry(4, 5));
        entries.add(new BarEntry(5, 4));

        BarDataSet dataSet = new BarDataSet(entries, "Label"); // add entries to dataset

        BarData lineData = new BarData(dataSet);
        barChart.setData(lineData);
        barChart.invalidate(); // refresh

        // Inflate the layout for this fragment
        return ret;
    }
}
