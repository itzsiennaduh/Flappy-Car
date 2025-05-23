package seng201.team124.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.Player;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.services.GarageService;

import static org.junit.jupiter.api.Assertions.*;

public class GarageServiceTest {

    private GarageService garageService;
    private Player player;
    private TuningParts tuningPart;
    private Vehicle vehicle;

    @BeforeEach
    public void setUp() {
        player = new Player("TestPlayer", 5000);
        tuningPart = new TuningParts(
                "Racing Engine",
                "Increases speed significantly.",
                10,
                0,
                0,
                0,
                1000,
                TuningParts.Type.ENGINE
        );
        vehicle = new Vehicle(
                "Sports Car",
                "A fast sports car.",
                250,
                50,
                80,
                85,
                9,
                20000,
                50,
                100,
                "Model X",
                "Path"
        );

        player.getTuningParts().add(tuningPart);
        player.getVehicles().add(vehicle);

        garageService = new GarageService(player);
    }

    @Test
    public void testUninstallTuningPartNotOwnedPart() {
        vehicle.getInstalledParts().add(tuningPart);
        player.getTuningParts().remove(tuningPart);

        String result = garageService.uninstallTuningPart(tuningPart, vehicle);
        assertEquals("You do not own this tuning part.", result);
    }


}