package seng201.team124.models;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * player's garage containing their vehicles and tuning parts.
 */
public class Garage {
    private final List<Vehicle> vehiclesInventory;
    private final List<TuningParts> tuningPartsInventory;
    private Vehicle currentVehicle;

    /**
     * constructor for the garage class.
     */
    public Garage() {
        this.vehiclesInventory = new ArrayList<>();
        this.tuningPartsInventory = new ArrayList<>();
        this.currentVehicle = null;
    }

    /**
     * gets all vehicles in the garage.
     * @return list of vehicles
     */
    public List<Vehicle> getVehiclesInventory() {
        return new ArrayList<>(vehiclesInventory);
    }

    /**
     * gets all tuning parts in the garage.
     * @return list of tuning parts
     */
    public List<TuningParts> getTuningPartsInventory() {
        return new ArrayList<>(tuningPartsInventory);
    }

    /**
     * gets the currently selected vehicle in the garage.
     * @return current vehicle
     */
    public Vehicle getCurrentVehicle() {
        return this.currentVehicle;
    }

    /**
     * adds a vehicle to the garage.
     * the garage must have 5 vehicles or fewer.
     * @param vehicle vehicle to add
     * @throws IllegalStateException if the garage already has 5 vehicles
     */
    public void addVehicle(Vehicle vehicle) {
        if (vehiclesInventory.size() >= 5) {
            throw new IllegalStateException("Garage already has 5 vehicles. Sell some to create room for more.");
        }
        vehiclesInventory.add(vehicle);
        if (currentVehicle == null) {
            currentVehicle = vehicle;
        }
    }

    /**
     * removes a vehicle from the garage.
     *
     * @param vehicle vehicle to remove
     * @throws IllegalStateException if the garage does not contain the vehicle or trying to remove the last vehicle
     */
    public void removeVehicle(Vehicle vehicle) {
        if (vehiclesInventory.size() <= 1 || !vehiclesInventory.contains(vehicle)) {
            throw new IllegalStateException("Must have at least one vehicle. Cannot remove the last one.");
        }
        vehiclesInventory.remove(vehicle);
        if (currentVehicle.equals(vehicle)) {
            currentVehicle = vehiclesInventory.get(0); //select the first remaining vehicle
        }
    }

    /**
     * changes the currently selected vehicle in the garage.
     * @param vehicle vehicle to select
     * @throws IllegalStateException if the garage does not contain the vehicle
     */
    public void setCurrentVehicle(Vehicle vehicle) {
        if (!vehiclesInventory.contains(vehicle)) {
            throw new IllegalStateException("Garage does not contain vehicle.");
        }
        currentVehicle = vehicle;
    }

    /**
     * adds a tuning part to inventory/garage.
     * @param part tuning part to add
     */
    public void addTuningPart(TuningParts part) {
        tuningPartsInventory.add(part);
    }

    /**
     * removes a tuning part from inventory/garage.
     * @param part tuning part to remove
     * @throws IllegalStateException if the garage does not contain the tuning part
     */
    public void removeTuningPart(TuningParts part) {
        if (!tuningPartsInventory.contains(part)) {
            throw new IllegalStateException("Garage does not contain tuning part.");
        }
        tuningPartsInventory.remove(part);
    }

    /**
     * installs a tuning part from inventory/garage on the current vehicle.
     * @param vehicle vehicle to install the tuning part on
     * @param part tuning part to install
     * @return success message
     * @throws IllegalArgumentException if part is not in inventory
     */
    public String installPart(Vehicle vehicle, TuningParts part) {
        if (!tuningPartsInventory.contains(part)) {
            throw new IllegalArgumentException("Tuning part not in inventory.");
        }
        tuningPartsInventory.remove(part);
        vehicle.installPart(part);
        return String.format("Installed %s on %s.", part.getName(), vehicle.getName());
    }

    /**
     * uninstalls a tuning part from a vehicle and puts it back into inventory.
     * @param vehicle vehicle to uninstall the tuning part from
     * @param part tuning part to uninstall
     * @return success message
     */
    public String uninstallPart(Vehicle vehicle, TuningParts part) {
        if (!vehicle.getInstalledParts().contains(part)) {
            return String.format("%s does not have %s installed.", vehicle.getName(), part.getName());
        }
        vehicle.removePart(part);
        tuningPartsInventory.add(part);
        return String.format("Uninstalled %s from %s.", part.getName(), vehicle.getName());
    }

}
