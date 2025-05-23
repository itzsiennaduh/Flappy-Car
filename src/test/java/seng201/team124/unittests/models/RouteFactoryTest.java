package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team124.factories.RouteFactory;
import seng201.team124.models.raceLogic.Route;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Tests for RouteFactory class")
public class RouteFactoryTest {

    private List<Route> routes;

    @BeforeEach
    public void setUp() {
        routes = RouteFactory.getRoute();
    }

    @Test
    @DisplayName("Test if getRoute method returns exactly three routes")
    public void testGetRouteReturnsThreeRoutes() {
        assertNotNull(routes, "Routes list should not be null");
        assertEquals(3, routes.size(), "Routes list should contain exactly 3 routes");
    }

    @Test
    @DisplayName("Test route descriptions from getRoute method")
    public void testGetRouteDescriptions() {
        assertEquals("This is a flat road route", routes.get(0).getDescription(), "First route description should match");
        assertEquals("This is a flat off road mud route", routes.get(1).getDescription(), "Second route description should match");
        assertEquals("This is a flat off road ice route", routes.get(2).getDescription(), "Third route description should match");
    }

    @Test
    @DisplayName("Test attributes of road route")
    public void testRoadRouteAttributes() {
        Route roadRoute = routes.getFirst();

        assertEquals("This is a flat road route", roadRoute.getDescription(), "Road route description should match");
        assertEquals(8, roadRoute.getDistance(), "Road route distance should match");
        assertEquals(2, roadRoute.getFuelStops(), "Road route fuel stops should match");
        assertEquals(1, roadRoute.getDifficulty(), "Road route difficulty should match");
        assertEquals(1, roadRoute.getSpeedModifier(), "Road route speed modifier should match");
        assertEquals(1, roadRoute.getHandlingModifier(), "Road route handling modifier should match");
        assertEquals(1, roadRoute.getReliabilityModifier(), "Road route reliability modifier should match");
        assertEquals(1, roadRoute.getFuelEconomyModifier(), "Road route fuel economy modifier should match");
    }

    @Test
    @DisplayName("Test attributes of mud route")
    public void testMudRouteAttributes() {
        Route mudRoute = routes.get(1);

        assertEquals("This is a flat off road mud route", mudRoute.getDescription(), "Mud route description should match");
        assertEquals(8, mudRoute.getDistance(), "Mud route distance should match");
        assertEquals(2, mudRoute.getFuelStops(), "Mud route fuel stops should match");
        assertEquals(3, mudRoute.getDifficulty(), "Mud route difficulty should match");
        assertEquals(0.75, mudRoute.getSpeedModifier(), "Mud route speed modifier should match");
        assertEquals(0.5, mudRoute.getHandlingModifier(), "Mud route handling modifier should match");
        assertEquals(0.25, mudRoute.getReliabilityModifier(), "Mud route reliability modifier should match");
        assertEquals(0.25, mudRoute.getFuelEconomyModifier(), "Mud route fuel economy modifier should match");
    }

    @Test
    @DisplayName("Test attributes of ice route")
    public void testIceRouteAttributes() {
        Route iceRoute = routes.get(2);

        assertEquals("This is a flat off road ice route", iceRoute.getDescription(), "Ice route description should match");
        assertEquals(8, iceRoute.getDistance(), "Ice route distance should match");
        assertEquals(2, iceRoute.getFuelStops(), "Ice route fuel stops should match");
        assertEquals(5, iceRoute.getDifficulty(), "Ice route difficulty should match");
        assertEquals(3, iceRoute.getSpeedModifier(), "Ice route speed modifier should match");
        assertEquals(0.1, iceRoute.getHandlingModifier(), "Ice route handling modifier should match");
        assertEquals(0.25, iceRoute.getReliabilityModifier(), "Ice route reliability modifier should match");
        assertEquals(1, iceRoute.getFuelEconomyModifier(), "Ice route fuel economy modifier should match");
    }
}