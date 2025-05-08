package seng201.team124.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chariot vehicle with stats mutable with tuning parts.
 * @see ChariotFactory for the initialisation of the different vehicles.
 */

public class Chariot {
    /**
     * chariot variables/attributes
     * integer values are 0-20 and must add to 20 total (excluding cost). --> this is only
     * for base stats, tuning parts will allow stats to exceed this limit.
     * the cost value is 0-10000 and depends on the set difficulty and spread of chariot stats.
     */
    private String name;
    // private String customName; implement custom naming of cars if time
    /**
     * speed is average velocity on a flat straight km/h */
    private int speed;
    /**
     * higher value is better handling e.g. cornering and maintaining control */
    private int handling;
    /**
     * higher percent, higher reliability, less likely to break down during random events */
    private int reliability;
    /**
     * max distance in km for full fuel tank (km/tank) */
    private int fuelEconomy;
    /**
     * base cost of the chariot */
    private int cost;
    /**
     * list of currently installed tuning parts */
    private List<TuningParts> installedParts;

    /**
     * creates a new Chariot with the specified characteristics.
     *
     * @param name         the name of the chariot
     * @param speed       initial speed rating in km/h
     * @param handling    initial handling rating
     * @param reliability initial reliability percentage
     * @param fuelEconomy initial fuel economy in km/tank
     * @param cost        purchase price
     */
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
     * @return chariot's base cost
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * @return chariot's base speed
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * @return chariot's base handling
     */
    public int getHandling() {
        return this.handling;
    }

    /**
     * @return chariot's base reliability
     */
    public int getReliability() {
        return this.reliability;
    }

    /**
     * @return chariot's base fuel economy
     */
    public int getFuelEconomy() {
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
    public int getEffectiveSpeed() {
        int effectiveSpeed = speed;
        for (TuningParts part : installedParts) {
            effectiveSpeed += part.getSpeedModifier();
        }
        return effectiveSpeed;
    }

    /**
     * Calculates the effective handling induced by all tuning parts applied
     * @return new handling value
     */
    public int getEffectiveHandling() {
        int effectiveHandling = handling;
        for (TuningParts part : installedParts) {
            effectiveHandling += part.getHandlingModifier();
        }
        return effectiveHandling;
    }

    /**
     * Calculates the effective reliability induced by all tuning parts applied
     * @return new reliability value
     */
    public int getEffectiveReliability() {
        int effectiveReliability = reliability;
        for (TuningParts part : installedParts) {
            effectiveReliability += part.getReliabilityModifier();
        }
        return effectiveReliability;
    }

    /**
     * Calculates the effective fuel economy induced by all tuning parts applied
     * @return new fuel economy value
     */
    public int getEffectiveFuelEconomy() {
        int effectiveFuelEconomy = fuelEconomy;
        for (TuningParts part : installedParts) {
            effectiveFuelEconomy += part.getFuelEconomyModifier();
        }
        return effectiveFuelEconomy;
    }

    /**
     * Calculates the total cost after all tuning parts are applied
     * @return new total cost value
     */
    public int getTotalCost() {
        int totalCost = cost;
        for (TuningParts part : installedParts) {
            totalCost += part.getCost();
        }
        return totalCost;
    }

    /**
     * installs a new tuning part
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
                "\nBase Cost: " + cost + " [Total: " + getTotalCost() + "]" +
                "\nInstalled Parts: " + installedParts.size();
    }
}
