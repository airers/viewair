package com.chaijiaxun.pm25tracker.database;

import com.orm.SugarRecord;
import com.orm.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Probably the only table required in this app. Stores all the sensor readings.
 */

public class SensorReading extends SugarRecord {
    Date time;
    int sensorReading;
    int microClimate;
    float locationLat;
    float locationLon;
    int locationAccuracy;

    public SensorReading() {}

    public SensorReading(Date time, int sensorReading, int microClimate, float locationLat, float locationLon, int locationAccuracy) {
        this.time = time;
        this.sensorReading = sensorReading;
        this.microClimate = microClimate;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.locationAccuracy = locationAccuracy;
    }

    public static List<SensorReading> getList() {
        return Select.from(SensorReading.class).list();
    }
}
