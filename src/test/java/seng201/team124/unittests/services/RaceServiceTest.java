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
        vehicle = new Vehicle("Car", "Fast car", 200, 10, 70, 80, 5, 50000, 50, 60, "Model","");
        route = new Route("Mountain Path", 100, 1, 3, 1.2, 0.8, 0.9, 0.7);
        List<Route> routes = new ArrayList<>();
        routes.add(route);
        race = new Race("Test Race", 5, routes, 10, 1000, "test.com");

        player.setCurrentVehicle(vehicle);
        raceService = new RaceService(player, gameManager, counterService);
    }

    @Test
    void testStartRaceFailureDueToNoVehicle() {
        player.setCurrentVehicle(null);

        boolean result = raceService.startRace(race, route);

        assertFalse(result);
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

}