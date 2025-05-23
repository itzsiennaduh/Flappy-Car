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
    private TuningParts testTuningPart;

    @BeforeEach
    public void setUp() {
        testTuningPart = TuningPartFactory.createNitrousOxide();
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







}
