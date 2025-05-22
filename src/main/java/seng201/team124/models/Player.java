package seng201.team124.models;

import seng201.team124.models.raceLogic.Difficulty;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.services.GameManager;

import java.util.ArrayList;
import java.util.List;

/**
 * represents all the data belonging to the player
 */
public class Player {
    private String name; //players name between 3-15 char incl
    private double money; //money in dollars accessible to the player
    private final List<Vehicle> vehicles; //list of vehicles owned by the player
    private final List<TuningParts> tuningParts; //list of tuning parts owned by the player (not yet installed)
    private Vehicle currentVehicle; //the vehicle the player is currently driving

    /**
     * constructor for the player with their set name and initial money based on difficulty
     * @param name the name of the player, between 3-15 characters long
     * @param money the initial money of the player based on difficulty setting
     */
    public Player(String name, double money) {
        this.name = name;
        this.money = money;
        this.vehicles = new ArrayList<>();
        this.tuningParts = new ArrayList<>();
        this.currentVehicle = null;
    }

    /**
     * gets the name of the player
     * @return the name of the player as a string, between 3-15 characters long.
     */
    public String getName() {
        return GameManager.getInstance().getPlayerName();
    }

    /**
     * gets the current money of the player
     * @return current money amount
     */
    public double getMoney() {
        return this.money;
    }

    /**
     * gets the difficulty value from game manager
     * @return difficulty value
     */
    public Difficulty getDifficulty() {
        return GameManager.getInstance().getDifficultyLevel();
    }

    /**
     * gets the season length from game manager
     * @return season length
     */
    public double getSeasonLength() {
        return GameManager.getInstance().getSeasonLength();
    }

    /**
     * adds money to the player's account
     * @param amount the amount of money to add
     */
    public void addMoney(double amount) {
        this.money += amount;
    }

    /**
     * subtracts money from the player's account
     * @param amount the amount of money to subtract
     * @return true if the player has enough money to subtract the amount, false otherwise
     */
    public boolean subtractMoney(double amount) {
        if (amount > this.money) {
            return false;
        }
        this.money -= amount;
        return true;
    }

    /**
     * gets the list of vehicles owned by the player
     * @return list of vehicles owned by the player
     */
    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles); //copy
    }

    /**
     * adds a vehicle to the list of vehicles owned by the player
     * @param vehicle the vehicle to add
     * @return true if successful, false if max capacity
     * @throws IllegalStateException if the player already owns the vehicle
     */
    public boolean addVehicle(Vehicle vehicle) {
        if (vehicles.contains(vehicle)) {
            throw new IllegalStateException(String.format("%s already owned.", vehicle.getName()));
        }
        if (vehicles.size() >= 5) {
            return false;
        }
        vehicles.add(vehicle);

        if (currentVehicle == null) {
            currentVehicle = vehicle;
        }
        return true;
    }

    /**
     * removes a vehicle from the list of vehicles owned by the player (sold the car)
     * @param vehicle the vehicle to remove
     * @return true if successful, false if was the player's last vehicle
     * @throws IllegalStateException if the player does not own the vehicle
     */
    public boolean removeVehicle(Vehicle vehicle) {
        if (!vehicles.contains(vehicle)) {
            throw new IllegalStateException(String.format("%s is not owned, so can't be removed.", vehicle.getName()));
        }
        if (vehicles.size() <= 1) {
            return false;
        }
        boolean result = vehicles.remove(vehicle);

        if (result && currentVehicle == vehicle) {
            currentVehicle = vehicles.getFirst();
        }
        return result;
    }

    /**
     * gets the currently selected vehicle
     * @return the currently selected vehicle
     */
    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    /**
     * sets the currently selected vehicle
     * @param vehicle the vehicle to set as the current vehicle
     * @return true if successful, false if the player does not own the vehicle
     */
    public boolean setCurrentVehicle(Vehicle vehicle) {
        if (!vehicles.contains(vehicle)) {
            return false;
        }
        currentVehicle = vehicle;
        return true;
    }

    /**
     * gets all uninstalled tuning parts owned by the player
     * @return list of tuning parts owned by the player
     */
    public List<TuningParts> getTuningParts() {
        return new ArrayList<>(tuningParts); //copy
    }

    /**
     * adds a tuning part to the list of tuning parts owned by the player (inventory)
     * @param part the tuning part to add
     */
    public void addTuningPart(TuningParts part) {
        tuningParts.add(part);
    }

    /**
     * removes a tuning part from the list of tuning parts owned by the player (inventory)
     * @param part the tuning part to remove
     * @return true if successful
     * @throws IllegalStateException if the player does not own the tuning part
     */
    public boolean removeTuningPart(TuningParts part) {
        if (!tuningParts.contains(part)) {
            throw new IllegalStateException(String.format("%s is not owned, so can't be removed.", part.getName()));
        }
        return tuningParts.remove(part);
    }

    /**
     * installs a tuning part on the current vehicle
     * @param part the tuning part to install
     * @param vehicle the vehicle to install the tuning part on
     * @return installation message or error message
     */
    public String installTuningPart(TuningParts part, Vehicle vehicle) {
        if (!vehicles.contains(vehicle)) {
            return String.format("%s is not owned, so %s can't be installed.", vehicle.getName(), part.getName());
        }
        if (!tuningParts.contains(part)) {
            return String.format("%s is not owned, so it can't be installed on %s.", part.getName(), vehicle.getName());
        }

        try {
            vehicle.installPart(part);
            tuningParts.remove(part);
            return String.format("%s successfully installed on %s!", part.getName(), vehicle.getName());
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    /**
     * uninstalls a tuning part from the current vehicle
     * @param part the tuning part to uninstall
     * @param vehicle the vehicle to uninstall the tuning part from
     * @return uninstallation message or error message
     */
    public String uninstallTuningPart(TuningParts part, Vehicle vehicle) {
        if (!vehicles.contains(vehicle)) {
            return String.format("%s is not owned, so %s can't be uninstalled.", vehicle.getName(), part.getName());
        }

        try {
            vehicle.removePart(part);
            tuningParts.add(part);
            return String.format("%s successfully uninstalled from %s!", part.getName(), vehicle.getName());
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }
}
