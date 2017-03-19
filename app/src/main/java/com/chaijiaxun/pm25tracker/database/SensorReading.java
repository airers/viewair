package com.chaijiaxun.pm25tracker.database;

import com.activeandroid.query.Select;
import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

public class SensorReading extends SugarRecord{
    long time;
    int pollutantLevel;
    int microclimate;
    float locationLat;
    float locationLon;
    int locationAccuracy;

    public SensorReading(){

    }

    public SensorReading(Date time, int pollutantLevel, int microclimate, float locationLat, float locationLon, int locationAccuracy) {
        this.time = time.getTime();
        this.pollutantLevel = pollutantLevel;
        this.microclimate = microclimate;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.locationAccuracy = locationAccuracy;
    }
    @Override
    public String toString() {
        return pollutantLevel + " " + microclimate + " " + locationLat + " " + locationLon + " " + time;
    }

    public static List<SensorReading> getList() {
        return SensorReading.listAll(SensorReading.class);
    }
}
