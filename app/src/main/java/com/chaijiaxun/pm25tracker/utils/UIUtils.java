package com.chaijiaxun.pm25tracker.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.chaijiaxun.pm25tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UIUtils {

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    public static String dayString(Calendar selectedDate) {
        Calendar today = DataUtils.getStartOfToday();
        long timeDiff = selectedDate.getTimeInMillis() - today.getTimeInMillis();
        timeDiff /= 60*60*24*1000;
        if ( timeDiff == 0 ) {
            return AppData.getInstance().getApplicationContext().getString(R.string.date_today);
        } else if ( timeDiff == -1 ) {
            return AppData.getInstance().getApplicationContext().getString(R.string.date_yesterday);
        } else if ( timeDiff < 0 && timeDiff >= -6 ) {
            switch (selectedDate.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    return AppData.getInstance().getApplicationContext().getString(R.string.date_monday);
                case Calendar.TUESDAY:
                    return AppData.getInstance().getApplicationContext().getString(R.string.date_tuesday);
                case Calendar.WEDNESDAY:
                    return AppData.getInstance().getApplicationContext().getString(R.string.date_wednesday);
                case Calendar.THURSDAY:
                    return AppData.getInstance().getApplicationContext().getString(R.string.date_thursday);
                case Calendar.FRIDAY:
                    return AppData.getInstance().getApplicationContext().getString(R.string.date_friday);
                case Calendar.SATURDAY:
                    return AppData.getInstance().getApplicationContext().getString(R.string.date_saturday);
                case Calendar.SUNDAY:
                    return AppData.getInstance().getApplicationContext().getString(R.string.date_sunday);
            }
        }
        SimpleDateFormat format;
        if ( selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) ) {
            format = new SimpleDateFormat("dd MMM");
        } else {
            format = new SimpleDateFormat("dd MMM yyyy");
        }
        return format.format(selectedDate.getTime());

    }

    public static String microclimateString(int microclimate) {
        String [] array = AppData.getInstance().getApplicationContext().getResources().getStringArray(R.array.microclimate_array);
        if ( microclimate < 0 || microclimate >= array.length - 1 ) {
            return "";
        }
        return array[microclimate+1];
    }
}