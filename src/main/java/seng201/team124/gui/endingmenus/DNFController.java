package seng201.team124.gui.endingMenus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DNFController {
    @FXML
    private Button returnToRaceSelection;
    @FXML
    private Label reasonLabel;

    private String reasonForNotFinishing = "Unknown Reason >:(";

    @FXML
    public void initialize() {
        if (reasonLabel != null) {
            reasonLabel.setText(reasonForNotFinishing);
        }
        returnToRaceSelection.setOnAction(e -> {returnToRaceSelection();});
    }

    private void returnToRaceSelection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent mainMenuRoot = loader.load();

            Scene currentScene = returnToRaceSelection.getScene();
            currentScene.setRoot(mainMenuRoot);

            Stage stage = (Stage) currentScene.getWindow();
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getReason() {
        return reasonForNotFinishing;
    }

    public void setReason(String reason) {
        this.reasonForNotFinishing = reason;
        if (reasonLabel != null) {
            reasonLabel.setText(reasonForNotFinishing);
        }
    }




}
