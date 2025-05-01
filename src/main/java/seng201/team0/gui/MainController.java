package seng201.team0.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng201.team0.services.CounterService;
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

    private CounterService counterService;

    /**
     * Initialize the window
     *
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        counterService = new CounterService();
    }

    /**
     * Method to call when our counter button is clicked
     *
     */

    @FXML
    protected void Exit() { //The name for the On Action

        Platform.exit(); // A better way to exit the application
    }


    @FXML
    protected void NewGame(ActionEvent event) throws IOException { // Action even gives info about the click (wtf does that mean), throws IOException if it can't find the file

        GameController game = new GameController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        game.setupGameScene(stage);

    }


    @FXML
    public void onButtonClicked() {
        System.out.println("Button has been clicked");
        counterService.incrementCounter();

        int count = counterService.getCurrentCount();
        defaultLabel.setText(Integer.toString(count));
    }
}
