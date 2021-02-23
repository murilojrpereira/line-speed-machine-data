package com.fp.machinedata.control.dos;

import org.junit.jupiter.api.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataMinuteDOTest {

    private static DataMinuteDO dataMinuteDO = new DataMinuteDO();

    private double totalMinutesInOneHour = 60;

    private double maxSpeedValue = 500;

    private double minSpeedValue = 2;

    private double speedValueA = 200;

    private double speedValueB = 215;

    private long dateA = 1613906585014L;

    private long dateB = 1613906585024L;


    @Test
    @Order(1)
    void processNewDataFromMachineWillAddSpeedOnAvgMaxAndMinBecauseIsTheFirstValueAdded() {
        // Given
        int minuteFromTimeStamp = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(dateA), ZoneOffset.UTC).getMinute();

        // When
        dataMinuteDO.processNewDataFromMachine(speedValueA, dateA, 11, minuteFromTimeStamp);

        // Then
        assertEquals(dataMinuteDO.getAvg().get(minuteFromTimeStamp).getSpeed(), speedValueA);
        assertEquals(dataMinuteDO.getMaxSpeed().get(minuteFromTimeStamp).getSpeed(), speedValueA);
        assertEquals(dataMinuteDO.getMinSpeed().get(minuteFromTimeStamp).getSpeed(), speedValueA);

        assertEquals(dataMinuteDO.getAvg().size(), totalMinutesInOneHour);
        assertEquals(dataMinuteDO.getMaxSpeed().size(), totalMinutesInOneHour);
        assertEquals(dataMinuteDO.getMinSpeed().size(), totalMinutesInOneHour);
    }

    @Test
    @Order(2)
    void processNewDataFromMachineWillAddSpeedOnTheAvgInfoAndMinBecauseMinSpeedValueWasAdded() {
        // Given
        int minuteFromTimeStamp = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(dateB), ZoneOffset.UTC).getMinute();
        final double average = (speedValueA + minSpeedValue) / 2;

        // When
        dataMinuteDO.processNewDataFromMachine(minSpeedValue, dateB, 11, minuteFromTimeStamp);

        // Then
        assertEquals(dataMinuteDO.getAvg().get(minuteFromTimeStamp).getSpeed(), average);
        assertEquals(dataMinuteDO.getMaxSpeed().get(minuteFromTimeStamp).getSpeed(), speedValueA);
        assertEquals(dataMinuteDO.getMinSpeed().get(minuteFromTimeStamp).getSpeed(), minSpeedValue);

        assertEquals(dataMinuteDO.getAvg().size(), totalMinutesInOneHour);
        assertEquals(dataMinuteDO.getMaxSpeed().size(), totalMinutesInOneHour);
        assertEquals(dataMinuteDO.getMinSpeed().size(), totalMinutesInOneHour);
    }

    @Test
    @Order(3)
    void processNewDataFromMachineWillAddSpeedOnTheAvgInfoAndMaxBecauseMaxSpeedValueWasAdded() {
        // Given
        int minuteFromTimeStamp = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(dateA), ZoneOffset.UTC).getMinute();
        final double average = (speedValueA + minSpeedValue + maxSpeedValue) / 3;

        // When
        dataMinuteDO.processNewDataFromMachine(maxSpeedValue, dateA, 11, minuteFromTimeStamp);

        // Then
        assertEquals(dataMinuteDO.getAvg().get(minuteFromTimeStamp).getSpeed(), average);
        assertEquals(dataMinuteDO.getMaxSpeed().get(minuteFromTimeStamp).getSpeed(), maxSpeedValue);
        assertEquals(dataMinuteDO.getMinSpeed().get(minuteFromTimeStamp).getSpeed(), minSpeedValue);

        assertEquals(dataMinuteDO.getAvg().size(), totalMinutesInOneHour);
        assertEquals(dataMinuteDO.getMaxSpeed().size(), totalMinutesInOneHour);
        assertEquals(dataMinuteDO.getMinSpeed().size(), totalMinutesInOneHour);
    }

    @Test
    @Order(4)
    void processNewDataFromMachineWillAddSpeedOnTheAvgInfoBecauseSpeedValueBWasAdded() {
        int minuteFromTimeStamp = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(dateA), ZoneOffset.UTC).getMinute();
        final double average = (speedValueA + minSpeedValue + maxSpeedValue + speedValueB) / 4;

        dataMinuteDO.processNewDataFromMachine(speedValueB, dateB, 11, minuteFromTimeStamp);

        assertEquals(dataMinuteDO.getAvg().get(minuteFromTimeStamp).getSpeed(), average);
        assertEquals(dataMinuteDO.getMaxSpeed().get(minuteFromTimeStamp).getSpeed(), maxSpeedValue);
        assertEquals(dataMinuteDO.getMinSpeed().get(minuteFromTimeStamp).getSpeed(), minSpeedValue);

        assertEquals(dataMinuteDO.getAvg().size(), totalMinutesInOneHour);
        assertEquals(dataMinuteDO.getMaxSpeed().size(), totalMinutesInOneHour);
        assertEquals(dataMinuteDO.getMinSpeed().size(), totalMinutesInOneHour);
    }

}
