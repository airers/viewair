package com.chaijiaxun.pm25tracker.utils;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Utilities to wrangle with the byte conversions
 */

public class ByteUtils {
    public static byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public static int byteArrayToInt(byte[] b) {
        return (b[3] & 0xFF) + ((b[2] & 0xFF) << 8) + ((b[1] & 0xFF) << 16) + ((b[0] & 0xFF) << 24);
    }

    public static String byteArrayToString(List<Byte> bytes) {
        if ( bytes == null || bytes.size() == 0 ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for ( byte b : bytes ) {
            sb.append(b&255);
            sb.append(" ");
        }
        sb.setLength(sb.length() - 1); // Remove last space;
        return sb.toString();
    }

    public static String byteArrayToString(byte[] bytes) {
        if ( bytes == null || bytes.length == 0 ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for ( byte b : bytes ) {
            sb.append(b&255);
            sb.append(" ");
        }
        sb.setLength(sb.length() - 1); // Remove last space;
        return sb.toString();
    }

    public static byte[] byteArrayToFloat(float f) {
        return ByteBuffer.allocate(4).putFloat(f).array();
    }
    public static float floatToByteArray(byte[] bytes) {
        if ( bytes == null || bytes.length != 4 ) return 0;
        return ByteBuffer.wrap(bytes).getFloat();
    }

    public static int uint16ToInt(byte[] bytes) {
        return ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff);
    }

    public static byte[] int16ToByteArray(int a) {
        byte[] ret = new byte[2];
        ret[1] = (byte) (a & 0xFF);
        ret[0] = (byte) ((a >> 8) & 0xFF);
        return ret;
    }

    public static byte[] reverseArray(byte [] bytes ) {
        for(int i = 0; i < bytes.length / 2; i++)
        {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
        return bytes;
    }

    public static byte[] androidIntToArduinoUint16(int d) {
        byte [] bytes = int16ToByteArray(d);
        bytes = reverseArray(bytes);
        return bytes;
    }

    public static int arduinoUint16ToAndroidInt(byte[] bytes) {
        bytes = reverseArray(bytes);
        return uint16ToInt(bytes);
    }

    public static long arduinoLongTSToAndroidLongTS(byte [] time) {
        time = reverseArray(time);
        long ret = (long)byteArrayToInt(time);
        return ret * 1000;

    }

    /**
     * @return byte array of length 4
     */
    public static byte [] androidLongTSToAndroidLongTS(long data) {
        int dataInt = (int) (data / 1000);
        byte [] dataBytes = intToByteArray(dataInt);
        return reverseArray(dataBytes);
    }
}
