package seng201.team124.services;

import seng201.team124.models.*;

/**
 * Class to manage the game state
 */
public class GameManager {
    private static GameManager instance;
    private Player player;
    //private Season season;
    //private Shop shop;
    //private Garage garage;
    private Race currentRace;

    private Difficulty difficultyLevel;
    private String playerName;
    private boolean isRaceActive = false;

    private static final double BASE_STARTING_MONEY = 10000;

    private GameManager() {
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * initialises a new game
     * @param playerName name of the player
     * @param difficultyLevel selected difficulty level of the game
     * @param seasonLength number of races in the season
     */
    public void initializeGame(String playerName, Difficulty difficultyLevel, int seasonLength) {
        this.playerName = playerName;
        this.difficultyLevel = difficultyLevel;

        double startingMoney = difficultyLevel.calculateStartingMoney(BASE_STARTING_MONEY);
        this.player = new Player(playerName, startingMoney);
        //this.season = new Season(seasonLength, difficultyLevel);
        //this.shop = new Shop(difficultyLevel);
        //this.garage = new Garage(player);
        currentRace = null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayerName(String name) {
        if (name == null || name.trim().length() < 3 || name.trim().length() > 15) {
            throw new IllegalArgumentException("Player name must be between 3 and 15 characters long.");
        }
        this.playerName = name.trim();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Difficulty getDifficultyLevel() {
        return this.difficultyLevel;
    }

    //public List<Vehicle> getAvailableVehicles() {
        //return shop.getAvailableVehicles();
    //}

    //public List<TuningParts> getAvailableTuningParts() {
        //return shop.getAvailableTuningParts();
    //}

    //public List<Vehicle> getGarageVehicles() {
        //return garage.getVehicles();
    //}

    public boolean purchaseVehicle(Vehicle vehicle) {
        if (player.subtractMoney(vehicle.getCost())) {
            if (player.addVehicle(vehicle)) {
                //shop.removeVehicle(vehicle);
                return true;
            }
            player.addMoney(vehicle.getCost());
        }
        return false;
    }

    public void startRace(Race race) {
        if (player.getCurrentVehicle() == null) {
            throw new IllegalStateException("Cannot start a race without a vehicle selected.");
        }
        this.currentRace = race;
        this.isRaceActive = true;
    }

    /**
     * completes the current race and updates game state
     * @param position final race position (1=first place, 2=second place, etc.)
     */
    public void completeRace(int position) {
        if (!isRaceActive) return;

        //double basePrize = currentRace.getBasePrize();
        double difficultyMultiplier = difficultyLevel.getMoneyMultiplier();
        //double actualPrize = (int) (basePrize * (1.0 / position) * difficultyMultiplier);

        //player.addMoney(actualPrize);
        //season.completeRace();
        currentRace = null;
        isRaceActive = false;

        // refresh
        //shop.refreshStock();
        //season.generateNewRaces();
    }

    public boolean installPart(TuningParts part, Vehicle vehicle) {
        if (!player.getTuningParts().contains(part)) return false;
        //if (!player.getVehicles.contains(vehicle)) return false;

        vehicle.installPart(part);
        player.removeTuningPart(part);
        return true;
    }

    //public int getRacesCompleted() {
        //return season.getRacesCompleted();
    //}

    //public int getTotalRaces() {
        //return season.getTotalRaces();
    //}

    //public boolean isSeasonComplete() {
        //return season.isSeasonComplete();
    //}

    public Vehicle getCurrentVehicle() {
        return player.getCurrentVehicle();
    }

    public void applyVehicleStats() {
        Vehicle vehicle = getCurrentVehicle();
        if (vehicle != null) {
            double speedMultiplier = vehicle.getSpeed() / 100.0;
            double handlingMultiplier = vehicle.getHandling() / 100.0;
            double reliabilityMultiplier = vehicle.getReliability() / 100.0;
            double fuelEconomyMultiplier = vehicle.getFuelEconomy() / 100.0;
            double difficultyPenalty = difficultyLevel.getRaceDifficultyMultiplier();

            //THESE PHYSICS NEED TO BE PASSED TO 3D CONTROLLER
        }
    }

    public void initializeDefaults() {
        if (player == null) {
            player = new Player("Default Player", 10000);
        }
        if (difficultyLevel == null) {
            difficultyLevel = Difficulty.MEDIUM;
        }
    }

    //random even handling in here
}
