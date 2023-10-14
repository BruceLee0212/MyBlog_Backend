package com.site.blog.my.core.util;
import java.util.Random;
public class IDGenerator {
    public static int generateID(){
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        long combinedNumber = timestamp * 10000 + randomNumber;
        return Math.abs((int) (combinedNumber % 100000000));
    }
}