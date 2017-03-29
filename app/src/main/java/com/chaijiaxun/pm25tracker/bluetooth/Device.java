package com.chaijiaxun.pm25tracker.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.Date;

/**
 * Stores device data
 */

public class Device {
    String name;
    String uuid;
    int microclimate;
    Date deviceTime;
    BluetoothDevice device;

    public Device() {
        deviceTime = new Date();
        deviceTime.setTime(0);

        name = "HC-05";
        uuid = "12345";
    }

    public Device(BluetoothDevice device) {
        deviceTime = new Date();
        deviceTime.setTime(0);

        name = device.getName();
        uuid = device.getAddress();
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public BluetoothDevice getBluetoothDevice() {
        return device;
    }

    public int getMicroclimate() {
        return microclimate;
    }

    public void setMicroclimate(int microclimate) {
        this.microclimate = microclimate;
    }

    public void setDeviceTime(long timestamp) {
        deviceTime.setTime(timestamp);
    }

    public Date getDeviceTime() {
        return deviceTime;
    }
}
