package seng201.team124.gui.importantControllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Controller for main.fxml, handling navigation and loading screen.
 */
public class MainController {
    @FXML private Button startGameButton;

    /**
     * Exit the application.
     */

    private Scene scene;

    @FXML
    protected void Exit() {
        Platform.exit();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Navigate to character naming screen.
     */
    @FXML
    protected void CharacterName(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/CharacterNameLayout.fxml")
        );
        Parent root = loader.load();
        scene.setRoot(root);

    }

}