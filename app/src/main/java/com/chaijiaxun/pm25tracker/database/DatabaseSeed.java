package com.chaijiaxun.pm25tracker.database;

import android.app.Application;
import android.widget.Toast;

import com.chaijiaxun.pm25tracker.utils.AppData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class DatabaseSeed {

    public static void seedReadings(int num){
        Random rand = new Random();
        Calendar date;

        int deviceCount = DatabaseDevice.getList().size();
        if ( deviceCount == 0 ) {
            Toast.makeText(AppData.getInstance().getApplicationContext(), "Need at least one device to seed readings", Toast.LENGTH_SHORT).show();
            return;
        }

        //1.468460, 103.573813
        //1.267951, 104.079557
        for(int i = 0; i < num; i++){
            int index = rand.nextInt(deviceCount);
            long databaseDeviceID = DatabaseDevice.getList().get(index).getId();
            date = Calendar.getInstance();
            date = new GregorianCalendar(2017, date.get(Calendar.MONTH), rand.nextInt(30)+1);
            date.set(Calendar.HOUR_OF_DAY, rand.nextInt(24));
            date.set(Calendar.MINUTE, rand.nextInt(60));
            date.set(Calendar.SECOND, rand.nextInt(60));
            long t= date.getTimeInMillis();
            int pollutantRandom = rand.nextInt(5) + 1;
            int microclimateRandom = rand.nextInt(2);
            float latRandom = (float) ((rand.nextInt(200000)*0.000001) + 1.267951);
            float lonRandom = (float) ((rand.nextInt(500000)*0.000001) + 103.573813);
            float locEleRandom = (float) (rand.nextInt(100) - 20);
            float locAccRandom = (float) (rand.nextInt(7) + 1);

            SensorReading newReading = new SensorReading(databaseDeviceID, new Date(t + ((rand.nextInt(10) + 1)* 60000)), pollutantRandom,microclimateRandom,
                    latRandom, lonRandom, locEleRandom, locAccRandom);
            newReading.save();
        }
    }

    public static void seedDevice() {
        try {
            // This whole thing is just to generate a random bluetooth looking string
            String original = Calendar.getInstance().toString();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(original.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            String randomHash = sb.toString().toUpperCase();
            String BTID = randomHash.substring(0,2) + ":" + randomHash.substring(2,4) + ":" + randomHash.substring(4,6) + ":" + randomHash.substring(6,8) + ":" + randomHash.substring(8,10);
            DatabaseDevice databaseDevice = new DatabaseDevice("HC-05", BTID);
            databaseDevice.save();
        } catch (NoSuchAlgorithmException e) {

        }
    }
}
