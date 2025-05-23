package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team124.models.vehicleUtility.Shop;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for the Shop class")
public class ShopTest {

    private Shop shop;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private TuningParts part1;
    private TuningParts part2;

    @BeforeEach
    public void setUp() {
        shop = new Shop();
        vehicle1 = new Vehicle("Car A", "A fast car", 200.0, 7.5, 8.5, 9.0, 15.0, 15000.0, 50.0, 50.0, "ModelA");
        vehicle2 = new Vehicle("Car B", "A reliable car", 180.0, 6.0, 7.0, 8.5, 12.0, 12000.0, 45.0, 45.0, "ModelB");
        part1 = new TuningParts("Turbo", "Increases acceleration", 1, 1, 1, 1, 3000, TuningParts.Type.ENGINE);
        part2 = new TuningParts("Nitro", "Boosts speed", 1, 1, 1, 1, 2000, TuningParts.Type.NITRO);
    }

    @Test
    @DisplayName("Test getAvailableVehicles returns empty list initially")
    public void testGetAvailableVehiclesInitially() {
        assertTrue(shop.getAvailableVehicles().isEmpty(), "Available vehicles should be empty initially.");
    }

    @Test
    @DisplayName("Test getAvailableParts returns empty list initially")
    public void testGetAvailablePartsInitially() {
        assertTrue(shop.getAvailableParts().isEmpty(), "Available parts should be empty initially.");
    }

    @Test
    @DisplayName("Test addVehicle correctly adds a vehicle")
    public void testAddVehicle() {
        shop.addVehicle(vehicle1);
        List<Vehicle> vehicles = shop.getAvailableVehicles();
        assertEquals(1, vehicles.size(), "Available vehicles should contain one vehicle after adding.");
        assertTrue(vehicles.contains(vehicle1), "Added vehicle should be present in the available vehicles.");
    }

    @Test
    @DisplayName("Test addPart correctly adds a part")
    public void testAddPart() {
        shop.addPart(part1);
        List<TuningParts> parts = shop.getAvailableParts();
        assertEquals(1, parts.size(), "Available parts should contain one part after adding.");
        assertTrue(parts.contains(part1), "Added part should be present in the available parts.");
    }

    @Test
    @DisplayName("Test addVehicles adds multiple vehicles")
    public void testAddVehicles() {
        shop.addVehicles(List.of(vehicle1, vehicle2));
        List<Vehicle> vehicles = shop.getAvailableVehicles();
        assertEquals(2, vehicles.size(), "Available vehicles should contain two vehicles after adding.");
        assertTrue(vehicles.contains(vehicle1), "First added vehicle should be present in the available vehicles.");
        assertTrue(vehicles.contains(vehicle2), "Second added vehicle should be present in the available vehicles.");
    }

    @Test
    @DisplayName("Test addParts adds multiple parts")
    public void testAddParts() {
        shop.addParts(List.of(part1, part2));
        List<TuningParts> parts = shop.getAvailableParts();
        assertEquals(2, parts.size(), "Available parts should contain two parts after adding.");
        assertTrue(parts.contains(part1), "First added part should be present in the available parts.");
        assertTrue(parts.contains(part2), "Second added part should be present in the available parts.");
    }

    @Test
    @DisplayName("Test purchaseVehicle removes vehicle from available list")
    public void testPurchaseVehicle() {
        shop.addVehicle(vehicle1);
        shop.purchaseVehicle(vehicle1);
        List<Vehicle> vehicles = shop.getAvailableVehicles();
        assertTrue(vehicles.isEmpty(), "Available vehicles should be empty after purchasing the vehicle.");
    }

    @Test
    @DisplayName("Test purchasePart removes part from available list")
    public void testPurchasePart() {
        shop.addPart(part1);
        shop.purchasePart(part1);
        List<TuningParts> parts = shop.getAvailableParts();
        assertTrue(parts.isEmpty(), "Available parts should be empty after purchasing the part.");
    }

    @Test
    @DisplayName("Test restock populates available vehicles and parts")
    public void testRestock() {
        List<Vehicle> possibleVehicles = List.of(vehicle1, vehicle2);
        List<TuningParts> possibleParts = List.of(part1, part2);

        shop.restock(possibleVehicles, possibleParts, 2, 2);

        List<Vehicle> vehicles = shop.getAvailableVehicles();
        List<TuningParts> parts = shop.getAvailableParts();

        assertEquals(2, vehicles.size(), "Available vehicles should contain two vehicles after restocking.");
        assertTrue(vehicles.contains(vehicle1), "Restocked vehicles should contain the first vehicle.");
        assertTrue(vehicles.contains(vehicle2), "Restocked vehicles should contain the second vehicle.");

        assertEquals(2, parts.size(), "Available parts should contain two parts after restocking.");
        assertTrue(parts.contains(part1), "Restocked parts should contain the first part.");
        assertTrue(parts.contains(part2), "Restocked parts should contain the second part.");
    }

    @Test
    @DisplayName("Test clearInventory removes all vehicles and parts")
    public void testClearInventory() {
        shop.addVehicle(vehicle1);
        shop.addPart(part1);

        shop.clearInventory();

        List<Vehicle> vehicles = shop.getAvailableVehicles();
        List<TuningParts> parts = shop.getAvailableParts();

        assertTrue(vehicles.isEmpty(), "Available vehicles should be empty after clearing inventory.");
        assertTrue(parts.isEmpty(), "Available parts should be empty after clearing inventory.");
    }

    @Test
    @DisplayName("Test getAvailableVehicles returns a copy of the internal list")
    public void testGetAvailableVehiclesReturnsCopy() {
        shop.addVehicle(vehicle1);
        List<Vehicle> vehicles = shop.getAvailableVehicles();
        vehicles.clear();

        assertEquals(1, shop.getAvailableVehicles().size(), "Clearing the returned list should not affect the internal list.");
    }

    @Test
    @DisplayName("Test getAvailableParts returns a copy of the internal list")
    public void testGetAvailablePartsReturnsCopy() {
        shop.addPart(part1);
        List<TuningParts> parts = shop.getAvailableParts();
        parts.clear();

        assertEquals(1, shop.getAvailableParts().size(), "Clearing the returned list should not affect the internal list.");
    }
}