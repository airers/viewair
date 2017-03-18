package com.chaijiaxun.pm25tracker.bluetooth;

/**
 * Created by chaij on 15/03/2017.
 */

public class Device {
    String name;
    String uuid;

    public Device() {
        name = "HC-05";
        uuid = "12345";
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
