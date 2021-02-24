package com.fp.machinedata.control.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConvertDoubleValuesTest {

    @Test
    void formatDoubleToTheRightPattern() {
        // Given
        final double testData = 33.1546;
        final double target = 33.15;

        // When
        final double result = ConvertDoubleValues.formatDoubleTo(testData);

        // Then
        assertEquals(result, target);
    }

    @Test
    void formatDoubleToTheRightPatternAdjustingToTheNextValue() {
        // Given
        final double testData = 33.1566;
        final double target = 33.16;

        // When
        final double result = ConvertDoubleValues.formatDoubleTo(testData);

        // Then
        assertEquals(result, target);
    }
}
