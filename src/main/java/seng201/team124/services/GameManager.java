package seng201.team124.services;

import javafx.application.Platform;
import seng201.team124.factories.RaceFactory;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.gui.importantControllers.GameController;
import seng201.team124.models.Player;
import seng201.team124.models.raceLogic.Difficulty;
import seng201.team124.models.raceLogic.Race;
import seng201.team124.models.raceLogic.Route;
import seng201.team124.models.vehicleUtility.Shop;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * class to manage the game state
 */
public class GameManager {
    private static GameManager instance;
    private GameController gameController;

    private Player player;
    private Difficulty difficulty;
    private int seasonLength;

    private ShopService shopService;
    private GarageService garageService;
    private RaceService raceService;
    private CounterService counterService;

    private boolean isRaceInProgress = false;

    private static final double BASE_STARTING_MONEY = 10000;

    private String tempName;

    private final List<Race> tracks = new ArrayList<>();

    private Race selectedRace;


    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * constructor for singleton class
     */
    private GameManager() {
        loadTrack();
    }

    /**
     * loads available tracks
     */
    private void loadTrack(){
    }

    public List<Race> getAvailableRaces() {
        return RaceFactory.getRaces();
    }

    private void getTrack(Race track){
        tracks.add(track);
    }

    public void setSelectedRace(Race race) {
        this.selectedRace = race;
    }

    public Race getSelectedRace() {
        return this.selectedRace;
    }
    /**
     * gets the singeton instance of the game manager
     * @return the game manager instance, or creates a new one if it doesn't exist yet.
     */
    public static synchronized GameManager getInstance() { //AI taught me what synchronized does
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * gets a list of available tracks
     * @return unmodifiable list of available tracks.
     */
    public List<Race> tracks() {
        return Collections.unmodifiableList(tracks);
    }

    /**
     * initialises a new game
     *
     * @param playerName   name of the player
     * @param difficulty   selected difficulty level of the game
     * @param seasonLength number of races in the season
     */
    public void initializeGame(String playerName, Difficulty difficulty, int seasonLength) {
        validatePlayerName(playerName);

        double startingMoney = difficulty.calculateStartingMoney(BASE_STARTING_MONEY);
        this.player = new Player(playerName, startingMoney);
        this.difficulty = difficulty;
        this.seasonLength = seasonLength;

        this.shopService = new ShopService(new Shop(), this.player, player.getVehicles(), player.getTuningParts());
        this.garageService = new GarageService(this.player);
        this.counterService = new CounterService();
        this.raceService = new RaceService(this.player, this, this.counterService);
    }

    /**
     * initialises a new game with default values if not already initialised
     */
    public void initializeWithDefaults() {
        initializeGame("Default Player", Difficulty.MEDIUM, 20000);
        Vehicle defaultVehicle = VehicleFactory.createRedVehicle();
        player.getVehicles().add(defaultVehicle);
        player.setCurrentVehicle(defaultVehicle);
    }

    /**
     * gets current player info
     *
     * @return player info
     */
    public Player getPlayer() {
        if (player == null) {
            initializeWithDefaults();
        }
        return this.player;
    }

    /**
     * gets the name of the current player
     * @return the player's name as a string
     */
    public String getPlayerName() {
        return getPlayer().getName();
    }



    /**
     * gets the selected difficulty level
     * @return selected difficulty level
     */
    public Difficulty getDifficultyLevel() {
        if (difficulty == null) {
            getPlayer();
        }
        return this.difficulty;
    }

    public void setSeasonLength(int seasonLength) {
        this.seasonLength = seasonLength;
    }
    /**
     * get selected season length
     * @return selected season length
     */
    public int getSeasonLength() {
        if (player == null) {
            getPlayer();
        }
        return this.seasonLength;
    }



    public double getTimeLimit() {
        return getRaceService().getTotalRaceHours();
    }

    /**
     * is the race in progress?
     * @return true if so, false otherwise
     */
    public boolean isRaceInProgress() {
        return this.isRaceInProgress;
    }

    /**
     * access service shop
     * @return shop service
     */
    public ShopService getShopService() {
        if (shopService == null) getPlayer();
        return this.shopService;
    }

    /**
     * access service garage
     * @return garage service
     */
    public GarageService getGarageService() {
        if (garageService == null) getPlayer();
        return this.garageService;
    }

    /**
     * access service race
     * @return race service
     */
    public RaceService getRaceService() {
        if (raceService == null) getPlayer();
        return this.raceService;
    }

    /**
     * access time counter service
     * @return counter service
     */
    public CounterService getCounterService() {
        if (counterService == null) getPlayer();
        return this.counterService;
    }

    public List<TuningParts> getTuningParts() {
        return getPlayer().getTuningParts();
    }


    public List<Vehicle> getVehicles() {
        return getPlayer().getVehicles();
    }
    /**
     * sets a temporary player name during setup
     * @param name the temp player name
     */
    public void setTempName(String name) {
        this.tempName = name;
    }

    /**
     * gets the temporary player name
     * @return temp player name
     */
    public String getTempName() {
        return this.tempName;
    }

    /**
     * clears the temporary player name
     * should be called after the player has been created
     */
    public void clearTempData() {
        this.tempName = null;
    }

    /**
     * sets the race in progress, pass over to race service
     * @param race the race to start
     * @param route the route to take
     */
    public void startRace(Race race, Route route) {
        this.isRaceInProgress = true;
        getRaceService().startRace(race, route);
    }

    /**
     * completes the current race and updates game state
     *
     * @param position final race position (1=first place, 2=second place, etc.)
     */
    public void completeRace(int position) {
        isRaceInProgress = false;
        getRaceService().completeRace(position);
        getShopService().restockShop();
    }

    public void onRaceCompleted() {
        checkGameEndConditions();
    }

    /**
     * checks if the season has been completed
     * @return true if season complete, false otherwise
     */
    public boolean isSeasonComplete() {
        return getRaceService().getCompletedRacesCount() >= getSeasonLength();
    }

    /**
     * gets number of races remaining in the season
     * @return count of remaining races
     */
    public int getRemainingRaces() {
        return getSeasonLength() - getRaceService().getCompletedRacesCount();
    }

    /**
     * restocks the shop
     */
    public void restockShop() {
        getShopService().restockShop();
    }


    /**
     * validates a player name
     * @param name the player name to validate
     * @throws IllegalArgumentException if the name is invalid
     */
    private void validatePlayerName(String name) {
        if (name == null || name.trim().length() < 3 || name.trim().length() > 15) {
            throw new IllegalArgumentException("Player name must be between 3 and 15 characters long.");
        }
    }

    /**
     * gets the current vehicle
     * @return current vehicle
     */
    public Vehicle getCurrentVehicle() {
        return player.getCurrentVehicle();
    }

    public void checkGameEndConditions() {
        boolean gameWon = player.hasCompletedSeason();
        boolean gameLost = player.isBrokeAndUnrepairable();

        if (gameWon || gameLost) {
            Platform.runLater(() -> {
                if (gameController != null) {
                    gameController.showGameEndScreen(
                            player.getName(),
                            getSeasonLength(),
                            player.getRacesCompleted(),
                            player.getAveragePlacing(),
                            player.getTotalWinnings(),
                            gameWon
                    );
                }
            });
        }
    }

    public boolean playerCanContinue() {
        return !getPlayer().isBrokeAndUnrepairable() || isRaceInProgress() || !isSeasonComplete();
    }



}
