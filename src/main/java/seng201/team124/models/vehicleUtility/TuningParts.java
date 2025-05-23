package seng201.team124.models.vehicleUtility;

import seng201.team124.factories.TuningPartFactory;

/**
 * tuning part class with properties.
 * available in the shop and can be applied to vehicles.
 * @see TuningPartFactory for the tuning parts
 */
public class TuningParts implements Purchasable{

    public enum Type {
        ENGINE,
        NITRO,
        BODY, WHEEL
    }

    private final String name;
    private final String description;
    private final double speedModifier;
    private final double handlingModifier;
    private final double reliabilityModifier;
    private final double fuelEconomyModifier;
    private final double cost;
    private final Type type;

    /**
     * constructor for tuning parts class.
     * creates a new tuning part with the specified characteristics.
     * @param name the name of the part
     * @param description brief description of the part
     * @param cost cost of the part
     * @param speedModifier effect on vehicle's speed (positive or negative)
     * @param handlingModifier effect on vehicle's handling (positive or negative)
     * @param reliabilityModifier effect on vehicle's reliability (positive or negative)
     * @param fuelEconomyModifier effect on vehicle's fuel economy (positive or negative)
     */
    public TuningParts(String name, String description, double speedModifier,
                       double handlingModifier, double reliabilityModifier,
                       double fuelEconomyModifier, double cost, Type type) {
        this.name = name;
        this.description = description;
        this.speedModifier = speedModifier;
        this.handlingModifier = handlingModifier;
        this.reliabilityModifier = reliabilityModifier;
        this.fuelEconomyModifier = fuelEconomyModifier;
        this.cost = cost;
        this.type = type;
    }

    /**
     * gets the part name.
     * @return part name
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets the part description.
     * @return part description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * gets the part speed modifier.
     * @return part speed modifier value
     */
    public double getSpeedModifier() {
        return this.speedModifier;
    }

    /**
     * gets the part handling modifier.
     * @return part handling modifier value
     */
    public double getHandlingModifier() {
        return this.handlingModifier;
    }

    /**
     * gets the part reliability modifier.
     * @return part reliability modifier value
     */
    public double getReliabilityModifier() {
        return this.reliabilityModifier;
    }

    /**
     * gets the part fuel economy modifier.
     * @return part fuel economy modifier value
     */
    public double getFuelEconomyModifier() {
        return this.fuelEconomyModifier;
    }

    /**
     * gets the part cost.
     * @return cost of part in dollars (integer value)
     */
    public double getCost() {
        return this.cost;
    }

    public Type getType() {return this.type;}

    /**
     * calculates the sell price of the part.
     * @return the sell price of the part
     */
    public double getSellPrice() {
        return calculateSellPrice();
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
        return String.format("""
        %s - %s
        Cost: %.2f
        Effects:
          Speed: %s
          Handling: %s
          Reliability: %s
          Fuel Economy: %s""",
                name, description, cost,
                formatModifier(speedModifier),
                formatModifier(handlingModifier),
                formatModifier(reliabilityModifier),
                formatModifier(fuelEconomyModifier));
    }

    /**
     * formats a modifier value to a string with a + or - sign and a number
     * @param modifier the modifier value to format
     * @return a string with the formatted modifier value
     */
    private String formatModifier(double modifier) {
        StringBuilder sb = new StringBuilder();
        if (modifier > 0) {
            sb.append('+');
        }
        sb.append(modifier);
        return sb.toString();
    }
}
