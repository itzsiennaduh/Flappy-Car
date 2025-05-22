package seng201.team124.models.raceLogic;

import seng201.team124.models.Player;
import seng201.team124.services.RaceService;

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
    public boolean requiresChoice() {
        return event.isPleaseChoose();
    }

    /**
     * get the choice options for the event.
     * @return The choice options for the event. If the event does not have any options, then this will return an empty array.
     */
    public String[] getChoiceOptions() {
        if (!requiresChoice()) {
            return new String[0];
        }
        return new String[]{event.getChoice1(), event.getChoice2()};
    }

    /**
     * change player's stats for events without choice
     * @param player The player who performed the event
     * @param counter the game timer
     * @param raceService the race service
     */
    public void applyAutoEffects(Player player, Counter counter, RaceService raceService) {
        player.addMoney(event.getMoneyChange());
        if (event.getTimePenalty() != 0) {
            counter.addHours(event.getTimePenalty());
        }

        if (event == RaceEvent.SEVERE_WEATHER) {
            raceService.completeRace(0);
        }
    }

    /**
     * apply effects for player's first choice (retire)
     * @param player the player
     * @param raceService the race service
     */
    public void handleChoice1(Player player, RaceService raceService) {
        if (event == RaceEvent.BREAKDOWN) {
            raceService.completeRace(0);
        }
    }

    /**
     * apply effects for player's second choice (repair)
     * @param player the player
     * @param counter the game counter
     * @param raceService the race service
     */
    public void handleChoice2(Player player, Counter counter, RaceService raceService) {
        if (event == RaceEvent.BREAKDOWN) {
            player.subtractMoney(500);
            counter.addHours(1.0);
        }
    }

}



