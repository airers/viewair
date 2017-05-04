package com.chaijiaxun.pm25tracker.lists;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaijiaxun.pm25tracker.utils.AppData;
import com.chaijiaxun.pm25tracker.R;

import java.util.ArrayList;

/**
 * List Adapter for the stats item
 */

public class BTItemAdapter extends BaseAdapter {
    private BluetoothDevice[] data;
    private LayoutInflater inflator = null;

    public static class BTDevice {
        public TextView name;
        public TextView uuid;
    }


    public BTItemAdapter(BluetoothDevice[] data) {
        this.data = data;
        inflator = (LayoutInflater) AppData.getInstance().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return Math.max(1, data.length);
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

        if ( data.length > 0 ) {
            BTDevice holder;
            if ( convertView == null ) {
                // Inflate the item xml for each row
                rowView = inflator.inflate(R.layout.listitem_bluetoothdevice, null);

                // Viewholder object contains the elements
                holder = new BTDevice();
                holder.name = (TextView)rowView.findViewById(R.id.text_name);
                holder.uuid = (TextView)rowView.findViewById(R.id.text_uuid);

                rowView.setTag( holder );
            } else {
                holder = (BTDevice) rowView.getTag();
            }

            BluetoothDevice btItem = data[position];

            if ( holder != null ) {
                holder.name.setText(String.valueOf(btItem.getName()));
                holder.uuid.setText(String.valueOf(btItem.getAddress()));
            }


        } else {
            rowView = inflator.inflate(R.layout.listitem_empty, null);
            TextView tv = (TextView)rowView.findViewById(R.id.text_message);
            tv.setText("No paired devices");
        }
        return rowView;
    }
}
