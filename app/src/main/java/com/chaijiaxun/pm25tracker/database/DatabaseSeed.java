package com.chaijiaxun.pm25tracker.database;

import java.util.Date;
import java.util.Random;

public class DatabaseSeed {
    public DatabaseSeed(){

    }
    public void seed(int num){
        Date date = new Date();
        Random rand = new Random();
        //1.468460, 103.573813
        //1.267951, 104.079557
        for(int i = 0; i < num; i++){

            int pollutantRandom = rand.nextInt(5) + 1;
            int microclimateRandom = rand.nextInt(5) + 1;
            float latRandom = (float) ((rand.nextInt(200000)*0.000001) + 1.267951);
            float lonRandom = (float) ((rand.nextInt(500000)*0.000001) + 103.573813);
            int locAccRandom = 0;
            SensorReading newReading = new SensorReading(date, pollutantRandom,microclimateRandom,
                    latRandom, lonRandom, locAccRandom);
            newReading.save();
        }
    }
}
