package com.chaijiaxun.pm25tracker.bluetooth;

/**
 * Packet for sending and receiving arduino stuff
 */

public class BTPacket {
    public static final byte TYPE_FALSE                  = 0;
    public static final byte TYPE_TRUE                   = 1;
    public static final byte TYPE_CONNECTION_CHECK       = 2;
    public static final byte TYPE_CONNECTION_ACK         = 3;
    public static final byte TYPE_GET_TIME               = 4;
    public static final byte TYPE_SET_TIME               = 5;
    public static final byte TYPE_TIME_PACKET            = 6;
    public static final byte TYPE_GET_READINGS           = 7;
    public static final byte TYPE_READING_COUNTING       = 8;
    public static final byte TYPE_READING_COUNT          = 9;
    public static final byte TYPE_READY_TO_RECEIVE       = 10;
    public static final byte TYPE_READING_PACKET         = 11;
    public static final byte TYPE_READINGS_RECEIVED      = 12;
    public static final byte TYPE_GET_MICROCLIMATE       = 13;
    public static final byte TYPE_SET_MICROCLIMATE       = 14;
    public static final byte TYPE_MICROCLIMATE_PACKET    = 15;

    public BTPacket(int type) {

    }

    public BTPacket(byte [] bytes) {

    }

}
