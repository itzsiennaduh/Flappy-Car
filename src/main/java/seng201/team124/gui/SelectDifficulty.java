package seng201.team124.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import seng201.team124.services.Stored_Variables;

public class SelectDifficulty {

    @FXML
    private Slider difficulty;

    @FXML
    private Label difficultyLabel;

    @FXML
    private Slider SeasonL;

    @FXML
    public void initialize() {
        updateDifficultyLabel(difficulty.getValue());

        difficulty.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateDifficultyLabel(newVal.doubleValue());
        });
    }

    private void updateDifficultyLabel(double value) {
        String text;
        switch ((int) value) {
            case 1: text = "Easy"; break;
            case 2: text = "Medium"; break;
            case 3: text = "Hard"; break;
            default: text = "Unknown";
        }
        difficultyLabel.setText("Difficulty: " + text);
        Stored_Variables.setDifficulty((int) value); // optional
    }



    @FXML
    private void handleStartGame() {
        System.out.println("Game starting with difficulty: " + Stored_Variables.getDifficulty());
        // You can load the game scene here
    }
}
