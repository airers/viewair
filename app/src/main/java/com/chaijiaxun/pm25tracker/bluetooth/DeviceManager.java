package com.chaijiaxun.pm25tracker.bluetooth;

/**
 * Created by chaij on 15/03/2017.
 */

public class DeviceManager {
    static private DeviceManager singleton = new DeviceManager();
    static public DeviceManager getInstance() {
        return singleton;
    }
    Device currentDevice;
    int connectionStatus;

    private DeviceManager() {

    }

    public void setCurrentDevice(Device d) {
        currentDevice = d;
    }

    public boolean hasLastDevice() {
        return currentDevice != null;
    }

    public boolean isDeviceConnected() {
        return false;
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }
}
