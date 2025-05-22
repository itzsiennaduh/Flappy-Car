package seng201.team124.models.vehicleUtility;

/**
 * represents any item that can be purchased or sold in the game.
 * implemented by vehicle and tuningparts classes.
 */
public interface Purchasable {
    /**
     * gets the name of the purchasable item.
     * @return name of the item
     */
    String getName();

    /**
     * gets the description of the purchasable item.
     * @return description of the item
     */
    String getDescription();

    /**
     * gets the cost of the purchasable item.
     * @return purchase cost of the item, in dollars
     */
    double getCost();

    /**
     * gets the sell price of the purchasable item.
     * @return sell price of the item, in dollars
     */
    double getSellPrice();

    /**
     * calculates sell price
     */
    default double calculateSellPrice() {
        return getCost() * 0.7;
    }
}
