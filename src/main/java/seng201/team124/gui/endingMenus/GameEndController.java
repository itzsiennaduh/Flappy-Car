package seng201.team124.gui.endingMenus;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng201.team124.services.GameManager;
import seng201.team124.services.RaceService;

import java.io.IOException;

public class GameEndController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label seasonLengthLabel;
    @FXML
    private Label completedRacesLabel;
    @FXML
    private Label totalWinningsLabel;
    @FXML
    private Label averagePlacingLabel;

    private boolean gameWon;

    public void initializeData(String username, int seasonLength, int completedRaces, double averagePlacing, double totalWinnings, boolean gameWon) {
        this.gameWon = gameWon;
        usernameLabel.setText(gameWon ? "Well done, " + username + "!" : "Nice try, " + username + ".");

        seasonLengthLabel.setText("Season Length: " + seasonLength + " races!");
        completedRacesLabel.setText("You competed in " + completedRaces + " of those " + seasonLength + " races!");
        totalWinningsLabel.setText(String.format("You earned a total of $%.2f", totalWinnings));
        averagePlacingLabel.setText(String.format("Your average placing was %.1f", averagePlacing));
    }

    /**
     * navigates back to the initial starting menu
     */
    @FXML
    private void handlePlayAgain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

}
