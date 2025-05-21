package seng201.team124.gui.startingmenus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class Shop {

    @FXML
    private Button backButton;


    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            loader.load();
            Scene currentScene = backButton.getScene();
            currentScene.setRoot(loader.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
