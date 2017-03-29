package com.chaijiaxun.pm25tracker.utils;

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

    public static byte[] reverseArray(byte [] bytes ) {
        for(int i = 0; i < bytes.length / 2; i++)
        {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
        return bytes;
    }

    public static long arduinoLongToAndroidLong(byte [] time) {
        time = reverseArray(time);
        long ret = (long)byteArrayToInt(time);
        return ret * 1000;

    }

    /**
     * @return byte array of length 4
     */
    public static byte [] androidLongToAndroidLong(long data) {
        int dataInt = (int) (data / 1000);
        byte [] dataBytes = intToByteArray(dataInt);
        return reverseArray(dataBytes);
    }
}
