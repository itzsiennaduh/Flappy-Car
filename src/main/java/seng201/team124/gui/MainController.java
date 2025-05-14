package seng201.team124.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng201.team124.services.CounterService;
import seng201.team124.services.GameManager;

import java.io.IOException;

/**
 * Controller for the main.fxml window
 * @author seng201 teaching team
 */
public class MainController {

    @FXML
    private Label defaultLabel;

    @FXML
    private Button defaultButton;

    @FXML
    private Button Gamebutton;

    private CounterService counterService;

    /**
     * Initialise the window
     *
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        counterService = new CounterService();
    }

    /**
     * Method to call when our counter-button is clicked
     *
     */

    @FXML
    protected void Exit() { //The name for the On Action

        Platform.exit(); // A better way to exit the application
    }


    @FXML
    protected void NewGame(ActionEvent event) { // Action even gives info about the click (wtf does that mean), throws IOException if it can't find the file
        try {
            GameManager.getInstance().initialiseDefaults();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            GameController game = new GameController();
            game.setupGameScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
            defaultLabel.setText("Error loading game");
        }
    }

    @FXML
    protected void CharacterName(ActionEvent event) throws IOException {

//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CharacterNameLayout.fxml"));
//        stage.setScene(new Scene(loader.load()));
//        stage.setFullScreen(true);
//        stage.show();

        Parent root = loader.load();
        Stage currentStage = (Stage) defaultButton.getScene().getWindow();
        Scene newScene = new Scene(root);
        currentStage.setScene(newScene);
        currentStage.setFullScreen(true);
        currentStage.show();


    }


    @FXML
    public void onButtonClicked() {
        System.out.println("Button has been clicked");
        counterService.incrementCounter();

        int count = counterService.getCurrentCount();
        defaultLabel.setText(Integer.toString(count));
    }
}
