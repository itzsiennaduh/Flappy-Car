package seng201.team124.models.vehicleUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * the in-game shop with vehicles and parts for sale.
 */
public class Shop {
    private final List<Vehicle> availableVehicles;
    private final List<TuningParts> availableParts;
    private final Random random;

    /**
     * creates a new shop with empty inventory. constructor.
     */
    public Shop() {
        this.availableVehicles = new ArrayList<>();
        this.availableParts = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * gets currently available vehicles for purchase.
     * @return list of vehicles
     */
    public List<Vehicle> getAvailableVehicles() {
        return new ArrayList<>(availableVehicles);
    }

    /**
     * gets currently available tuning parts for purchase.
     * @return list of tuning parts
     */
    public List<TuningParts> getAvailableParts() {
        return new ArrayList<>(availableParts);
    }

    /**
     * purchase a vehicle from the shop.
     * @param vehicle vehicle to purchase
     * @return the purchased vehicle
     * @throws IllegalArgumentException if the vehicle is not available for purchase
     */
    public Vehicle purchaseVehicle(Vehicle vehicle) {
        if (!availableVehicles.contains(vehicle)) {
//            throw new IllegalArgumentException("Vehicle not available for purchase.");
            System.out.println("Vehicle not available for purchase.");
        }
        availableVehicles.remove(vehicle);
        return vehicle;
    }

    /**
     * purchase a tuning part from the shop.
     * @param part tuning part to purchase
     * @return the purchased tuning part
     * @throws IllegalArgumentException if the tuning part is not available for purchase
     */
    public TuningParts purchasePart(TuningParts part) {
        if (!availableParts.contains(part)) {
//            throw new IllegalArgumentException("Tuning part not available for purchase.");
            System.out.println("this isn't an error ");
        }
        availableParts.remove(part);
        return part;
    }

    /**
     * restocks the shop with random vehicles and parts.
     * @param possibleVehicles all possible vehicles
     * @param possibleParts all possible tuning parts
     * @param vehicleCount number of vehicles to restock (3-5)
     * @param partCount number of tuning parts to restock (at least 3)
     */
    public void restock(List<Vehicle> possibleVehicles, List<TuningParts> possibleParts, int vehicleCount, int partCount) {
        availableVehicles.clear();
        availableParts.clear();

        List<Vehicle> vehiclePool = new ArrayList<>(possibleVehicles);
        for (int i = 0; i < Math.min(vehicleCount, vehiclePool.size()); i++) {
            int index = random.nextInt(vehiclePool.size());
            availableVehicles.add(vehiclePool.remove(index));
        }

        List<TuningParts> partPool = new ArrayList<>(possibleParts);
        for (int i = 0; i < Math.min(partCount, partPool.size()); i++) {
            int index = random.nextInt(partPool.size());
            availableParts.add(partPool.remove(index));
        }
    }

    public void clearInventory() {
        this.availableVehicles.clear();
        this.availableParts.clear();
    }

    /**
     * adds a single vehicle to the shop inventory
     * @param vehicle vehicle to add
     */
    public void addVehicle(Vehicle vehicle) {
        if (!this.availableVehicles.contains(vehicle)) {
            this.availableVehicles.add(vehicle);
        }
    }

    /**
     * adds a single tuning part to the shop inventory
     * @param part tuning part to add
     */
    public void addPart(TuningParts part) {
        if (!this.availableParts.contains(part)) {
            this.availableParts.add(part);
        }
    }

    /**
     * adds multiple vehicles to the shop inventory
     * @param vehicles list of vehicles to add
     */
    public void addVehicles(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            if (!this.availableVehicles.contains(vehicle)) {
                this.availableVehicles.add(vehicle);
            }
        }
    }

    /**
     * adds multiple tuning parts to the shop inventory
     * @param parts list of tuning parts to add
     */
    public void addParts(List<TuningParts> parts) {
        for (TuningParts part : parts) {
            if (!this.availableParts.contains(part)) {
                this.availableParts.add(part);
            }
        }
    }
}
