package seng201.team124.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.models.Player;
import seng201.team124.models.vehicleUtility.Shop;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.services.ShopService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShopServiceTest {

    private ShopService shopService;
    private Player player;
    private Shop shop;
    private Vehicle vehicle;
    private List<Vehicle> allPossibleVehicles;
    private List<TuningParts> allPossibleParts;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle("Car1", "Fast car", 200, 10, 70, 80, 5, 50000, 50, 60, "ModelX");
        TuningParts part1 = new TuningParts("Part1", "Engine improvement", 1, 1, 1, 1, 500, TuningParts.Type.ENGINE);
        TuningParts part2 = new TuningParts("Part2", "Nitro improvement", 1, 1, 1, 1, 500, TuningParts.Type.NITRO);
        player = new Player("Test", 100000);
        shop = new Shop();
        allPossibleVehicles = List.of(vehicle, new Vehicle("Car2", "Luxury car", 300, 15, 80, 85, 6, 70000, 60, 70, "ModelY"));
        allPossibleParts = List.of(part1, part2);
        shopService = new ShopService(shop, player, allPossibleVehicles, allPossibleParts);
    }

    @Test
    void purchaseVehicleSuccess() {
        String result = shopService.purchaseVehicle(vehicle);

        assertEquals(50000, player.getMoney());
        assertTrue(player.getVehicles().contains(vehicle));
        assertEquals("Purchase successful. Car1 added to your inventory.", result);
    }

    @Test
    void purchaseVehicleNotEnoughMoney() {
        player = new Player("Test", 1000);
        String result = shopService.purchaseVehicle(vehicle);

        assertEquals("You do not have enough money to purchase this vehicle.", result);
    }

    @Test
    void purchaseVehicleAlreadyOwned() {
        player.addVehicle(vehicle);
        String result = shopService.purchaseVehicle(vehicle);

        assertEquals("You already own this vehicle.", result);
    }

    @Test
    void purchaseVehicleMaxVehiclesReached() {
        for (int i = 0; i < 5; i++) {
            player.addVehicle(new Vehicle("Car" + i, "Sample car", 200, 10, 70, 80, 5, 50000, 50, 60, "Model" + i));
        }
        String result = shopService.purchaseVehicle(vehicle);

        assertEquals("You have already purchased 5 vehicles.", result);
    }

    @Test
    void restockShop() {
        TuningParts part1 = allPossibleParts.get(0);
        TuningParts part2 = allPossibleParts.get(1);

        player.getVehicles().add(vehicle);
        player.getTuningParts().add(part1);

        shopService.restockShop();

        assertFalse(shop.getAvailableVehicles().contains(vehicle));
        assertTrue(shop.getAvailableVehicles().contains(allPossibleVehicles.get(1)));
        assertFalse(shop.getAvailableParts().contains(part1));
        assertTrue(shop.getAvailableParts().contains(part2));
    }

    @Test
    void sellVehicleSuccessfully() {
        player.addVehicle(vehicle);
        double initialMoney = player.getMoney();

        String result = shopService.sellVehicle(vehicle);

        assertFalse(player.getVehicles().contains(vehicle));
        assertEquals(initialMoney + vehicle.getSellPrice(), player.getMoney());
        assertEquals("Vehicle sold back to the shop. Car1 removed from your inventory.", result);
    }

    @Test
    void sellVehicleLastVehicle() {
        player.addVehicle(vehicle);

        String result = shopService.sellVehicle(vehicle);

        assertEquals("You cannot sell your last vehicle.", result);
    }

    @Test
    void sellPartSuccessfully() {
        TuningParts part = allPossibleParts.getFirst();
        player.addTuningPart(part);
        double initialMoney = player.getMoney();

        String result = shopService.sellPart(part);

        assertFalse(player.getTuningParts().contains(part));
        assertEquals(initialMoney + part.getSellPrice(), player.getMoney());
        assertEquals("Tuning part sold back to the shop. Part1 removed from your inventory.", result);
    }

    @Test
    void sellPartNotOwned() {
        TuningParts part = allPossibleParts.getFirst();

        String result = shopService.sellPart(part);

        assertEquals("You do not own this tuning part.", result);
    }
}