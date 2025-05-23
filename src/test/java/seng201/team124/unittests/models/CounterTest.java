package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.raceLogic.Counter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CounterTest {

    private Counter counter;

    @BeforeEach
    void setUp() {
        counter = new Counter(0); // Initialize counter with default time for each test
    }

    @Test
    void getFormattedTimeReturnsCorrectFormatForWholeHours() {
        counter.addHours(5);
        String result = counter.getFormattedTime();
        assertEquals("5:00", result);
    }

    @Test
    void getFormattedTimeReturnsCorrectFormatForHoursAndMinutes() {
        counter.addHours(5.5);
        String result = counter.getFormattedTime();
        assertEquals("5:30", result);
    }

    @Test
    void getFormattedTimeReturnsCorrectFormatForZeroHours() {
        String result = counter.getFormattedTime();
        assertEquals("0:00", result);
    }

    @Test
    void getFormattedTimeHandlesFractionalMinutesCorrectly() {
        counter.addHours(3.75);
        String result = counter.getFormattedTime();
        assertEquals("3:45", result);
    }

    @Test
    void getFormattedTimeHandlesTimeJustUnderNextHour() {
        counter.addHours(1.999);
        String result = counter.getFormattedTime();
        assertEquals("1:59", result);
    }

    @Test
    void addHoursIncreasesTimeCorrectly() {
        counter.addHours(3.5);
        assertEquals(3.5, counter.getCurrentTime());
    }

    @Test
    void subtractHoursDecreasesTimeCorrectly() {
        counter.subtractHours(2.5);
        assertEquals(-2.5, counter.getCurrentTime());
    }

    @Test
    void addAndSubtractHoursWorkCorrectly() {
        counter.addHours(4);
        counter.subtractHours(1.5);
        assertEquals(2.5, counter.getCurrentTime());
    }
}