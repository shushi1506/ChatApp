package com.example.shushi.supports;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shushi on 4/24/2017.
 */

public final class SupportString {
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }
    public static String DecodeStringNumber(String string) {
        return string.replace("+84", "0");
    }
    public static String addSuffixeString(String string) {
        return string+"@halo.com";
    }
    public static String subSuffixeString(String string) {
        return string.replace("@halo.com","").trim();
    }
    public static String addSufAll(String string){
        return string+"@halo,com";
    }
    public static String CreatePassword(int length)
    {
        String valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder res = new StringBuilder();
        Random rnd = new Random();
        while (res.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * valid.length());
            res.append(valid.charAt(index));
        }
        String saltStr = res.toString();
        return saltStr;

    }
    public static  String millisecondsToString(int milliseconds){
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) milliseconds) ;
        return minutes+":"+ seconds;
    }
}
