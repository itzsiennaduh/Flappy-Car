package seng201.team124.models;

/**
 * route class with properties.
 * routes affect vehicle classes stats. there should be at least one per race.
 */
public class Route {
    private final String description; //routes properties
    private final int distance; //length in kms
    private final int fuelStops; // num of fuel stops
    private final int difficulty; //overall route difficulty.

    private final double speedModifier; //effect on a vehicle's speed
    private final double handlingModifier; //effect on a vehicle's handling
    private final double reliabilityModifier; //effect on a vehicle's reliability
    private final double fuelEconomyModifier; //effect on a vehicle's fuel economy

    //constructor
    public Route(String description, int distance, int fuelStops, int difficulty, double speedModifier, double handlingModifier, double reliabilityModifier, double fuelEconomyModifier) {
        this.description = description;
        this.distance = distance;
        this.fuelStops = fuelStops;
        this.difficulty = difficulty;
        this.speedModifier = speedModifier;
        this.handlingModifier = handlingModifier;
        this.reliabilityModifier = reliabilityModifier;
        this.fuelEconomyModifier = fuelEconomyModifier;
    }

    //getters
    public String getDescription() {
        return this.description;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getFuelStops() {
        return this.fuelStops;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public double getSpeedModifier() {
        return this.speedModifier;
    }

    public double getHandlingModifier() {
        return this.handlingModifier;
    }

    public double getReliabilityModifier() {
        return this.reliabilityModifier;
    }

    public double getFuelEconomyModifier() {
        return this.fuelEconomyModifier;
    }

    //fuel stops distance?

    /**
     * @return a string with the route's description, distance, fuel stops, and difficulty (detailed)
     */
    @Override
    public String toString() {
        return  "Route Description: " + getDescription() +
                "\nDistance: " + getDistance() + " km" +
                "\nFuel Stops: " + getFuelStops() +
                "\nOverall Difficulty: " + getDifficulty() +
                "\nThis affects: " +
                "\nSpeed: " + getSpeedModifier() +
                "\nHandling: " + getHandlingModifier() +
                "\nReliability: " + getReliabilityModifier() +
                "\nFuel Economy: " + getFuelEconomyModifier();
    }
}
