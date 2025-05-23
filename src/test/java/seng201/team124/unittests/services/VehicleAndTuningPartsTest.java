package seng201.team124.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.factories.TuningPartFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleAndTuningPartsTest {
    private Vehicle testVehicle;
    private TuningParts testTuningPart;

    @BeforeEach
    public void setUp() {
        testVehicle = VehicleFactory.createRedVehicle();
        testTuningPart = TuningPartFactory.createNitrousOxide();
    }

    @Test
    public void testInitialVehicleStats() {
        assertEquals("Ferrari", testVehicle.getName());
        assertEquals("Fast but unreliable, will you risk it?", testVehicle.getDescription());
        assertEquals(10, testVehicle.getSpeed());
        assertEquals(5, testVehicle.getHandling());
        assertEquals(2, testVehicle.getReliability());
        assertEquals(3, testVehicle.getFuelEconomy());
        assertEquals(9000, testVehicle.getCost());
        assertEquals(100, testVehicle.getFuelLevel());
        assertEquals(100, testVehicle.getMaxFuelLevel());
    }

    @Test
    public void testTuningPartCreation() {
        assertEquals("Nitrous Oxide", testTuningPart.getName());
        assertEquals("Enhances speed, diminishes fuel economy", testTuningPart.getDescription());
        assertEquals(5, testTuningPart.getSpeedModifier());
        assertEquals(0, testTuningPart.getHandlingModifier());
        assertEquals(0, testTuningPart.getReliabilityModifier());
        assertEquals(-2, testTuningPart.getFuelEconomyModifier());
        assertEquals(500, testTuningPart.getCost());
    }

    @Test
    public void testFuelMethods() {
        testVehicle.consumeFuel(30);
        assertEquals(70, testVehicle.getFuelLevel());

        testVehicle.refuel();
        assertEquals(100, testVehicle.getFuelLevel());
    }

    @Test
    public void testTuningPartInstallation() {
        testVehicle.installPart(testTuningPart);
        List<TuningParts> installedParts = testVehicle.getInstalledParts();
        assertEquals(1, installedParts.size());
        assertTrue(installedParts.contains(testTuningPart));

        assertEquals(15, testVehicle.getEffectiveSpeed());
        assertEquals(5, testVehicle.getEffectiveHandling());
        assertEquals(2, testVehicle.getEffectiveReliability());
        assertEquals(1, testVehicle.getEffectiveFuelEconomy());
        assertEquals(9500, testVehicle.getTotalCost());
    }






}
