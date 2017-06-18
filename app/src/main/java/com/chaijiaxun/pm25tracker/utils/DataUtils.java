package com.chaijiaxun.pm25tracker.utils;

import com.chaijiaxun.pm25tracker.database.SensorReading;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Utility functions for database selection
 */

public class DataUtils {
    /**
     * Gets the readings of a given  day
     * @param selectedDate Selected date to get readings
     * @return List of readings for whole day
     */
    public static List<SensorReading> getDayReadings(Calendar selectedDate) {
        long sDate, eDate;
        if(selectedDate == null){
            selectedDate = new GregorianCalendar();
        }
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);
        selectedDate.set(Calendar.HOUR, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        sDate = selectedDate.getTimeInMillis();
        eDate = sDate + 86400000;
        return SensorReading.findWithQuery(SensorReading.class, "SELECT * from SENSOR_READING where time >= " + sDate + " AND time < " + eDate);
    }

    /**
     * Gets the readings of a given hour in a day
     * @param selectedDate Selected date to get readings
     * @param hour 0 indexed (if >= 24, it will leak to next day)
     * @return List of readings from hour to hour + 1
     */
    public static List<SensorReading> getHourReadings(Calendar selectedDate, int hour) {
        long sDate, eDate;
        if(selectedDate == null){
            selectedDate = new GregorianCalendar();
        }
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);
        selectedDate.set(Calendar.HOUR, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        sDate = selectedDate.getTimeInMillis() + (hour * 60 * 60 * 1000);
        eDate = sDate + 60 * 60 * 1000;
        return SensorReading.findWithQuery(SensorReading.class, "SELECT * from SENSOR_READING where time >= " + sDate + " AND time < " + eDate);
    }

    /**
     * Returns a calendar object with the start of the day
     */
    public static Calendar getStartOfToday() {
        Calendar today = new GregorianCalendar();
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        return today;
    }
}
