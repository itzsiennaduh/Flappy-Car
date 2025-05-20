package seng201.team124.models.racelogic;

/**
 * enum for difficulty levels
 */
public enum Difficulty {
    EASY(1.5, 0.7, 0.5),
    MEDIUM(1.0, 1.0, 1.0),
    HARD(0.5, 1.3, 1.5);

    private final double moneyMultiplier; //prize money
    private final double eventChanceMultiplier;
    private final double raceDifficultyMultiplier;

    /**
     * constructs a difficulty level with the specified multipliers
     * @param moneyMultiplier multiplier for prize money
     * @param eventChanceMultiplier multiplier for event chances
     * @param routeDifficultyMultiplier multiplier for route difficulty
     */
    Difficulty(double moneyMultiplier, double eventChanceMultiplier, double routeDifficultyMultiplier) {
        this.moneyMultiplier = moneyMultiplier;
        this.eventChanceMultiplier = eventChanceMultiplier;
        this.raceDifficultyMultiplier = routeDifficultyMultiplier;
    }

    /**
     * gets the money multiplier for this difficulty level
     * @return money multiplier
     */
    public double getMoneyMultiplier() {
        return moneyMultiplier;
    }

    /**
     * gets the random event chance multiplier for this difficulty level
     * @return multiplier for event chances
     */
    public double getEventChanceMultiplier() {
        return eventChanceMultiplier;
    }

    /**
     * gets the route difficulty multiplier for this difficulty level
     * @return multiplier for route difficulty
     */
    public double getRaceDifficultyMultiplier() {
        return raceDifficultyMultiplier;
    }

    /**
     * calculates the starting money for a player based on the difficulty level
     * @param baseMoney Base money amount
     * @return starting money adjusted for difficulty
     */
    public double calculateStartingMoney(double baseMoney) {
        return (int) (baseMoney * getMoneyMultiplier());
    }
}
