package com.sachin.gogit.utils;

/**
 *  Class used for all operations related to date and time
 */
public class DateAndTimeUtils {

    public static final int CACHE_EXPIRY_IN_MINUTES = 120;
    /**
     *  Checks if the time difference is more than 2 hours
     * @param currentTimeInMillis current time
     * @param oldTimeInMillis older time
     * @return true if difference is more than 2 hours, else returns false
     */
    public static boolean isHourDifferenceGreaterThanTwo(long currentTimeInMillis, long oldTimeInMillis) {
        long diff =  currentTimeInMillis - oldTimeInMillis;
        int minutes = getNumberOfMinutes(diff);
        return minutes >= CACHE_EXPIRY_IN_MINUTES;
    }

    private static int getNumberOfDays(long timeInMills) {
        return (int) (timeInMills / (1000 * 60 * 60 * 24));
    }

    private static int getNumberOfHours(long timeInMills) {
        return (int) (timeInMills / (1000 * 60 * 60));
    }

    private static int getNumberOfMinutes(long timeInMills) {
        return (int) (timeInMills / (1000 * 60));
    }

    public static int getMinutesDifference(long currentTimeInMillis, long oldTimeInMillis) {
        return getNumberOfMinutes(currentTimeInMillis - oldTimeInMillis);
    }

}