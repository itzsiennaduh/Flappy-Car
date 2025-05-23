package seng201.team124.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team124.gui.importantControllers.GameController;
import seng201.team124.models.Player;
import seng201.team124.models.raceLogic.Difficulty;
import seng201.team124.models.raceLogic.Race;
import seng201.team124.models.raceLogic.Route;
import seng201.team124.services.GameManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {

    private GameManager gameManager;

    @BeforeEach
    public void setUp() {
        gameManager = new GameManager();
    }

    @Test
    public void setGameControllerSetsControllerCorrectly() {
        GameController controller = new GameController();
        gameManager.setGameController(controller);
        assertNotNull(controller);
    }

    @Test
    public void setSeasonLengthUpdatesSeasonLength() {
        gameManager.initializeWithDefaults();
        gameManager.setSeasonLength(15);

        assertEquals(15, gameManager.getSeasonLength());
    }

    @Test
    public void isRaceInProgressReturnsCorrectValue() {
        assertFalse(gameManager.isRaceInProgress());
    }

    @Test
    public void setTempNameUpdatesTempName() {
        String tempName = "Temporary Player";

        gameManager.setTempName(tempName);

        assertEquals(tempName, gameManager.getTempName());
    }

    @Test
    public void clearTempDataResetsTempName() {
        gameManager.setTempName("Temporary Player");
        gameManager.clearTempData();

        assertNull(gameManager.getTempName());
    }

    @Test
    public void getRemainingRacesCalculatesCorrectly() {
        gameManager.initializeGame("Player", Difficulty.MEDIUM, 10);

        assertEquals(10, gameManager.getRemainingRaces());
    }

    @Test
    public void isSeasonCompleteChecksConditionCorrectly() {
        gameManager.initializeGame("Player", Difficulty.MEDIUM, 5);

        assertFalse(gameManager.isSeasonComplete());
    }

    @Test
    public void playerCanContinueReturnsTrueIfPlayerIsAbleToProceed() {
        gameManager.initializeWithDefaults();

        assertTrue(gameManager.playerCanContinue());
    }

    @Test
    public void getSelectedRaceReturnsNullIfNotSet() {
        assertNull(gameManager.getSelectedRace());
    }

    @Test
    public void setAndGetSelectedRaceUpdatesRaceCorrectly() {
        Route route = new Route("Route1", 100, 1, 3, 1.2, 0.8, 0.9, 0.7);
        List<Route> routeList = List.of(route);
        Race race = new Race("Race1", 4, routeList, 12, 10000, "url");
        gameManager.setSelectedRace(race);

        assertEquals(race, gameManager.getSelectedRace());
    }
}