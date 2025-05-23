package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.raceLogic.Race;
import seng201.team124.models.raceLogic.Route;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RaceTest {

    private Race race;
    private List<Route> routes;

    @BeforeEach
    void setUp() {
        routes = new ArrayList<>();
        routes.add(new Route("Route1", 3, 2, 1, 1, 1, 1, 1));
        routes.add(new Route("Route2", 5, 2, 1, 0.5, 0.25, 1, 1));
        race = new Race("Race1", 12.5, routes, 10, 1000.0, "http://example.com");
    }

    @Test
    void testGetName() {
        String result = race.getName();
        assertEquals("Race1", result);
    }

    @Test
    void testGetHours() {
        double result = race.getHours();
        assertEquals(12.5, result);
    }

    @Test
    void testGetRouteByIndex() {
        Route result = race.getRoute(0);
        assertEquals(routes.get(0), result);
        result = race.getRoute(1);
        assertEquals(routes.get(1), result);
    }

    @Test
    void testGetRouteThrowsExceptionForInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> race.getRoute(2));
    }

    @Test
    void testGetNumberOfRoutes() {
        int result = race.getNumberOfRoutes();
        assertEquals(2, result);
    }

    @Test
    void testGetEntries() {
        int result = race.getEntries();
        assertEquals(10, result);
    }

    @Test
    void testGetPrizeMoney() {
        double result = race.getPrizeMoney();
        assertEquals(1000.0, result);
    }

    @Test
    void testGetRaceURL() {
        String result = race.getRaceURL();
        assertEquals("http://example.com", result);
    }

    @Test
    void testApplyTimePenalty() {
        double result = race.applyTimePenalty(2.5);
        assertEquals(10.0, result);
    }

    @Test
    void testToString() {
        String result = race.toString();
        String expected = "Race: Race1\n" +
                "Duration: 12.5 hours\n" +
                "This race has: 2 number of routes.\n" +
                "Routes: " + routes + "\n" +
                "Competitors: 10\n" +
                "First Place Prize: $1000.0";
        assertEquals(expected, result);
    }
}