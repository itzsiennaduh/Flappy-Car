package seng201.team124.models.racelogic;

import java.util.Random;

/**
 * enum for possible random events during a race
 */
public enum RaceEvent {
    BREAKDOWN("Your vehicle has broken down. You may choose whether to retire from the race or pay a repair fee and lose some racing time.",
            0,
            0,
            true,
            "Retire", "Repair (-1 hour, -500 dollars)"
    ),
    STRANDED_TRAVELLER("You happen upon a wandering traveller. He looks like he may need a ride. You pull over and pick him up because you're a good person. He rewards you but you lose some race time)",
            1000,
            1.0,
            false,
            null, null
    ),
    SEVERE_WEATHER("A storm advances from seemingly no where. You are forced to ditch the vehicle and take shelter. You must retire from the race.",
            0,
            0,
            false,
            null, null
    );

    private final String description;
    private final double moneyChange;
    private final double timePenalty;
    private final boolean pleaseChoose;
    private final String choice1;
    private final String choice2;

    /**
     * the constructor for race events.
     * @param description description of the event
     * @param moneyChange money change for the event
     * @param timePenalty time penalty for the event
     * @param pleaseChoose whether the player must choose between the two options
     * @param choice1 first option for the player to choose
     * @param choice2 second option for the player to choose
     */
    RaceEvent(String description, double moneyChange, double timePenalty, boolean pleaseChoose, String choice1, String choice2) {
        this.description = description;
        this.moneyChange = moneyChange;
        this.timePenalty = timePenalty;
        this.pleaseChoose = pleaseChoose;
        this.choice1 = choice1;
        this.choice2 = choice2;
    }

    /**
     * gets the description of the event
     * @return description of the event
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * gets the money change for the event
     * @return money change for the event
     */
    public double getMoneyChange() {
        return this.moneyChange;
    }

    /**
     * gets the time penalty for the event
     * @return time penalty for the event
     */
    public double getTimePenalty() {
        return this.timePenalty;
    }

    /**
     * gets whether the player must choose between the two options
     * @return whether the player must choose between the two options
     */
    public boolean isPleaseChoose() {
        return this.pleaseChoose;
    }

    /**
     * gets the first option for the player to choose
    */
    public String getChoice1() {
        return this.choice1;
    }

    /**
     * gets the second option for the player to choose
    */
    public String getChoice2() {
        return this.choice2;
    }

    /**
     * gets all events with their weights for selection randomly
     */
    public static RaceEvent getRandomEvent() {
        Random random = new Random();
        RaceEvent[] weightedEvents = {BREAKDOWN, BREAKDOWN, BREAKDOWN, BREAKDOWN, BREAKDOWN, // 5/8
                STRANDED_TRAVELLER, STRANDED_TRAVELLER, // 2/8
                SEVERE_WEATHER // 1/8
        };

        int index = random.nextInt(weightedEvents.length);
        return weightedEvents[index];

    }



}
