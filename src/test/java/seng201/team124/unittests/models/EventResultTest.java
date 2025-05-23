package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.Player;
import seng201.team124.models.raceLogic.Counter;
import seng201.team124.models.raceLogic.EventResult;
import seng201.team124.models.raceLogic.RaceEvent;
import seng201.team124.services.RaceService;

import static org.junit.jupiter.api.Assertions.*;

class EventResultTest {

    private Player player;
    private Counter counter;
    private RaceService raceService;

    /**
     * Sets up the necessary objects for all tests in this class.
     */
    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", 1000.0);
        counter = new Counter(0.0);
        raceService = new RaceService(player, null, null);
    }

    @Test
    void testGetEventReturnsCorrectEvent() {
        RaceEvent event = RaceEvent.SEVERE_WEATHER;
        EventResult eventResult = new EventResult(event);

        assertEquals(event, eventResult.getEvent());
    }

    @Test
    void testHandleChoice1ForBreakdownEvent() {
        EventResult eventResult = new EventResult(RaceEvent.BREAKDOWN);

        eventResult.handleChoice1(player, raceService);

        assertEquals(0.0, raceService.getCompletedRacesCount()); // Assuming it ends the race (did not increment).
    }

    @Test
    void testHandleChoice2ForBreakdownEvent() {
        EventResult eventResult = new EventResult(RaceEvent.BREAKDOWN);

        eventResult.handleChoice2(player, counter, raceService);

        assertEquals(500.0, player.getMoney(), 0.01);
        assertEquals(1.0, counter.getCurrentTime(), 0.01);
    }
}