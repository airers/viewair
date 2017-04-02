package com.chaijiaxun.pm25tracker.database;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DatabaseSeed {

    public static void seed(int num){
        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Random rn = new Random();
        //Date date = new Date();
        Random rand = new Random();
        //1.468460, 103.573813
        //1.267951, 104.079557
        for(int i = 0; i < num; i++){

            int pollutantRandom = rand.nextInt(5) + 1;
            int microclimateRandom = rand.nextInt(2) + 1;
            float latRandom = (float) ((rand.nextInt(200000)*0.000001) + 1.267951);
            float lonRandom = (float) ((rand.nextInt(500000)*0.000001) + 103.573813);
            float locEleRandom = (float) (rand.nextInt(100) - 20);
            float locAccRandom = (float) (rand.nextInt(7) + 1);
            SensorReading newReading = new SensorReading(0, new Date(t + ((rn.nextInt(10) + 1)* 60000)), pollutantRandom,microclimateRandom,
                    latRandom, lonRandom, locEleRandom, locAccRandom);
            newReading.save();
        }
    }
}
