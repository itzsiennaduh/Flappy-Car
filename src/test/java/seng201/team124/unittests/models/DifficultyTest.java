package seng201.team124.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seng201.team124.models.raceLogic.Difficulty;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DifficultyTest")
class DifficultyTest {

    private Difficulty easy;
    private Difficulty medium;
    private Difficulty hard;

    @BeforeEach
    void setUp() {
        easy = Difficulty.EASY;
        medium = Difficulty.MEDIUM;
        hard = Difficulty.HARD;
    }

    @Test
    @DisplayName("Easy money multiplier is correct")
    void easyMoneyMultiplierIsCorrect() {
        assertEquals(1.5, easy.getMoneyMultiplier(), "Easy money multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Medium money multiplier is correct")
    void mediumMoneyMultiplierIsCorrect() {
        assertEquals(1.0, medium.getMoneyMultiplier(), "Medium money multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Hard money multiplier is correct")
    void hardMoneyMultiplierIsCorrect() {
        assertEquals(0.5, hard.getMoneyMultiplier(), "Hard money multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Easy event chance multiplier is correct")
    void easyEventChanceMultiplierIsCorrect() {
        assertEquals(0.7, easy.getEventChanceMultiplier(), "Easy event chance multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Medium event chance multiplier is correct")
    void mediumEventChanceMultiplierIsCorrect() {
        assertEquals(1.0, medium.getEventChanceMultiplier(), "Medium event chance multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Hard event chance multiplier is correct")
    void hardEventChanceMultiplierIsCorrect() {
        assertEquals(1.3, hard.getEventChanceMultiplier(), "Hard event chance multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Easy race difficulty multiplier is correct")
    void easyRaceDifficultyMultiplierIsCorrect() {
        assertEquals(0.5, easy.getRaceDifficultyMultiplier(), "Easy race difficulty multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Medium race difficulty multiplier is correct")
    void mediumRaceDifficultyMultiplierIsCorrect() {
        assertEquals(1.0, medium.getRaceDifficultyMultiplier(), "Medium race difficulty multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Hard race difficulty multiplier is correct")
    void hardRaceDifficultyMultiplierIsCorrect() {
        assertEquals(1.5, hard.getRaceDifficultyMultiplier(), "Hard race difficulty multiplier did not match the expected value.");
    }

    @Test
    @DisplayName("Easy difficulty calculates correct starting money")
    void easyDifficultyCalculatesCorrectStartingMoney() {
        double baseMoney = 100.0;
        double expected = 150.0; // 100 * 1.5
        double actual = easy.calculateStartingMoney(baseMoney);
        assertEquals(expected, actual, "Easy difficulty did not calculate the correct starting money.");
    }

    @Test
    @DisplayName("Medium difficulty calculates correct starting money")
    void mediumDifficultyCalculatesCorrectStartingMoney() {
        double baseMoney = 100.0;
        double expected = 100.0; // 100 * 1.0
        double actual = medium.calculateStartingMoney(baseMoney);
        assertEquals(expected, actual, "Medium difficulty did not calculate the correct starting money.");
    }

    @Test
    @DisplayName("Hard difficulty calculates correct starting money")
    void hardDifficultyCalculatesCorrectStartingMoney() {
        double baseMoney = 100.0;
        double expected = 50.0; // 100 * 0.5
        double actual = hard.calculateStartingMoney(baseMoney);
        assertEquals(expected, actual, "Hard difficulty did not calculate the correct starting money.");
    }
}