package com.chaijiaxun.pm25tracker.utils;

import com.chaijiaxun.pm25tracker.database.DatabaseDevice;
import com.chaijiaxun.pm25tracker.database.SensorReading;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
     * Gets a count of all the readings from the given time non inclusive
     * @param device Device to get readings of
     * @param startTime timestamp in mills, non inclusive
     * @return
     */
    public static int getReadingCountFrom(DatabaseDevice device, long startTime) {
        return Select.from(SensorReading.class).
                where(
                        Condition.prop(NamingHelper.toSQLNameDefault("localDeviceID")).eq(device.getId()),
                        Condition.prop(NamingHelper.toSQLNameDefault("time")).gt(startTime)
                ).list().size();
    }

    /**
     * Gets a list of all the readings from the given time non inclusive
     * @param device Device to get readings of
     * @param startTime timestamp in mills, non inclusive
     */
    public static List<SensorReading> getReadingsFrom(DatabaseDevice device, long startTime, int limit) {
        return Select.from(SensorReading.class).
                where(
                        Condition.prop(NamingHelper.toSQLNameDefault("localDeviceID")).eq(device.getId()),
                        Condition.prop(NamingHelper.toSQLNameDefault("time")).gt(startTime)
                ).
                orderBy("time").
                limit(String.valueOf(limit)).
                list();
    }

    public static List<SensorReading> getReadingsFrom(DatabaseDevice device, long startTime) {
        return getReadingsFrom(device, startTime, 100);
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

    public static long SQLTimestampToMills(String sqlTimestamp) {

        try {
            DateFormat formatter = new SimpleDateFormat("y-M-d k:m:s.S");
            Date date = formatter.parse(sqlTimestamp.replace('T', ' ').replace('Z', ' '));
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Calendar millsToDate(long mills) {
        Calendar converted = Calendar.getInstance();
        converted.setTimeInMillis(mills);
        return converted;
    }

    public static String dateToSQLDate(Calendar cal) {
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s+00");
        return sqlFormat.format(cal.getTime());
    }
}
