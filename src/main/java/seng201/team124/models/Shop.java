package seng201.team124.models;

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
            throw new IllegalArgumentException("Vehicle not available for purchase.");
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
            throw new IllegalArgumentException("Tuning part not available for purchase.");
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
    //List<Car> shopCars = new ArrayList<>();
    //shopCars.add(VehicleFactory.createRedVehicle());
    //shopCars.add(VehicleFactory.createCatVehicle());
    //shopCars.add(VehicleFactory.createHorseChariot());

    //List<TuningPart> shopParts = new ArrayList<>();
    //shopParts.add(TuningPartFactory.createTurbocharger());
    //shopParts.add(TuningPartFactory.createOffRoadTires());
    //shopParts.add(TuningPartFactory.createSupercharger());
    //shopParts.add(TuningPartFactory.createNitrousOxide());
}
