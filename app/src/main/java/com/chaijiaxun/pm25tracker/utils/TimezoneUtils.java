package com.chaijiaxun.pm25tracker.utils;

import android.util.Log;

import java.util.TimeZone;

/**
 * Created by chaij on 26/07/2017.
 */

public class TimezoneUtils {
    private static class TimezoneObject {
        private int rawOffset;
        private String name;

        public TimezoneObject(float hourOffset, String name) {
            this.rawOffset = (int)(hourOffset * 60 * 60 * 1000);
            this.name = name;
        }

        public int getRawOffset() {
            return rawOffset;
        }

        public String getName() {
            return name;
        }
    }
    private static TimezoneObject [] timezones = {
            new TimezoneObject(6, "UTC+6"),
            new TimezoneObject(7, "UTC+7"),
            new TimezoneObject(8, "UTC+8"),
            new TimezoneObject(9, "UTC+9"),
            new TimezoneObject(10, "UTC+10"),
            new TimezoneObject(11, "UTC+11"),
            new TimezoneObject(12, "UTC+12"),

    };
    public static String [] getNames() {
        String [] timezoneNames = new String[timezones.length];

        for (int i = 0; i < timezones.length; i++) {
            timezoneNames[i] = timezones[i].getName();
        }

        return timezoneNames;
    }

    /**
     *
     * @param timezoneOffset in millis
     * @return Index of the selected timezone -1 if doesn't exist
     */
    public static int indexOfTimezone(int timezoneOffset) {
        for (int i = 0; i < timezones.length; i++) {
            if ( timezones[i].getRawOffset() == timezoneOffset ) {
                return i;
            }
        }
        return -1;
    }

    public static int getTimezoneOffset(int timezoneIndex) {
        return timezones[timezoneIndex].getRawOffset();
    }

    public static int getPhoneTimezone() {
        TimeZone timezone = TimeZone.getDefault();
//        Log.d("Timezone", timezone.getDisplayName());
//        Log.d("Timezone", timezone.getID());
//        Log.d("Timezone", timezone.getRawOffset() + "" );
//
//        String [] timezones = TimeZone.getAvailableIDs(timezone.getRawOffset());
//        for ( String time : timezones ) {
//            Log.d("Timezone", time);
//        }
        return timezone.getRawOffset();
    }
}
