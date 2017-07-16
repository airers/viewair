package com.chaijiaxun.pm25tracker.calendar;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chaijiaxun.pm25tracker.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

/**
 * Created by Shalom Quek on 8/7/2017.
 */

public class CalendarDialog extends DialogFragment{
    MaterialCalendarView cv;
    Button cancelButton, todayButton, selectButton;
    OnMyDialogResult mDialogResult; // the callback


    public CalendarDialog(){
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        cv = (MaterialCalendarView) v.findViewById(R.id.calendarView);
        cancelButton = (Button)v.findViewById(R.id.button_cancel);
        todayButton = (Button)v.findViewById(R.id.button_today);
        selectButton = (Button)v.findViewById(R.id.button_select);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Calendar Picker", "today pressed");
                cv.setDateSelected(Calendar.getInstance(), true);
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("Calendar Picker", String.valueOf(cv.getSelectedDate()));
            if( mDialogResult != null ){
                mDialogResult.finish(cv.getSelectedDate());
            }
            getDialog().dismiss();
            }
        });

        return v;
    }

    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(CalendarDay result);
    }

}
