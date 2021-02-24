package com.fp.machinedata.control.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.fp.machinedata.control.common.BusinessConstants.ZERO;

public class DateUtils {

    public static boolean verifyIfDataOccurredOnTheSpecificRangeOfTime(long firstDate,
                                                                       long secondDate,
                                                                       final long rangeOfTime) {

        long deltaOfDateTimes = secondDate - firstDate;

        if (deltaOfDateTimes < ZERO) {
            deltaOfDateTimes = deltaOfDateTimes * -1;
        }

        return firstDate >= ZERO && secondDate >= ZERO && deltaOfDateTimes <= rangeOfTime;
    }
}
