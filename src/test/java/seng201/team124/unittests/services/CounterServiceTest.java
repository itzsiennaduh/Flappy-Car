package seng201.team124.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.services.CounterService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Counter implementation
 * @author seng201 teaching team
 */
class CounterServiceTest {
    private CounterService testCounterService;

    /**
     * Setup before each test, we create two objects, one an actual
     * instance of our CounterService class, and another a mocked version
     * that has overridden methods.
     */
    @BeforeEach
    public void setupTest() {
        // Use CounterService directly
        testCounterService = new CounterService();
        double currentRaceTime = 100;
    }

    /**
     * Validate startRace method sets raceInProgress to true.
     */
    @Test
    void testStartRaceSetsRaceInProgress() {
        testCounterService.startRace();
        assertTrue(testCounterService.isRaceInProgress(), "startRace should set raceInProgress to true.");
    }

    /**
     * Validate startRace method resets currentRaceTime to 0.
     */
    @Test
    void testStartRaceResetsCurrentRaceTime() {
        testCounterService.startRace();
        assertEquals(0, testCounterService.getCurrentRaceTime(), "startRace should reset currentRaceTime to 0.");
    }

    @Test
    void testGetFormattedElapsedTime() {
        testCounterService.modifyTime(3661); // 1 hour, 1 minute, 1 second
        assertEquals("01:01:01", testCounterService.getFormattedElapsedTime(), "getFormattedElapsedTime should format time as HH:mm:ss.");
    }

    @Test
    void testModifyTime() {
        testCounterService.modifyTime(10);
        assertEquals(10, testCounterService.getElapsedSeconds(), "modifyTime should correctly update elapsedSeconds.");
        testCounterService.modifyTime(-5);
        assertEquals(5, testCounterService.getElapsedSeconds(), "modifyTime should not allow elapsedSeconds to go below 0.");
    }

    @Test
    void testIncrementRaceTime() {
        testCounterService.startRace();
        testCounterService.incrementRaceTime(120); // 2 minutes
        assertEquals(120, testCounterService.getCurrentRaceTime(), "incrementRaceTime should update currentRaceTime during a race.");
        assertEquals(120, testCounterService.getElapsedSeconds(), "incrementRaceTime should also update elapsedSeconds.");
    }

    @Test
    void testHasRaceTimeExpired() {
        testCounterService.setRaceTimeLimit(2); // 2 hours
        testCounterService.startRace();
        testCounterService.incrementRaceTime(7200); // 2 hours
        assertTrue(testCounterService.hasRaceTimeExpired(), "hasRaceTimeExpired should return true when race time exceeds the limit.");
        testCounterService.incrementRaceTime(1); // additional second
        assertTrue(testCounterService.hasRaceTimeExpired(), "hasRaceTimeExpired should remain true after race time exceeds the limit.");
    }

    @Test
    void testStopRace() {
        testCounterService.startRace();
        testCounterService.stopRace();
        assertFalse(testCounterService.isRaceInProgress(), "stopRace should set raceInProgress to false.");
    }

}
