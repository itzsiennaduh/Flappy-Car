package seng201.team124.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Chariots attributes and modifications via tuning parts,
 * calculates new stats after tuning part modification.
 */

public class Chariot {
    // Chariot variables
    private String name; //cool name
    // private String customName; implement custom naming of cars if time
    private int speed; // average velocity on a flat straight km/h
    private int handling; //higher value is better handling e.g. cornering and maintaining control
    private int reliability; //higher percent, higher reliability, less likely to break down during random events
    private int fuelEconomy; //max distance in km for full fuel tank (km/tank)
    private int cost; //purchase price (0-10000)

    private List<TuningParts> installedParts; //list of parts installed

    //constructor
    public Chariot(String name, int speed, int handling, int reliability, int fuelEconomy, int cost) {
        this.name = name;
        this.speed = speed;
        this.handling = handling;
        this.reliability = reliability;
        this.fuelEconomy = fuelEconomy;
        this.cost = cost;
        this.installedParts = new ArrayList<>();
    }

    /**
     * @return chariot name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return chariots base cost
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * @return chariots base speed
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * @return chariots base handling
     */
    public double getHandling() {
        return this.handling;
    }

    /**
     * @return chariots base reliability
     */
    public double getReliability() {
        return this.reliability;
    }

    /**
     * @return chariots base fuel economy
     */
    public double getFuelEconomy() {
        return this.fuelEconomy;
    }

    /**
     * @return list of installed parts
     */
    public List<TuningParts> getInstalledParts() {
        return new ArrayList<>(installedParts); //copy
    }

    /**
     * Calculates the effective speed induced by all tuning parts applied
     * @return new speed value
     */
    public double getEffectiveSpeed() {
        double effectiveSpeed = speed;
        for (TuningParts part : installedParts) {
           // effectiveSpeed += part.getSpeedModifier();
        }
        return effectiveSpeed;
    }

    /**
     * Calculates the effective handling induced by all tuning parts applied
     * @return new handling value
     */
    public double getEffectiveHandling() {
        double effectiveHandling = handling;
        for (TuningParts part : installedParts) {
          //  effectiveHandling += part.getHandlingModifier();
        }
        return effectiveHandling;
    }

    /**
     * Calculates the effective reliability induced by all tuning parts applied
     * @return new reliability value
     */
    public double getEffectiveReliability() {
        double effectiveReliability = reliability;
        for (TuningParts part : installedParts) {
          //  effectiveReliability += part.getReliabilityModifier();
        }
        return effectiveReliability;
    }

    /**
     * Calculates the effective fuel economy induced by all tuning parts applied
     * @return new fuel economy value
     */
    public double getEffectiveFuelEconomy() {
        double effectiveFuelEconomy = fuelEconomy;
        for (TuningParts part : installedParts) {
         //   effectiveFuelEconomy += part.getFuelEconomyModifier();
        }
        return effectiveFuelEconomy;
    }

    /**
     * Calculates the total cost after all tuning parts are applied
     * @return new total cost value
     */
    public double getTotalCost() {
        double totalCost = cost;
        for (TuningParts part : installedParts) {
          //  totalCost += part.getCost();
        }
        return totalCost;
    }

    /**
     * installs new tuning part
     * @param part the specific part to install
     * @return successful installation message
     * @throws IllegalStateException if part is already installed
     */
    public String installPart(TuningParts part) {
        if (installedParts.contains(part)) {
            throw new IllegalStateException(String.format("%s already installed.", part.getName()));
        }
        installedParts.add(part);
        return String.format("%s successfully installed!", part.getName());
    }

    /**
     * @param part the part we want to remove
     * @return successful removal message
     * @throws IllegalStateException if there is no part to remove
     */
    public String removePart(TuningParts part) {
        if (installedParts.contains(part)) {
            installedParts.remove(part);
            return String.format("%s successfully removed!", part.getName());
        }
        throw new IllegalStateException(String.format("%s is not installed, so can't be removed.", part.getName()));
    }

    /**
     * @return a string with the chariot's effective stats
     */
    @Override
    public String toString() {
        return  "Chariot: " + name +
                "\nSpeed: " + getEffectiveSpeed() +
                "\nHandling: " + getEffectiveHandling() +
                "\nReliability: " + getEffectiveReliability() +
                "\nFuel Economy: " + getEffectiveFuelEconomy() +
                "\nTotal Cost: " + getTotalCost();
    }

    /**
     * @return a string with the chariot's base and effective stats for comparison
     */
    public String toDetailedString() {
        return  "Chariot: " + name +
                "\nSpeed: " + speed + " [Effective: " + getEffectiveSpeed() + "]" +
                "\nHandling: " + handling + " [Effective: " + getEffectiveHandling() + "]" +
                "\nReliability: " + reliability + " [Effective: " + getEffectiveReliability() + "]" +
                "\nFuel Economy: " + fuelEconomy + " [Effective: " + getEffectiveFuelEconomy() + "]" +
                "\nBase Cost: " + cost + "[Total: " + getTotalCost() + "]" +
                "\nInstalled Parts: " + installedParts.size();
    }
}
