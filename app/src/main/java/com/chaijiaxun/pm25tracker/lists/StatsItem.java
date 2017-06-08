package com.chaijiaxun.pm25tracker.lists;

/**
 * Used for displaying a single item in the stats screen
 */

public class StatsItem {
    private String time;
    private float min;
    private float max;
    private float avg;
    private float airGood = 4;
    private float airMed = 3;
    private float airBad = 1;

    StatsItem() {
        time = "00:00";
        min =  0;
        max = 0;
        avg = 0;
    }

    public StatsItem(String time, float min, float max, float avg) {
        this.time = time;
        this.min = min;
        this.max = max;
        this.avg = avg;
    }

    public void setAirQuality(float bad, float med, float good) {
        airBad = bad;
        airGood = good;
        airMed = med;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public float getAirGood() {
        return airGood;
    }

    public float getAirMed() {
        return airMed;
    }

    public float getAirBad() {
        return airBad;
    }
}
