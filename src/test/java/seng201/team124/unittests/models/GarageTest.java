package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team124.models.vehicleUtility.Garage;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static seng201.team124.models.vehicleUtility.TuningParts.Type.ENGINE;

class GarageTest {

    private Garage garage;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private TuningParts tuningPart;

    @BeforeEach
    void setUp() {
        garage = new Garage();
        vehicle1 = new Vehicle("Car A", "Description A", 120.0, 10.0, 80.0, 90.0, 15.0, 20000.0, 50.0, 60.0, "Model A", "path");
        vehicle2 = new Vehicle("Car B", "Description B", 130.0, 12.0, 85.0, 92.0, 16.0, 22000.0, 55.0, 65.0, "Model B", "path");
        tuningPart = new TuningParts("Part1", "Description", 1, 1, 1, 1, 500, ENGINE);
    }

    @Test
    @DisplayName("Add vehicle successfully when garage is not full")
    void addVehicleSuccessfully() {
        garage.addVehicle(vehicle1);

        List<Vehicle> inventory = garage.getVehiclesInventory();
        assertTrue(inventory.contains(vehicle1), "Garage should contain the added vehicle.");
        assertEquals(vehicle1, garage.getCurrentVehicle(), "The added vehicle should be set as the current vehicle.");
    }

    @Test
    @DisplayName("Add vehicle fails when garage is full")
    void addVehicleFailsWhenGarageFull() {
        for (int i = 1; i <= 5; i++) {
            garage.addVehicle(new Vehicle("Car " + i, "Description", 120.0, 10.0, 80.0, 90.0, 15.0, 20000.0, 50.0, 60.0, "Model " + i, "path"));
        }

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> garage.addVehicle(vehicle1));
        assertEquals("Garage already has 5 vehicles. Sell some to create room for more.", exception.getMessage());
    }

    @Test
    @DisplayName("Remove vehicle successfully")
    void removeVehicleSuccessfully() {
        garage.addVehicle(vehicle1);
        garage.addVehicle(vehicle2);

        garage.removeVehicle(vehicle2);

        List<Vehicle> inventory = garage.getVehiclesInventory();
        assertFalse(inventory.contains(vehicle2), "Garage should not contain the removed vehicle.");
        assertEquals(vehicle1, garage.getCurrentVehicle(), "Current vehicle should remain unaffected.");
    }

    @Test
    @DisplayName("Remove vehicle fails when it's the last vehicle")
    void removeVehicleFailsWhenLastVehicle() {
        garage.addVehicle(vehicle1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> garage.removeVehicle(vehicle1));
        assertEquals("Must have at least one vehicle. Cannot remove the last one.", exception.getMessage());
    }

    @Test
    @DisplayName("Set current vehicle successfully")
    void setCurrentVehicleSuccessfully() {
        garage.addVehicle(vehicle1);
        garage.addVehicle(vehicle2);

        garage.setCurrentVehicle(vehicle2);

        assertEquals(vehicle2, garage.getCurrentVehicle(), "Current vehicle should be updated correctly.");
    }

    @Test
    @DisplayName("Set current vehicle fails if vehicle is not in the garage")
    void setCurrentVehicleFailsIfNotInGarage() {
        garage.addVehicle(vehicle1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> garage.setCurrentVehicle(vehicle2));
        assertEquals("Garage does not contain vehicle.", exception.getMessage());
    }

    @Test
    @DisplayName("Add tuning part successfully")
    void addTuningPartSuccessfully() {
        garage.addTuningPart(tuningPart);

        List<TuningParts> inventory = garage.getTuningPartsInventory();
        assertTrue(inventory.contains(tuningPart), "Garage should contain the added tuning part.");
    }

    @Test
    @DisplayName("Remove tuning part successfully")
    void removeTuningPartSuccessfully() {
        garage.addTuningPart(tuningPart);

        garage.removeTuningPart(tuningPart);

        List<TuningParts> inventory = garage.getTuningPartsInventory();
        assertFalse(inventory.contains(tuningPart), "Garage should not contain the removed tuning part.");
    }

    @Test
    @DisplayName("Remove tuning part fails if part is not in the garage")
    void removeTuningPartFailsIfNotInGarage() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> garage.removeTuningPart(tuningPart));
        assertEquals("Garage does not contain tuning part.", exception.getMessage());
    }

    @Test
    @DisplayName("Install tuning part successfully")
    void installPartSuccessfully() {
        garage.addVehicle(vehicle1);
        garage.addTuningPart(tuningPart);

        String message = garage.installPart(vehicle1, tuningPart);

        assertEquals(String.format("Installed %s on %s.", tuningPart.getName(), vehicle1.getName()), message);
        assertFalse(garage.getTuningPartsInventory().contains(tuningPart), "Tuning part should no longer be in inventory.");
    }

    @Test
    @DisplayName("Install tuning part fails when part is not in inventory")
    void installPartFailsWhenNotInInventory() {
        garage.addVehicle(vehicle1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> garage.installPart(vehicle1, tuningPart));
        assertEquals("Tuning part not in inventory.", exception.getMessage());
    }

    @Test
    @DisplayName("Uninstall tuning part successfully")
    void uninstallPartSuccessfully() {
        garage.addVehicle(vehicle1);
        garage.addTuningPart(tuningPart);
        garage.installPart(vehicle1, tuningPart);

        String message = garage.uninstallPart(vehicle1, tuningPart);

        assertEquals(String.format("Uninstalled %s from %s.", tuningPart.getName(), vehicle1.getName()), message);
        assertTrue(garage.getTuningPartsInventory().contains(tuningPart), "Tuning part should be back in inventory.");
    }

    @Test
    @DisplayName("Uninstall tuning part fails if part is not installed on vehicle")
    void uninstallPartFailsIfNotInstalled() {
        garage.addVehicle(vehicle1);

        String message = garage.uninstallPart(vehicle1, tuningPart);

        assertEquals(String.format("%s does not have %s installed.", vehicle1.getName(), tuningPart.getName()), message);
    }
}