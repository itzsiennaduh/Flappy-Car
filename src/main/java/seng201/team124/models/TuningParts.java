package seng201.team124.models;

public class TuningParts {
    private final String name;
    private final String description;
    private final int speedModifier;
    private final int handlingModifier;
    private final int reliabilityModifier;
    private final int fuelEconomyModifier;
    private final int cost;

    /**
     * constructor for tuning parts class.
     * creates a new tuning part with the specified characteristics.
     * @param name the name of the part
     * @param description brief description of the part
     * @param cost purchase cost of the part
     * @param speedModifier effect on vehicle's speed (positive or negative)
     * @param handlingModifier effect on vehicle's handling (positive or negative)
     * @param reliabilityModifier effect on vehicle's reliability (positive or negative)
     * @param fuelEconomyModifier effect on vehicle's fuel economy (positive or negative)
     */
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

    /**
     * @return part name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return part description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return part speed modifier value
     */
    public int getSpeedModifier() {
        return this.speedModifier;
    }

    /**
     * @return part handling modifier value
     */
    public int getHandlingModifier() {
        return this.handlingModifier;
    }

    /**
     * @return part reliability modifier value
     */
    public int getReliabilityModifier() {
        return this.reliabilityModifier;
    }

    /**
     * @return part fuel economy modifier value
     */
    public int getFuelEconomyModifier() {
        return this.fuelEconomyModifier;
    }

    /**
     * @return cost of part in dollars (integer value)
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * compares two tuning parts based on name
     * @param obj the other tuning part to compare to this one
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
        TuningParts other = (TuningParts) obj;
        return other.getName().equals(this.getName());
    }

    /**
     * generates a hashcode for the tuning part based on name
     * @return hashcode for the tuning part
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * @return a string with the tuning part's name and cost and effects
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" - ").append(description);
        sb.append("\nCost: ").append(cost);

        sb.append("\nEffects:");
        if (speedModifier != 0) sb.append("\n  Speed: ").append(formatModifier(speedModifier));
        if (handlingModifier != 0) sb.append("\n  Handling: ").append(formatModifier(handlingModifier));
        if (reliabilityModifier != 0) sb.append("\n  Reliability: ").append(formatModifier(reliabilityModifier));
        if (fuelEconomyModifier != 0) sb.append("\n  Fuel Economy: ").append(formatModifier(fuelEconomyModifier));

        return sb.toString();
    }

    /**
     * formats a modifier value to a string with a + or - sign and a number
     * @param modifier the modifier value to format
     * @return a string with the formatted modifier value
     */
    private String formatModifier(int modifier) {
        StringBuilder sb = new StringBuilder();
        if (modifier > 0) {
            sb.append('+');
        }
        sb.append(modifier);
        return sb.toString();
    }

}
