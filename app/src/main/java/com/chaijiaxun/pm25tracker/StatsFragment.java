package com.chaijiaxun.pm25tracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chaijiaxun.pm25tracker.lists.StatsItem;
import com.chaijiaxun.pm25tracker.lists.StatsItemAdapter;
import com.github.mikephil.charting.charts.BarChart;
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

    BarChart statsChart;
    ListView statsListView;

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

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 2));
        entries.add(new BarEntry(2, 3));
        entries.add(new BarEntry(3, 4));
        entries.add(new BarEntry(4, 5));
        entries.add(new BarEntry(5, 4));

        BarDataSet dataSet = new BarDataSet(entries, "Hour"); // add entries to dataset

        BarData bardata = new BarData(dataSet);
        statsChart.setData(bardata);
        statsChart.invalidate(); // refresh

        ArrayList<StatsItem> statsItems = new ArrayList<>();
        statsItems.add(new StatsItem("5pm", 12, 14, 15));
        statsItems.add(new StatsItem("6pm", 12, 14, 15));
        statsItems.add(new StatsItem("7pm", 12, 14, 15));
        statsItems.add(new StatsItem("8pm", 12, 14, 15));
        statsItems.add(new StatsItem("9pm", 12, 14, 15));
        statsItems.add(new StatsItem("10pm", 12, 14, 15));
        statsItems.add(new StatsItem("11pm", 12, 14, 15));

        statsItemAdapter = new StatsItemAdapter(statsItems);

        statsListView.setAdapter(statsItemAdapter);


        return fragmentView;
    }
}
