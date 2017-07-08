package com.chaijiaxun.pm25tracker.calendar;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaijiaxun.pm25tracker.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

/**
 * Created by Shalom Quek on 8/7/2017.
 */

public class CalendarDialog extends DialogFragment{
    MaterialCalendarView cv;

    public CalendarDialog(){
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        cv = (MaterialCalendarView) v.findViewById(R.id.calendarView);
        //View view = inflater.inflate(R.layout.fragment_edit_name, container);
        //mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        //getDialog().setTitle("Hello");

        return v;
    }


}
