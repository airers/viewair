package com.chaijiaxun.pm25tracker.utils;

/**
 * Type of setting
 */

public enum SettingType {
    AUTOCONNECT ("SettingAutoconnect"),
    AUTOCONNECT_DEVICE ("SettingAutoconnectDevice"),
    AUTOSYNC ("SettingAutosync"),
    STATUSBAR ("SettingStatusbar"),
    CLOUD ("SettingCloud");

    private final String name;

    SettingType(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
