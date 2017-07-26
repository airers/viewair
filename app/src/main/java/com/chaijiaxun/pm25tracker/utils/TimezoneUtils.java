package com.chaijiaxun.pm25tracker.utils;

import android.util.Log;

import java.util.TimeZone;

/**
 * Created by chaij on 26/07/2017.
 */

public class TimezoneUtils {
    private static class TimezoneObject {
        private float rawOffset;
        private String name;

        public TimezoneObject(float hourOffset, String name) {
            this.rawOffset = hourOffset * 60 * 60 * 1000;
            this.name = name;
        }

        public float getRawOffset() {
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
    };
    public static String [] getNames() {
        String [] timezoneNames = new String[timezones.length];

        for (int i = 0; i < timezones.length; i++) {
            timezoneNames[i] = timezones[i].getName();
        }

        return timezoneNames;
    }

    public static int getPhoneTimezone() {
        TimeZone timezone = TimeZone.getDefault();
        Log.d("Timezone", timezone.getDisplayName());
        Log.d("Timezone", timezone.getID());
        Log.d("Timezone", timezone.getRawOffset() + "" );

        String [] timezones = TimeZone.getAvailableIDs(timezone.getRawOffset());
        for ( String time : timezones ) {
            Log.d("Timezone", time);
        }

        return timezone.getRawOffset();
    }
}
