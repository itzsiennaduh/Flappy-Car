package seng201.team124.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.Player;
import seng201.team124.models.raceLogic.EventResult;
import seng201.team124.models.raceLogic.Race;
import seng201.team124.models.raceLogic.Route;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.services.CounterService;
import seng201.team124.services.GameManager;
import seng201.team124.services.RaceService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RaceServiceTest {

    private RaceService raceService;
    private Player player;
    private Vehicle vehicle;
    private Race race;
    private Route route;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", 1000);
        GameManager gameManager = new GameManager();
        CounterService counterService = new CounterService();
        vehicle = new Vehicle("Car", "Fast car", 200, 10, 70, 80, 5, 50000, 50, 60, "Model");
        route = new Route("Mountain Path", 100, 1, 3, 1.2, 0.8, 0.9, 0.7);
        List<Route> routes = new ArrayList<>();
        routes.add(route);
        race = new Race("Test Race", 5, routes, 10, 1000, "test.com");

        player.setCurrentVehicle(vehicle);
        raceService = new RaceService(player, gameManager, counterService);
    }

    @Test
    void testStartRaceSuccess() {
        boolean result = raceService.startRace(race, route);

        assertTrue(result);
        assertEquals(race, raceService.startRace(race, route) ? race : null);
        assertEquals(5.0, raceService.getTotalRaceHours());
    }

    @Test
    void testStartRaceFailureDueToNoVehicle() {
        player.setCurrentVehicle(null);

        boolean result = raceService.startRace(race, route);

        assertFalse(result);
    }

    @Test
    void testIncrementRaceTimeSuccess() {
        raceService.startRace(race, route);

        boolean result = raceService.incrementRaceTime(2.0);

        assertTrue(result);
    }

    @Test
    void testIncrementRaceTimeFailure() {
        raceService.startRace(race, route);

        boolean result = raceService.incrementRaceTime(6.0);

        assertFalse(result);
    }

    @Test
    void testCompleteRace() {
        raceService.startRace(race, route);
        raceService.completeRace(1);

        assertEquals(1, raceService.getCompletedRacesCount());
        assertEquals(1.0, raceService.getAveragePlacing());
        assertEquals(2000, player.getMoney());
    }

    @Test
    void testHandleRandomEventNoEvent() {
        EventResult eventResult = raceService.handleRandomEvent();

        assertNull(eventResult);
    }

    @Test
    void testHandleRefuel() {
        boolean result = raceService.handleRefuel(true);

        assertTrue(result);
        assertEquals(50, vehicle.getFuelLevel());
    }

    @Test
    void testHandleRefuelNoAction() {
        boolean result = raceService.handleRefuel(false);

        assertFalse(result);
        assertEquals(50, vehicle.getFuelLevel());
    }

    @Test
    void testCheckFuelLevelEnoughFuel() {
        boolean result = raceService.checkFuelLevel(vehicle, route);

        assertTrue(result);
    }

    @Test
    void testCheckFuelLevelNotEnoughFuel() {
        vehicle.consumeFuel(45);
        boolean result = raceService.checkFuelLevel(vehicle, route);

        assertFalse(result);
    }

    @Test
    void testGetCompletedRacesCount() {
        raceService.completeRace(1);
        raceService.completeRace(2);

        assertEquals(2, raceService.getCompletedRacesCount());
    }

    @Test
    void testGetAveragePlacing() {
        raceService.completeRace(1);
        raceService.completeRace(3);

        assertEquals(2.0, raceService.getAveragePlacing());
    }
}