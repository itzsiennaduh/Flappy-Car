package seng201.team124.gui.startingMenus;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import seng201.team124.gui.importantControllers.GameController;
import seng201.team124.models.raceLogic.Difficulty;
import seng201.team124.services.GameManager;

import java.io.IOException;

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
    @FXML
    Button continueButton;

    private final int MIN_SEASON_LENGTH = 5;
    private final int MAX_SEASON_LENGTH = 15;

    @FXML
    public void initialize() {
        //difficulty initialisation why is javafx AMERICAN


        difficultySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateDifficultyLabel(newVal.intValue());


        });

        seasonLengthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            GameManager.getInstance().setSeasonLength((int) newVal.intValue());
            System.out.println(GameManager.getInstance().getSeasonLength());
            updateSeasonLengthLabel(newVal.intValue());

        });

        errorMessageLabel.setText("");
        errorMessageLabel.setStyle("-fx-text-fill: red;");
    }


    private void updateDifficultyLabel(int value) {
        GameManager.getInstance().setSeasonLength(value);
        Difficulty difficulty = Difficulty.values()[value - 1]; //convert 1-3 to enum
        difficultyLabel.setText("Difficulty: " + difficulty.toString());

        String description = switch (difficulty) {
            case EASY -> "More starting money, less random events, easier races.";
            case MEDIUM -> "A nice balance, how the game is usually played.";
            case HARD -> "Less starting money, more random events, harder races (but so much more fun!).";
        };

        difficultyLabel.setText(difficultyLabel.getText() + " - " + description);
        GameManager.getInstance().setSeasonLength(value);

    }

    private void updateSeasonLengthLabel(int value) {
        GameManager.getInstance().setSeasonLength(value);
        seasonLengthLabel.setText("Season Length: " + value + " races");

    }


    @FXML
    private void handleContinue() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartShop.fxml"));
        Parent root = loader.load();
        Scene currentScene = difficultySlider.getScene();
        currentScene.setRoot(root);
        Stage stage = (Stage) currentScene.getWindow();
        stage.setFullScreen(true);
    }
}
