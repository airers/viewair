package com.chaijiaxun.pm25tracker.database;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

public class SensorReading extends SugarRecord {
    private long time;
    private float pollutantLevel;
    private int microclimate;
    private float locationLat;
    private float locationLon;
    private float locationElevation;
    private float locationAccuracy;
    private long localDeviceID;


    public SensorReading(){

    }

    public SensorReading(long localDeviceID, Date time, float pollutantLevel, int microclimate, float locationLat, float locationLon, float locationElevation, float locationAccuracy) {
        this.localDeviceID = localDeviceID;
        this.time = time.getTime();
        this.pollutantLevel = pollutantLevel;
        this.microclimate = microclimate;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
        this.locationElevation = locationElevation;
        this.locationAccuracy = locationAccuracy;
    }
    @Override
    public String toString() {
        return pollutantLevel + " " + microclimate + " " + locationLat + " " + locationLon + " " + time;
    }

    public long getTime() {
        return time;
    }

    public double getPollutantLevel() {
        return pollutantLevel;
    }

    public int getMicroclimate() {
        return microclimate;
    }

    public float getLocationLat() {
        return locationLat;
    }

    public float getLocationLon() {
        return locationLon;
    }

    public float getLocationElevation() {
        return locationElevation;
    }

    public float getLocationAccuracy() {
        return locationAccuracy;
    }

    public long getLocalDeviceID() {
        return localDeviceID;
    }

    public static List<SensorReading> getList() {
        return SensorReading.listAll(SensorReading.class);
    }
}
