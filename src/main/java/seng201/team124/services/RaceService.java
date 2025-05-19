package seng201.team124.services;

import seng201.team124.models.*;
import java.util.Random;

/**
 * handles all race-related operations and logic
 */
public class RaceService {
    private final Player player;
    private final GameManager gameManager;
    private Race currentRace;
    private Route currentRoute;
    private final Random random = new Random();
    private static final double BASE_EVENT_CHANCE = 0.2;
    private double elapsedHours; // time passed in the current race
    private double totalRaceHours; //original race duration
    private final CounterService counter;

    public RaceService(Player player, GameManager gameManager) {
        this.player = player;
        this.gameManager = gameManager;
        this.counter = new CounterService();
    }

    /**
     * starts a race with the selected route
     * @param race race to start
     * @param route route to take
     * @return true if the race was started successfully, false otherwise
     */
    public boolean startRace(Race race, Route route) {
        if (player.getCurrentVehicle() == null) {
            return false;
        }
        this.currentRace = race;
        this.currentRoute = route;
        this.counter.startRace(race);
        this.gameManager.startRace(race);
        return true;
    }

    /**
     * increments race time and checks for completion
     * @param deltaHours time to increase
     * @return true if the race is still ongoing
     */
    public boolean incrementRaceTime(double deltaHours) {
        elapsedHours += deltaHours;

        if (elapsedHours >= totalRaceHours) {
            completeRace(0); //DNF, out of time
            return false;
        }

        //implement check if the finish line crossed
        return true;
    }

    /**
     * completes the current race
     * @param position final race position (1 = first place)
     */
    public void completeRace(int position) {
        if (currentRace == null) return;

        double prizeMoney = calculatePrizeMoney(position);
        player.addMoney(prizeMoney);
        gameManager.completeRace(position);
        currentRace = null;
        currentRoute = null;
    }

    /**
     * calculates prize money based on the race position
     * @param position final race position (1 = first place)
     * @return prize money amount
     */
    private double calculatePrizeMoney(int position) {
        if (position <= 0) {
            return 0; //invalid or DNF
        }
        double basePrize = currentRace.getPrizeMoney();
        double positionMultiplier = 1.0 / position;
        double difficultyMultiplier = gameManager.getDifficultyLevel().getRaceDifficultyMultiplier();
        return (basePrize * positionMultiplier * difficultyMultiplier);
    }

    /**
     * handles random events during the race
     * @return event description
     */
    public EventResult handleRandomEvent() {
        Difficulty difficulty = gameManager.getDifficultyLevel();
        Vehicle vehicle = player.getCurrentVehicle();

        double eventChance = BASE_EVENT_CHANCE * difficulty.getEventChanceMultiplier() *
                (1 - (vehicle.getEffectiveReliability() / 100.0));

        if (random.nextDouble() >= eventChance) {
            return null; //no event
        }

        RaceEvent[] weightedEvents = RaceEvent.getWeightedEvents();
        RaceEvent selectedEvent = weightedEvents[random.nextInt(weightedEvents.length)];

        return new EventResult(selectedEvent);
    }

    /**
     * process player's choice for breakdown event
     * @param repair true to repair, false to replace
     * @return message to display to the player and applies consequences
     */
    public String processBreakdownEvent(boolean repair) {
        if (repair) {
            if (!player.subtractMoney(500)) {
                completeRace(0);
                return "You do not have enough money to repair your vehicle. You must retire.";
            }
            this.counter.modifyTime(1.0);
            return "Your vehicle has been repaired. A time penalty of 1.0 hr has been applied to your race.";
        } else {
            this.counter.modifyTime(-counter.getRemainingHours());
            completeRace(0);
            return "Your vehicle has been written off. Retire from the race, you did not finish.";
        }
    }


}
