package seng201.team124.services;

import seng201.team124.models.Player;
import seng201.team124.models.raceLogic.*;
import seng201.team124.models.vehicleUtility.*;

import java.util.ArrayList;
import java.util.List;
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
    private int completedRaces = 0;
    private static final double BASE_EVENT_CHANCE = 1;
    private double elapsedSeconds; // time passed in the current race
    private double totalRaceHours; //original race duration
    private final CounterService counter;
    private List<Integer> raceResults = new ArrayList<>();

    public RaceService(Player player, GameManager gameManager, CounterService counterService) {
        this.player = player;
        this.gameManager = gameManager;
        this.counter = counterService;
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
        this.totalRaceHours = race.getHours();
        this.elapsedSeconds = 0;

        Vehicle vehicle = player.getCurrentVehicle();
        vehicle.applyRouteModifiers(route);

        counter.setRaceTimeLimit(race.getHours());
        this.gameManager.startRace(this.currentRace, this.currentRoute);

        System.out.println("Starting race: " + race.getHours() + " hours, ");

        return true;
    }

    public double getTotalRaceHours() {
        return this.totalRaceHours;
    }

    /**
     * increments race time and checks for completion
     * @param deltaHours time to increase
     * @return true if the race is still ongoing
     */
    public boolean incrementRaceTime(double deltaHours) {
        elapsedSeconds += deltaHours;

        if (elapsedSeconds >= totalRaceHours) {
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

        Vehicle vehicle = player.getCurrentVehicle();
        if (vehicle != null) {
            vehicle.resetRouteModifiers();
        }

        completedRaces++;
        raceResults.add(position);

        double prizeMoney = calculatePrizeMoney(position);
        player.addMoney(prizeMoney);
        player.addRaceResult(position, prizeMoney);

        currentRace = null;
        currentRoute = null;
        counter.stopRace();

        gameManager.onRaceCompleted();
    }

    public int getCompletedRacesCount() {
        return completedRaces;
    }

    /**
     * calculates prize money based on the race position
     * @param position final race position (1 = first place)
     * @return prize money amount
     */
    double calculatePrizeMoney(int position) {
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

        RaceEvent[] weightedEvents = new RaceEvent[] {RaceEvent.getRandomEvent()};
        RaceEvent selectedEvent = weightedEvents[random.nextInt(weightedEvents.length)];

        return new seng201.team124.models.raceLogic.EventResult(selectedEvent);
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
            this.counter.modifyTime(-counter.getRemainingTime());
            completeRace(0);
            return "Your vehicle has been written off. Retire from the race, you did not finish.";
        }
    }

    public boolean handleRefuel(boolean shouldRefuel) {
        if (shouldRefuel) {
            this.counter.modifyTime(1.0);
            player.getCurrentVehicle().refuel();
            return true;
        }
        return false;
    }

    public boolean checkFuelLevel(Vehicle vehicle, Route route) {
        double effectiveFuelEconomy = vehicle.getEffectiveFuelEconomy();
        return vehicle.getFuelLevel() >= (route.getDistance() / effectiveFuelEconomy);
    }

    /** determines if random even should take place according to selected difficulty
     * @param difficulty the current game difficulty
     * @return true if a random event should occur
     */
    public boolean shouldRandomEventOccur(Difficulty difficulty) {
        Random random = new Random();
        double baseChance = 1.0;
        double adjustedChance = baseChance * difficulty.getEventChanceMultiplier();
        return random.nextDouble() < adjustedChance;
    }





}

