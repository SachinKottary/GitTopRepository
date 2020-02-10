package com.sachin.gogit.utils;

/**
 *  Extention of DateAndTimeUtils class, used mainly for testing without static methods
 */
public class DateAndTimeUtilsTest {

    public static final int CACHE_EXPIRY_IN_MINUTES = 120;
    /**
     *  Checks if the time difference is more than 2 hours
     * @param currentTimeInMillis current time
     * @param oldTimeInMillis older time
     * @return true if difference is more than 2 hours, else returns false
     */
    public boolean isHourDifferenceGreaterThanTwo(long currentTimeInMillis, long oldTimeInMillis) {
        long diff =  currentTimeInMillis - oldTimeInMillis;
        int minutes = getNumberOfMinutes(diff);
        return minutes >= CACHE_EXPIRY_IN_MINUTES;
    }

    private int getNumberOfDays(long timeInMills) {
        return (int) (timeInMills / (1000 * 60 * 60 * 24));
    }

    private int getNumberOfHours(long timeInMills) {
        return (int) (timeInMills / (1000 * 60 * 60));
    }

    private int getNumberOfMinutes(long timeInMills) {
        return (int) (timeInMills / (1000 * 60));
    }

    public int getMinutesDifference(long currentTimeInMillis, long oldTimeInMillis) {
        return getNumberOfMinutes(currentTimeInMillis - oldTimeInMillis);
    }

}
