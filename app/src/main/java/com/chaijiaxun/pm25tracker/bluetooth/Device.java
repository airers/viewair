package com.chaijiaxun.pm25tracker.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by chaij on 15/03/2017.
 */

public class Device {
    String name;
    String uuid;
    int microclimate;
    BluetoothDevice device;

    public Device() {
        name = "HC-05";
        uuid = "12345";
    }

    public Device(BluetoothDevice device) {
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
}
