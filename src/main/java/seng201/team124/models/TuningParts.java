package seng201.team124.models;

public class TuningParts {
    private String name;
    private String description;
    private int speedModifier;
    private int handlingModifier;
    private int reliabilityModifier;
    private int fuelEconomyModifier;
    private int cost;

    //constructor
    public TuningParts(String name, String description, int speedModifier,
                       int handlingModifier, int reliabilityModifier,
                       int fuelEconomyModifier, int cost) {
        this.name = name;
        this.description = description;
        this.speedModifier = speedModifier;
        this.handlingModifier = handlingModifier;
        this.reliabilityModifier = reliabilityModifier;
        this.fuelEconomyModifier = fuelEconomyModifier;
        this.cost = cost;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }
}
