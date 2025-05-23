package seng201.team124.models.vehicleUtility;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.Group;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.models.raceLogic.Route;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a vehicle with stats mutable with tuning parts.
 * @see VehicleFactory for the initialisation of the different vehicles.
 */

public class Vehicle implements Purchasable {
    /**
     * vehicle variables/attributes
     * integer values are 0-20 and must add to 20 total (excluding cost). --> this is only
     * for base stats, tuning parts will allow stats to exceed this limit.
     * the cost value is 0-10000 and depends on the set difficulty and spread of vehicle stats.
     */
    private final String name;
    private final String description;

    private final double speed;
    private final double handling;
    private final double reliability;
    private final double fuelEconomy;
    private final double cost;
    private String previewImagePath;
    private double acceleration;

    private double currentFuel;
    private final double maxFuel;

    private double routeSpeedModifier = 1.0;
    private double routeHandlingModifier = 1.0;
    private double routeReliabilityModifier = 1.0;
    private double routeFuelEconomyModifier = 1.0;

    private TuningParts engine;
    private TuningParts wheels;
    private TuningParts nitro;

    private final List<TuningParts> installedParts;

    private String model;

    /**
     * constructor for the vehicle class.
     * creates a new vehicle with the specified characteristics.
     * @param name the name of the vehicle
     * @param description brief description of the vehicle
     * @param speed initial speed rating in km/h
     * @param handling initial handling rating
     * @param reliability initial reliability percentage
     * @param fuelEconomy initial fuel economy in km/tank
     * @param cost purchase price
     * @param currentFuel the fuel level of the vehicle
     * @param maxFuel max fuel the vehicle may have
     */
    public Vehicle(String name, String description, double speed, double accleration, double handling, double reliability, double fuelEconomy, double cost, double currentFuel, double maxFuel, String modelName, String previewImagePath) {
        this.name = name;
        this.description = description;
        this.speed = speed;
        this.acceleration = accleration;
        this.handling = handling;
        this.reliability = reliability;
        this.fuelEconomy = fuelEconomy;
        this.cost = cost;
        this.currentFuel = currentFuel;
        this.maxFuel = maxFuel;
        this.installedParts = new ArrayList<>();
        this.model = modelName;
        this.previewImagePath = previewImagePath;
    }

    /**
     * gets the model name of the vehicle
     * @return the model name of the vehicle (string)
     */
    public String getModelName() {return this.model;}

    /**
     * gets the acceleration of the vehicle
     * @return acceleration
     */
    public double getAcceleration() {return this.acceleration;}

    /**
     * gets vehicle name
     * @return vehicle name
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets vehicle description
     * @return vehicle description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * gets vehicles base cost
     * @return vehicle's base cost
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * calculates the sell price of the vehicle
     * @return sell price of the vehicle (percentage of cost)
     */
    public double getSellPrice() {
        double vehicleValue = calculateSellPrice();

        double partsValue = 0;
        for (TuningParts part : installedParts) {
            partsValue += calculateSellPrice();
        }
        return vehicleValue + partsValue;
    }

    /**
     * checks if object equals object
     * @param obj the other object to compare to this one
     * @return true if the names are the same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vehicle other = (Vehicle) obj;
        return other.getName().equals(this.getName());
    }


    /**
     * @return vehicle's base speed
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * @return vehicle's base handling
     */
    public double getHandling() {
        return this.handling;
    }

    /**
     * @return vehicle's base reliability
     */
    public double getReliability() {
        return this.reliability;
    }

    /**
     * @return vehicle's base fuel economy
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
     * calculates the effective speed induced by all tuning parts applied
     * @return new speed value
     */
    public double getEffectiveSpeed() {
        double effectiveSpeed = this.speed;
        for (TuningParts part : installedParts) {
            effectiveSpeed += part.getSpeedModifier();
        }
        return effectiveSpeed * routeSpeedModifier;
    }

    /**
     * calculates the effective handling induced by all tuning parts applied
     * @return new handling value
     */
    public double getEffectiveHandling() {
        double effectiveHandling = this.handling;
        for (TuningParts part : installedParts) {
            effectiveHandling += part.getHandlingModifier();
        }
        return effectiveHandling * routeHandlingModifier;
    }

    /**
     * calculates the effective reliability induced by all tuning parts applied
     * @return new reliability value
     */
    public double getEffectiveReliability() {
        double effectiveReliability = this.reliability;
        for (TuningParts part : installedParts) {
            effectiveReliability += part.getReliabilityModifier();
        }
        return effectiveReliability * routeReliabilityModifier;
    }

    /**
     * calculates the effective fuel economy induced by all tuning parts applied
     * @return new fuel economy value
     */
    public double getEffectiveFuelEconomy() {
        double effectiveFuelEconomy = this.fuelEconomy;
        for (TuningParts part : installedParts) {
            effectiveFuelEconomy += part.getFuelEconomyModifier();
        }
        return effectiveFuelEconomy * routeFuelEconomyModifier;
    }

    /**
     * calculates the total cost after all tuning parts are applied
     * @return new total cost value
     */
    public double getTotalCost() {
        double totalCost = this.cost;
        for (TuningParts part : installedParts) {
            totalCost += part.getCost();
        }
        return totalCost;
    }

    /**
     * get the amount of fuel in the vehicle
     * @return current fuel level
     */
    public double getFuelLevel() {
        return this.currentFuel;
    }

    /**
     * get the maximum fuel level of the vehicle
     * @return max fuel level
     */
    public double getMaxFuelLevel() {
        return this.maxFuel;
    }

    /**
     * installs a new tuning part
     * @param part the specific part to install
     * @return successful installation message
     * @throws IllegalStateException if part is already installed
     */
    public String installPart(TuningParts part) {

        switch (part.getType()) {
            case ENGINE:
                if (engine != null) {
                    throw new IllegalStateException("Engine already installed!");
                }
                engine = part;
                break;
            case NITRO:
                if (nitro != null) {
                    throw new IllegalStateException("Nitro already installed!");
                }
                nitro = part;
                break;
            case WHEEL:
                if (wheels != null) {
                    throw new IllegalStateException("Wheels already installed!");
                }
                wheels = part;
                break;
        }


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
        if (part.equals(engine)) {engine = null;}
        else if (part.equals(nitro)) {nitro = null;}
        else if (part.equals(wheels)) {wheels = null;}
        else throw new IllegalStateException("No such part installed!");

        if (installedParts.contains(part)) {
            installedParts.remove(part);
            return String.format("%s successfully removed!", part.getName());
        }
        throw new IllegalStateException(String.format("%s is not installed, so can't be removed.", part.getName()));
    }

    public TuningParts getInstalledEngine() { return engine; }
    public TuningParts getInstalledNitro()  { return nitro;  }
    public TuningParts getInstalledWheels() { return wheels; }

    /**
     * refuels vehicle back to max fuel
     */
    public void refuel() {
        if (this.currentFuel <= this.maxFuel) {
            this.currentFuel += 10;
            if (this.currentFuel > this.maxFuel) {
                this.currentFuel = this.maxFuel;
            }
            System.out.println("Vehicle refueled!");
        } else {
            this.currentFuel = this.maxFuel;
            System.out.println("Vehicle is already at max fuel level");
        }

    }

    /**
     * consumes a specified amount of fuel
     * throws an exception if the fuel level reaches zero after consumption.
     * @param amount the amount of fuel to consume
     * @throws IllegalStateException if the vehicle runs out of fuel after consumption
     */
    public void consumeFuel(double amount) {
        this.currentFuel = Math.max(0, this.currentFuel - amount);
        if (this.currentFuel == 0) {
            this.currentFuel = 0;
            throw new IllegalStateException("Out of fuel!");
        }
    }

    /**
     * apply temporary route modifiers to the vehicle
     * @param route the route from which the modifiers are from
     */
    public void applyRouteModifiers(Route route) {
        this.routeSpeedModifier = route.getSpeedModifier();
        this.routeHandlingModifier = route.getHandlingModifier();
        this.routeReliabilityModifier = route.getReliabilityModifier();
        this.routeFuelEconomyModifier = route.getFuelEconomyModifier();
    }

    /**
     * reset route modifiers back to default (usually used after a race)
     */
    public void resetRouteModifiers() {
        this.routeSpeedModifier = 1.0;
        this.routeHandlingModifier = 1.0;
        this.routeReliabilityModifier = 1.0;
        this.routeFuelEconomyModifier = 1.0;
    }

    /**
     * @return a string with the vehicle's effective stats
     */
    @Override
    public String toString() {
        return  "Vehicle: " + getName() +
                "\nSpeed: " + getEffectiveSpeed() +
                "\nHandling: " + getEffectiveHandling() +
                "\nReliability: " + getEffectiveReliability() + "%" +
                "\nFuel Economy: " + getEffectiveFuelEconomy() + " km/tank" +
                "\nCost: " + getCost() +
                "\nInstalled Parts: " + installedParts.size();

    }

    /**
     * @return a string with the vehicle's base and effective stats for comparison
     */
    public String toDetailedString() {
        return  "Vehicle: " + this.name + " - " + this.description +
                "\nSpeed: " + this.speed + " [Effective: " + getEffectiveSpeed() + "]" +
                "\nHandling: " + this.handling + " [Effective: " + getEffectiveHandling() + "]" +
                "\nReliability: " + this.reliability + "% [Effective: " + getEffectiveReliability() + "]" +
                "\nFuel Economy: " + this.fuelEconomy + "km/tank [Effective: " + getEffectiveFuelEconomy() + "]" +
                "\nBase Cost: " + this.cost + " [Total: " + getTotalCost() + "]" +
                "\nSell Price: $" + getSellPrice() +
                "\nInstalled Parts: " + installedParts.size();
    }


    public Group getModel() {
        URL url = getClass().getResource(model);
        if (url == null) {
            System.err.println("Error: Could not find model file for vehicle " + name);
            return null;
        }
        return loadModel(url);
    }

    private Group loadModel(URL url) {
        return ModelLoader.getGroup(url);
    }

    public String getPreviewImagePath() {
        return previewImagePath;
    }
}
