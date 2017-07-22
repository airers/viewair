package com.chaijiaxun.pm25tracker.database;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.List;


/**
 * Stores device data
 */

public class DatabaseDevice extends SugarRecord {
    private String name; // The name of the bluetooth device
    private String uuid; // The UUID of the bluetooth device
    private String serverId; // The UUID assigned by the server
    private long lastSyncTime; // The last time this device synced

    // NB: Last server sync time may be slightly inaccurate.
    // It is used for calculating the number of readings unsynced
    // During syncing, the app will ping the server for the actual last reading.
    private long lastServerSyncTime; // The last time this device synced with the server

    public DatabaseDevice(){

    }

    public DatabaseDevice(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
        this.serverId = null;
        this.lastSyncTime = 0;
        this.lastServerSyncTime = 0;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getServerID() {
        return serverId;
    }

    public long getLastServerSyncTime() {
        return lastServerSyncTime;
    }

    public void setLastServerSyncTime(long lastSyncTime) {
        this.lastServerSyncTime = lastSyncTime;
        this.save();
    }

    public long getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime() {
        this.lastSyncTime = Calendar.getInstance().getTimeInMillis();
        this.save();
    }

    public void setServerID(String serverID) {
        this.serverId = serverID;
        this.save();
    }

    public static List<DatabaseDevice> getList() {
        return DatabaseDevice.listAll(DatabaseDevice.class);
    }
}
