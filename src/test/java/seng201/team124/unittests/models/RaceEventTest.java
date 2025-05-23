package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.raceLogic.RaceEvent;

import static org.junit.jupiter.api.Assertions.*;

class RaceEventTest {

    private RaceEvent breakdownEvent;
    private RaceEvent strandedTravellerEvent;
    private RaceEvent severeWeatherEvent;

    /**
     * Initializes all RaceEvent enum values before each test to reduce repetitive code.
     */
    @BeforeEach
    void setUp() {
        breakdownEvent = RaceEvent.BREAKDOWN;
        strandedTravellerEvent = RaceEvent.STRANDED_TRAVELLER;
        severeWeatherEvent = RaceEvent.SEVERE_WEATHER;
    }

    @Test
    void testBreakdownEventProperties() {
        assertEquals("Your vehicle has broken down. You may choose whether to retire from the race or pay a repair fee and lose some racing time.", breakdownEvent.getDescription());
        assertEquals(0, breakdownEvent.getMoneyChange());
        assertEquals(0, breakdownEvent.getTimePenalty());
        assertTrue(breakdownEvent.isPleaseChoose());
        assertEquals("Retire", breakdownEvent.getChoice1());
        assertEquals("Repair (-1 hour, -500 dollars)", breakdownEvent.getChoice2());
    }

    @Test
    void testStrandedTravellerEventProperties() {
        assertEquals("You happen upon a wandering traveller. He looks like he may need a ride. You pull over and pick him up because you're a good person. He rewards you but you lose some race time)", strandedTravellerEvent.getDescription());
        assertEquals(1000, strandedTravellerEvent.getMoneyChange());
        assertEquals(1.0, strandedTravellerEvent.getTimePenalty());
        assertFalse(strandedTravellerEvent.isPleaseChoose());
        assertNull(strandedTravellerEvent.getChoice1());
        assertNull(strandedTravellerEvent.getChoice2());
    }

    @Test
    void testSevereWeatherEventProperties() {
        assertEquals("A storm advances from seemingly no where. You are forced to ditch the vehicle and take shelter. You must retire from the race.", severeWeatherEvent.getDescription());
        assertEquals(0, severeWeatherEvent.getMoneyChange());
        assertEquals(0, severeWeatherEvent.getTimePenalty());
        assertFalse(severeWeatherEvent.isPleaseChoose());
        assertNull(severeWeatherEvent.getChoice1());
        assertNull(severeWeatherEvent.getChoice2());
    }

    @Test
    void testGetRandomEventReturnsNotNull() {
        RaceEvent randomEvent = RaceEvent.getRandomEvent();
        assertNotNull(randomEvent);
    }

    @Test
    void testGetRandomEventReturnsValidEnumValue() {
        for (int i = 0; i < 100; i++) { // Run multiple times to ensure randomness doesn't fail the test
            RaceEvent randomEvent = RaceEvent.getRandomEvent();
            assertTrue(randomEvent == RaceEvent.BREAKDOWN || randomEvent == RaceEvent.STRANDED_TRAVELLER || randomEvent == RaceEvent.SEVERE_WEATHER);
        }
    }

    @Test
    void testGetRepairCost() {
        assertEquals(-500, RaceEvent.getRepairCost());
    }

    @Test
    void testGetTimeCostBreakdown() {
        assertEquals(-1, breakdownEvent.getTimeCostBreakdown());
    }
}