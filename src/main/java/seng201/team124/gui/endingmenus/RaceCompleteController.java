package seng201.team124.gui.endingMenus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng201.team124.gui.importantControllers.GameController;

import java.io.IOException;

public class RaceCompleteController {
    @FXML
    private Label placeLabel;
    @FXML
    private Button backToRaceSelection;

    private GameController gameController;

    @FXML
    private void initialize() {
        backToRaceSelection.setOnAction(e -> {returnToRaceSelection();});
    }

    public void setPlacement(int placement) {
        String suffix = getPlacementSuffix(placement);
        placeLabel.setText(placement + suffix + "!!!");
    }

    public void setMainMenuReference(GameController gameController) {
        this.gameController = gameController;
    }

    private void returnToRaceSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent mainMenuRoot = loader.load();

            Scene currentScene = backToRaceSelection.getScene();
            currentScene.setRoot(mainMenuRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPlacementSuffix(int placement) {
        if (placement % 10 == 1 && placement % 100 != 11) return "ST";
        if (placement % 10 == 2 && placement % 100 != 12) return "ND";
        if (placement % 10 == 3 && placement % 100 != 13) return "RD";
        return "TH";
    }


}
