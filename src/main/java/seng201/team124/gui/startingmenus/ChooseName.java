package seng201.team124.gui.startingmenus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import seng201.team124.services.GameManager;

public class ChooseName {
    @FXML
    private TextField nameField;
    @FXML
    private Label Checker;

    @FXML
    private void handleContinue() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            Checker.setText("Please enter a name");
            nameField.setStyle("-fx-border-color: red;");
            return;
        }

        if ((name.length() < 3) || (name.length() > 15)) {
            Checker.setText("The name must be at least 3 characters long and less than 15. Please try again.");
            nameField.setPromptText("At least 3 characters, but less than 15 characters.");
            nameField.setStyle("-fx-border-color: red;");
            return;
        }

        if (!name.matches("[a-zA-Z0-9]+")) {
            Checker.setText("Only letters and numbers allowed");
            nameField.setPromptText("Only letters allowed");
            nameField.setStyle("-fx-border-color: red;");
            return;
        }

        // Reset style on success
        nameField.setStyle(null);

        GameManager.getInstance().setTempName(name);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DifficultySeasonSelect.fxml"));
            Parent root = loader.load();
            Scene currentScene = nameField.getScene();
            currentScene.setRoot(root);
            Stage stage = (Stage) currentScene.getWindow();
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
