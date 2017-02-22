package com.chaijiaxun.pm25tracker.database;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Probably the only table required in this app. Stores all the sensor readings.
 */

@Table(name = "sensor_readings")
public class SensorReading extends Model {
    @Column(name = "time")
    long time;
    @Column(name ="pollutant_level")
    int pollutantLevel;
    @Column(name ="microclimate")
    int microclimate;
    @Column(name ="location_lat")
    float locationLat;
    @Column(name ="location_lon")
    float locationLon;
    @Column(name ="location_accuracy")
    int locationAccuracy;

    public SensorReading() {}

    public SensorReading(Date time, int pollutantLevel, int microclimate, float locationLat, float locationLon, int locationAccuracy) {
        this.time = time.getTime();
        this.pollutantLevel = pollutantLevel;
        this.microclimate = microclimate;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.locationAccuracy = locationAccuracy;
    }

    public static List<SensorReading> getList() {
        return new Select().from(SensorReading.class).execute();
    }

    public static void deleteAll() {
        // Delete all rows from table
        ActiveAndroid.execSQL(String.format("DELETE FROM %s;", "sensor_readings"));

        // Reset ids
        ActiveAndroid.execSQL(String.format("DELETE FROM sqlite_sequence WHERE name='%s';", "sensor_readings"));
    }

    @Override
    public String toString() {
        return pollutantLevel + " " + microclimate + " " + locationLat + " " + locationLon + " " + time;
    }
}
