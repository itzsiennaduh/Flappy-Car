package seng201.team124.services;

import seng201.team124.models.*;

/**
 * handles all garage-related operations and logic
 */
public class GarageService {
    private final Player player;

    public GarageService(Player player) {
        this.player = player;
    }

    /**
     * installs a tuning part on a vehicle
     * @param vehicle vehicle to install tuning part on
     * @param part tuning part to install
     * @return success message if installation was successful, error message otherwise
     */
    public String installTuningPart(TuningParts part, Vehicle vehicle) {
        if (!player.getTuningParts().contains(part)) {
            return "You do not own this tuning part.";
        }
        if (!player.getVehicles().contains(vehicle)) {
            return "You do not own this vehicle.";
        }
        player.installTuningPart(part, vehicle);
        return "Tuning part installed on %s.".formatted(vehicle.getName());
    }

    /**
     * uninstalls a tuning part from a vehicle
     * @param part tuning part to uninstall
     * @param vehicle vehicle to uninstall tuning part from
     * @return success message if uninstallation was successful, error message otherwise
     */
    public String uninstallTuningPart(TuningParts part, Vehicle vehicle) {
        if (!player.getTuningParts().contains(part)) {
            return "You do not own this tuning part.";
        }
        if (!player.getVehicles().contains(vehicle)) {
            return "You do not own this vehicle.";
        }
        if (!vehicle.getInstalledParts().contains(part)) {
            return "This tuning part is not installed on this vehicle.";
        }
        player.uninstallTuningPart(part, vehicle);
        return "Tuning part uninstalled from %s.".formatted(vehicle.getName());
    }

    /**
     * changes the currently selected vehicle in the garage
     * @param vehicle vehicle to select
     * @return true if successful
     */
    public boolean changeCurrentVehicle(Vehicle vehicle) {
        if (!player.getVehicles().contains(vehicle)) {
            return false;
        }
        player.setCurrentVehicle(vehicle);
        return true;
    }

    /**
     * gets the currently selected vehicle
     * @return the currently selected vehicle
     */
    public Vehicle getCurrentVehicle() {
        return player.getCurrentVehicle();
    }
}
