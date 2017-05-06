package com.chaijiaxun.pm25tracker.database;

import com.orm.SugarRecord;

import java.util.List;


/**
 * Stores device data
 */

public class DatabaseDevice extends SugarRecord {
    private String name; // The name of the bluetooth device
    private String uuid; // The UUID of the bluetooth device
    private String serverId; // The ID assigned by the server

    public DatabaseDevice(){

    }

    public DatabaseDevice(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
        this.serverId = null;
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

    public String setServerID() {
        String serverID = "1";
        this.serverId = serverID;
        this.save();
        return serverID;
    }

    public static List<DatabaseDevice> getList() {
        return DatabaseDevice.listAll(DatabaseDevice.class);
    }
}
