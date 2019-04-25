package org.octabyte.zeem.Helper;

import java.util.Arrays;

public class Utils {

    // Bucket URL
    public final static String bucketURL = "https://storage.googleapis.com/octabyte-zeem/";

    /**
     * Check object contain specific property or not
     * @param object    Class object in which you need to check
     * @param fieldName Name of property that need to check
     * @return  TRUE | FALSE
     */
    public static boolean doesObjectContainField(Object object, String fieldName) {
        return Arrays.stream(object.getClass().getFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }

    // Over Loading function
    public static String formatTime(long startData){
        return formatTime(startData, false);
    }

    /**
     * Convert Milli Sec in Human readable format
     * @param startData     When post is posted
     * @return              String - How many days or Hours ago
     */
    public static String formatTime(long startData, boolean forComment){
        long different = System.currentTimeMillis() - startData;


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthInMilli = daysInMilli * 30;
        long yearInMilli = monthInMilli * 12;

        long elapsedYears = different / yearInMilli;
        different = different % yearInMilli;

        long elapsedMonth = different / monthInMilli;
        different = different % monthInMilli;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (forComment){
            if (elapsedYears != 0){
                return elapsedYears + "Y ";
            }else if (elapsedMonth != 0){
                return elapsedMonth +"M ";
            }else if (elapsedDays != 0){
                return elapsedDays +"d ";
            }else if (elapsedHours != 0){
                return elapsedHours +"h ";
            }else if (elapsedMinutes != 0){
                return elapsedMinutes +"m ";
            }else if (elapsedSeconds != 0){
                return elapsedSeconds +"s ";
            }

            return "Now ";
        }else {

            if (elapsedYears != 0) {
                return elapsedYears > 1 ? elapsedYears + " Years" : elapsedYears + " Year";
            } else if (elapsedMonth != 0) {
                return elapsedMonth > 1 ? elapsedMonth + " Months" : elapsedMonth + " Month";
            } else if (elapsedDays != 0) {
                return elapsedDays > 1 ? elapsedDays + " Days" : elapsedDays + " Day";
            } else if (elapsedHours != 0) {
                return elapsedHours > 1 ? elapsedHours + " Hours" : elapsedHours + " Hour";
            } else if (elapsedMinutes != 0) {
                return elapsedMinutes > 1 ? elapsedMinutes + " Minutes" : elapsedMinutes + " Minute";
            } else if (elapsedSeconds != 0) {
                return elapsedSeconds > 1 ? elapsedSeconds + " Seconds" : elapsedSeconds + " Second";
            }

        }

        return "Few Moments ";
    }

    /**
     * Capitalize each first char of every word
     * @param input     String input that you want to convert into capitalize form
     * @return          Capitalize form of String
     */
    public static String capitalize(String input) {

        String[] words = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i > 0 && word.length() > 0) {
                builder.append(" ");
            }

            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
        }
        return builder.toString();
    }

    /**
     * Used in geo Hash to reduce map precision
     * @param geoHash   geoHash that you want to reduce precision
     * @return          reduced geoHash 7 character long
     */
    public static String reducePrecision(String geoHash){
        return geoHash.substring(0, 7);
    }

    public static int getBadgeByStar(int starCount){
        if (starCount < 100)    return 0;       // Basic
        else if (starCount < 500) return 1;     // Brown
        else if (starCount < 1000) return 2;    // Silver
        else if (starCount < 5000) return 3;    // Gold
        else if (starCount < 10000) return 4;   // Diamond
        else if (starCount < 25000) return 5;   // Celebrity
        else if (starCount < 50000) return 6;   // Star
        else if (starCount >= 100000) return 7; // King or Queen
        else return 0;
    }

}
