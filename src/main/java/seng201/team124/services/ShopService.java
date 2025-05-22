package seng201.team124.services;

import seng201.team124.models.*;
import seng201.team124.models.vehicleUtility.Shop;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * handles all shop-related operations and logic
 */

public class ShopService {
    private final Shop shop;
    private final Player player;
    private final List<Vehicle> allPossibleVehicles;
    private final List<TuningParts> allPossibleParts;

    public ShopService(Shop shop, Player player, List<Vehicle> allPossibleVehicles, List<TuningParts> allPossibleParts) {
        this.shop = shop;
        this.player = player;
        this.allPossibleVehicles = allPossibleVehicles;
        this.allPossibleParts = allPossibleParts;
    }

    /**
     * purchase a vehicle from the shop
     * @param vehicle vehicle to purchase
     * @return success message if purchase was successful, error message otherwise
     */
    public String purchaseVehicle(Vehicle vehicle) {
        if (player.getMoney() < vehicle.getCost()) {
            return "You do not have enough money to purchase this vehicle.";
        }
        if (player.getVehicles().size() >= 5) {
            return "You have already purchased 5 vehicles.";
        }
        if (player.getVehicles().contains(vehicle)) {
            return "You already own this vehicle.";
        }
        player.addVehicle(vehicle);
        player.subtractMoney(vehicle.getCost());
        Vehicle purchasedVehicle = shop.purchaseVehicle(vehicle);
        return "Purchase successful. %s added to your inventory.".formatted(purchasedVehicle.getName());
    }
    /**
     * purchase a tuning part from the shop
     * @param part tuning part to purchase
     * @return success message if purchase was successful, error message otherwise
     */
    public String purchasePart(TuningParts part) {
        if (player.getMoney() < part.getCost()) {
            return "You do not have enough money to purchase this tuning part.";
        }
        if (player.getTuningParts().contains(part)) {
            return "You already own this tuning part.";
        }
        player.subtractMoney(part.getCost());
        player.addTuningPart(part);
        TuningParts purchasedPart = shop.purchasePart(part);
        return "Purchase successful. %s added to your inventory.".formatted(purchasedPart.getName());
    }

    /**
     * restocks the shop with new items
     */
    public void restockShop() {
        // get lists
        List<Vehicle> availableVehicles = new ArrayList<>(allPossibleVehicles);
        for (Vehicle vehicle : player.getVehicles()) {
            availableVehicles.remove(vehicle);
        }
        List<TuningParts> availableParts = new ArrayList<>(allPossibleParts);
        for (TuningParts part : player.getTuningParts()) {
            availableParts.remove(part);
        }

        shop.clearInventory();
        shop.addVehicles(availableVehicles);
        shop.addParts(availableParts);
    }

    public Shop getShop() {
        return this.shop;
    }

    /**
     * sells a vehicle back to the shop
     * @param vehicle vehicle to sell
     * @return success message if sell was successful, error message otherwise
     */
    public String sellVehicle(Vehicle vehicle) {
        if (player.getVehicles().size() <= 1) {
            return "You cannot sell your last vehicle.";
        }
        if (!player.getVehicles().contains(vehicle)) {
            System.out.println(player.getVehicles());
            return "You do not own this vehicle.";
        }

        player.removeVehicle(vehicle);
        player.addMoney(vehicle.getSellPrice());
        return "Vehicle sold back to the shop. %s removed from your inventory.".formatted(vehicle.getName());
    }

    /**
     * sells a tuning part back to the shop
     * @param part tuning part to sell
     * @return success message if sell was successful, error message otherwise
     */
    public String sellPart(TuningParts part) {
        if (!player.getTuningParts().contains(part)) {
            return "You do not own this tuning part.";
        }
        player.addMoney(part.getSellPrice());
        player.removeTuningPart(part);
        return "Tuning part sold back to the shop. %s removed from your inventory.".formatted(part.getName());
    }



}
