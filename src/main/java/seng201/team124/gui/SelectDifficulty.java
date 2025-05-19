package seng201.team124.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import seng201.team124.models.*;
import seng201.team124.services.GameManager;

public class SelectDifficulty {
    @FXML
    private Slider difficultySlider;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Slider seasonLengthSlider;
    @FXML
    private Label seasonLengthLabel;
    @FXML
    private Label errorMessageLabel;

    private final int MIN_SEASON_LENGTH = 5;
    private final int MAX_SEASON_LENGTH = 15;

    @FXML
    public void initialize() {
        //difficulty initialisation why is javafx AMERICAN
        difficultySlider.setSnapToTicks(true);
        difficultySlider.setBlockIncrement(1);
        difficultySlider.setMajorTickUnit(1);
        difficultySlider.setMinorTickCount(0);
        difficultySlider.setMin(1);
        difficultySlider.setMax(3);

        //initialise season length slider
        seasonLengthSlider.setSnapToTicks(true);
        seasonLengthSlider.setBlockIncrement(1);
        seasonLengthSlider.setMin(MIN_SEASON_LENGTH);
        seasonLengthSlider.setMax(MAX_SEASON_LENGTH);

        updateDifficultyLabel((int) difficultySlider.getValue());
        updateSeasonLengthLabel((int) seasonLengthSlider.getValue());

        difficultySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateDifficultyLabel(newVal.intValue());
        });

        seasonLengthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSeasonLengthLabel(newVal.intValue());
        });

        errorMessageLabel.setText("");
        errorMessageLabel.setStyle("-fx-text-fill: red;");
    }

    private void updateDifficultyLabel(int value) {
        Difficulty difficulty = Difficulty.values()[value - 1]; //convert 1-3 to enum
        difficultyLabel.setText("Difficulty: " + difficulty.toString());

        String description = switch (difficulty) {
            case EASY -> "More starting money, less random events, easier races.";
            case MEDIUM -> "A nice balance, how the game is usually played.";
            case HARD -> "Less starting money, more random events, harder races (but so much more fun!).";
        };

        difficultyLabel.setText(difficultyLabel.getText() + " - " + description);
    }

    private void updateSeasonLengthLabel(int value) {
        seasonLengthLabel.setText("Season Length: " + value + " races");
    }

    @FXML
    private void handleStartGame() {
        int difficultyValue = (int)difficultySlider.getValue();
        Difficulty difficulty = Difficulty.values()[difficultyValue - 1];
        int seasonLength = (int)seasonLengthSlider.getValue();

        GameManager.getInstance().initializeGame(
                GameManager.getInstance().getPlayerName(),
                difficulty,
                seasonLength
        );

        //GameManager gameManager = GameManager.getInstance();
        //gameManager.initializeGame(
                //gameManager.getPlayerName(),
                //difficulty,
                //seasonLength
        //);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameScene.fxml"));
            Parent root = loader.load();

            GameController gameController = loader.getController();
            //gameController.setupGameScene((Stage)difficultySlider.getScene().getWindow());

            Stage stage = (Stage) difficultySlider.getScene().getWindow();
            //stage.setScene(new Scene(root));
            //stage.setFullScreen(true);
            //stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
