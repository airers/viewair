package com.chaijiaxun.pm25tracker.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaijiaxun.pm25tracker.AppData;
import com.chaijiaxun.pm25tracker.R;

import java.util.ArrayList;

/**
 * List Adapter for the stats item
 */

public class StatsItemAdapter extends BaseAdapter {
    private ArrayList<StatsItem> data;
    private LayoutInflater inflator = null;

    public static class StatsView {
        public TextView time;
        public TextView min;
        public TextView max;
        public TextView avg;
    }


    public StatsItemAdapter(ArrayList<StatsItem> statsList) {
        data = statsList;
        inflator = (LayoutInflater) AppData.getInstance().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return Math.max(1, data.size());
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if ( data.size() > 0 ) {
            StatsView holder;
            if ( convertView == null ) {
                // Inflate the item xml for each row
                rowView = inflator.inflate(R.layout.listitem_stats, null);

                // Viewholder object contains the elements
                holder = new StatsView();
                holder.time = (TextView)rowView.findViewById(R.id.text_time);
                holder.min = (TextView)rowView.findViewById(R.id.text_min);
                holder.max = (TextView)rowView.findViewById(R.id.text_max);
                holder.avg = (TextView)rowView.findViewById(R.id.text_avg);

                rowView.setTag( holder );
            } else {
                holder = (StatsView) rowView.getTag();
            }

            StatsItem statsItem = data.get(position);

            if ( holder != null ) {
                holder.min.setText(String.valueOf(statsItem.getMin()));
                holder.avg.setText(String.valueOf(statsItem.getAvg()));
                holder.max.setText(String.valueOf(statsItem.getMax()));
                holder.time.setText(statsItem.getTime());
            }


        } else {
            rowView = inflator.inflate(R.layout.listitem_empty, null);
        }
        return rowView;
    }
}
