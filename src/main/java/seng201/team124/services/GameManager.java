package seng201.team124.services;

import seng201.team124.factories.VehicleFactory;
import seng201.team124.models.*;
import seng201.team124.models.racelogic.Difficulty;
import seng201.team124.models.racelogic.Race;
import seng201.team124.models.racelogic.Route;
import seng201.team124.models.vehicleutility.Shop;
import seng201.team124.models.vehicleutility.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * class to manage the game state
 */
public class GameManager {
    private static GameManager instance;

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

    public String player_model;

    private final List<String> tracks = new ArrayList<>();

    private GameManager() {
        loadTrack();
    }

    private void loadTrack(){
        tracks.add("Zwei Hockenheimring");
    }

    public static synchronized GameManager getInstance() { //AI taught me what synchronized does
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }


    public List<String> tracks() {
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
     * gets current player info
     *
     * @return player info
     */
    public Player getPlayer() {
        if (player == null) {
            initializeDefaults();
        }
        return this.player;
    }

    /**
     * gets the name of the current player
     *
     * @return the player's name as a string
     */
    public String getPlayerName() {
        return getPlayer().getName();
    }

    /**
     * gets the selected difficulty level
     *
     * @return selected difficulty level
     */
    public Difficulty getDifficultyLevel() {
        return this.difficulty;
    }

    /**
     * get selected season length
     *
     * @return selected season length
     */
    public int getSeasonLength() {
        return this.seasonLength;
    }

    /**
     * is the race in progress?
     *
     * @return true if so, false otherwise
     */
    public boolean isRaceInProgress() {
        return this.isRaceInProgress;
    }

    /**
     * access service shop
     *
     * @return shop service
     */
    public ShopService getShopService() {
        if (shopService == null) initializeDefaults();
        return this.shopService;
    }

    /**
     * access service garage
     *
     * @return garage service
     */
    public GarageService getGarageService() {
        if (garageService == null) initializeDefaults();
        return this.garageService;
    }

    /**
     * access service race
     *
     * @return race service
     */
    public RaceService getRaceService() {
        if (raceService == null) initializeDefaults();
        return this.raceService;
    }

    /**
     * access time counter service
     *
     * @return counter service
     */
    public CounterService getCounterService() {
        if (counterService == null) initializeDefaults();
        return this.counterService;
    }

    public void setTempName(String name) {
        this.tempName = name;
    }

    public String getTempName() {
        return this.tempName;
    }

    public void clearTempData() {
        this.tempName = null;
    }

    public void startRace(Race race, Route route) {
        getRaceService().startRace(race, route);
        this.isRaceInProgress = true;
    }

    /**
     * completes the current race and updates game state
     *
     * @param position final race position (1=first place, 2=second place, etc.)
     */
    public void completeRace(int position) {
        getRaceService().completeRace(position);
        isRaceInProgress = false;
        getShopService().restockShop();
    }

    public int getRacesCompleted() {
        return getRaceService().getCompletedRacesCount();
    }

    public boolean isSeasonComplete() {
        return getRacesCompleted() >= getSeasonLength();
    }

    public int getRemainingRaces() {
        return getSeasonLength() - getRacesCompleted();
    }

    public void restockShop() {
        getShopService().restockShop();
    }

    public String gettingModel() {return player_model;}

    private void validatePlayerName(String name) {
        if (name == null || name.trim().length() < 3 || name.trim().length() > 15) {
            throw new IllegalArgumentException("Player name must be between 3 and 15 characters long.");
        }
    }

    public Vehicle getCurrentVehicle() {
        return player.getCurrentVehicle();
    }

    public void initializeDefaults() {
        this.player = new Player("Default Player", 10000);
        this.difficulty = Difficulty.MEDIUM;
        this.seasonLength = 10;
        this.shopService = new ShopService(new Shop(), this.player, player.getVehicles(), player.getTuningParts());
        this.garageService = new GarageService(this.player);
        this.counterService = new CounterService();
        this.raceService = new RaceService(this.player, this, this.counterService);
        this.player_model = "/assets/models/Cars/Supra.obj";
        Vehicle defaultVehicle = VehicleFactory.createRedVehicle();
        player.getVehicles().add(defaultVehicle);
        player.setCurrentVehicle(defaultVehicle);
        this.player_model = "/assets/models/Cars/Supra.obj";
    }


    //random event handling in here



}
