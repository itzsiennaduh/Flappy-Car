package seng201.team124.models;

/**
 * contains the result of a random event.
 */
public class EventResult {
    private final RaceEvent event;

    /**
     * constructor
     * @param event The event that was performed
     */
    public EventResult(RaceEvent event) {
        this.event = event;
    }

    /**
     * get a race event that was performed
     * @return The event that was performed.
     */
    public RaceEvent getEvent() {
        return this.event;
    }

    /**
     * get the player's choice
     * @return The player's choice
     */
    public boolean playerPleaseChoose() {
        return event.isPleaseChoose();
    }

    /**
     * get the choice options for the event.
     * @return The choice options for the event. If the event does not have any options, then this will return an empty array.
     */
    public String[] getChoiceOptions() {
        return new String[]{event.getChoice1(), event.getChoice2()};
    }

    /**
     * change player's stats based on the player's choice
     * @param player The player who performed the event
     */
    public void applyEffects(Player player) {
        if (!event.isPleaseChoose()) {
            player.addMoney(event.getMoneyChange());
        }
    }

    /**
     * gets the time penalty for this event
     * @return time penalty in hours
     */
    public double getTimePenalty() {
        return event.getTimePenalty();
    }

    /**
     * apply time effects
     */
    public void applyTimeEffects(Counter counter) {
        if (event.getTimePenalty() != 0) {
            counter.addHours(event.getTimePenalty());
        }
    }
}



