package com.fp.machinedata.control.common.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.fp.machinedata.control.common.BusinessConstants.ONE_MINUTE_MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void verifyIfDataOccurredOnTheSpecificRangeOfTimeWillReturnTrueBecauseTheDatesArePartOfTheSameMinute() {
        // Given
        final long firstDate = 1613906585014L;
        final long secondDate = 1613906585914L;
        // When
        final boolean isOnTheSameRange = DateUtils.verifyIfDataOccurredOnTheSpecificRangeOfTime(firstDate, secondDate, ONE_MINUTE_MILLISECONDS);
        // Then
        assertTrue(isOnTheSameRange);
    }

    @Test
    void verifyIfDataOccurredOnTheSpecificRangeOfTimeWillReturnFalseBecauseTheDatesAreNotPartOfTheSameMinute() {
        // Given
        final long firstDate = 1613906585014L;
        final long secondDate = 1513906585914L;
        // When
        final boolean isOnTheSameRange = DateUtils.verifyIfDataOccurredOnTheSpecificRangeOfTime(firstDate, secondDate, ONE_MINUTE_MILLISECONDS);
        // Then
        assertFalse(isOnTheSameRange);
    }

    @Test
    void verifyIfDataOccurredOnTheSpecificRangeOfTimeWillReturnFalseBecauseTheDatesAreNotPartOfTheSameMinuteTestWithNegativeValue() {
        //Given
        final long firstDate = -1;
        final long secondDate = 60000;
        // When
        final boolean isOnTheSameRange = DateUtils.verifyIfDataOccurredOnTheSpecificRangeOfTime(firstDate, secondDate, ONE_MINUTE_MILLISECONDS);
        //Then
        assertFalse(isOnTheSameRange);
    }

}
