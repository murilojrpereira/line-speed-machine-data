package com.fp.machinedata.control.common.utils;

public class DateUtils {

    public static boolean verifyIfDataOccurredOnTheSpecificRangeOfTime(final long firstDate,
                                                                       final long secondDate,
                                                                       final long rangeOfTime) {

        long deltaOfDateTimes = secondDate - firstDate;

        if (deltaOfDateTimes < 0) {
            deltaOfDateTimes = deltaOfDateTimes * -1;
        }

        return deltaOfDateTimes <= rangeOfTime;
    }
}
