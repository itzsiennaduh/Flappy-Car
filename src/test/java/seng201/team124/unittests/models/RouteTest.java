package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.raceLogic.Route;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteTest {

    private Route route;

    /**
     * Initializes a sample Route object to be used in tests.
     */
    @BeforeEach
    public void setUp() {
        route = new Route(
                "Track",
                100.5,
                2,
                5,
                0.8,
                0.9,
                1.0,
                1.2
        );
    }

    /**
     * Tests the getDescription method.
     */
    @Test
    public void testGetDescription() {
        assertEquals("Track", route.getDescription());
    }

    /**
     * Tests the getDistance method.
     */
    @Test
    public void testGetDistance() {
        assertEquals(100.5, route.getDistance());
    }

    /**
     * Tests the getFuelStops method.
     */
    @Test
    public void testGetFuelStops() {
        assertEquals(2, route.getFuelStops());
    }

    /**
     * Tests the getDifficulty method.
     */
    @Test
    public void testGetDifficulty() {
        assertEquals(5, route.getDifficulty());
    }

    /**
     * Tests the getSpeedModifier method.
     */
    @Test
    public void testGetSpeedModifier() {
        assertEquals(0.8, route.getSpeedModifier());
    }

    /**
     * Tests the getHandlingModifier method.
     */
    @Test
    public void testGetHandlingModifier() {
        assertEquals(0.9, route.getHandlingModifier());
    }

    /**
     * Tests the getReliabilityModifier method.
     */
    @Test
    public void testGetReliabilityModifier() {
        assertEquals(1.0, route.getReliabilityModifier());
    }

    /**
     * Tests the getFuelEconomyModifier method.
     */
    @Test
    public void testGetFuelEconomyModifier() {
        assertEquals(1.2, route.getFuelEconomyModifier());
    }

    /**
     * Tests the toString method.
     */
    @Test
    public void testToString() {
        String expected = "Route Description: Track" +
                "\nDistance: 100.5 km" +
                "\nFuel Stops: 2" +
                "\nOverall Difficulty: 5" +
                "\nThis affects: " +
                "\nSpeed: 0.8" +
                "\nHandling: 0.9" +
                "\nReliability: 1.0" +
                "\nFuel Economy: 1.2";
        assertEquals(expected, route.toString());
    }
}