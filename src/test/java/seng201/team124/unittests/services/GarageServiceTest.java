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
                "Model X"
        );

        player.getTuningParts().add(tuningPart);
        player.getVehicles().add(vehicle);

        garageService = new GarageService(player);
    }

    @Test
    public void testInstallTuningPartSuccess() {
        String result = garageService.installTuningPart(tuningPart, vehicle);
        assertEquals("Tuning part installed on Sports Car.", result);
    }

    @Test
    public void testInstallTuningPartNotOwnedPart() {
        player.getTuningParts().remove(tuningPart);

        String result = garageService.installTuningPart(tuningPart, vehicle);
        assertEquals("You do not own this tuning part.", result);
    }

    @Test
    public void testInstallTuningPartNotOwnedVehicle() {
        player.getVehicles().remove(vehicle);

        String result = garageService.installTuningPart(tuningPart, vehicle);
        assertEquals("You do not own this vehicle.", result);
    }

    @Test
    public void testUninstallTuningPartSuccess() {
        vehicle.getInstalledParts().add(tuningPart);

        String result = garageService.uninstallTuningPart(tuningPart, vehicle);
        assertEquals("Tuning part uninstalled from Sports Car.", result);
    }

    @Test
    public void testUninstallTuningPartNotOwnedPart() {
        vehicle.getInstalledParts().add(tuningPart);
        player.getTuningParts().remove(tuningPart);

        String result = garageService.uninstallTuningPart(tuningPart, vehicle);
        assertEquals("You do not own this tuning part.", result);
    }

    @Test
    public void testUninstallTuningPartNotOwnedVehicle() {
        vehicle.getInstalledParts().add(tuningPart);
        player.getVehicles().remove(vehicle);

        String result = garageService.uninstallTuningPart(tuningPart, vehicle);
        assertEquals("You do not own this vehicle.", result);
    }

    @Test
    public void testUninstallTuningPartNotInstalled() {
        String result = garageService.uninstallTuningPart(tuningPart, vehicle);
        assertEquals("This tuning part is not installed on this vehicle.", result);
    }

    @Test
    public void testChangeCurrentVehicleSuccess() {
        boolean result = garageService.changeCurrentVehicle(vehicle);
        assertTrue(result);
        assertEquals(vehicle, player.getCurrentVehicle());
    }

    @Test
    public void testChangeCurrentVehicleNotOwned() {
        player.getVehicles().remove(vehicle);

        boolean result = garageService.changeCurrentVehicle(vehicle);
        assertFalse(result);
    }

    @Test
    public void testGetCurrentVehicle() {
        player.setCurrentVehicle(vehicle);

        Vehicle result = garageService.getCurrentVehicle();
        assertEquals(vehicle, result);
    }
}