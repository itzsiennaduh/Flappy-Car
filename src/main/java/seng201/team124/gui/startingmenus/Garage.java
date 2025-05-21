package seng201.team124.gui.startingmenus;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import seng201.team124.models.vehicleutility.TuningParts;
import seng201.team124.services.GameManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Garage {

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<String> DropdownEngine;

    @FXML
    private ChoiceBox<String> DropdownNitris;

    @FXML
    private ChoiceBox<String> DropdownWheels;

    @FXML
    public void initialize() {

        List<TuningParts> allParts = GameManager.getInstance().getTuningParts();

        List<String> engineNames = allParts.stream()
                .filter(p -> p.getName().equals("Turbocharger")
                        || p.getName().equals("Supercharger"))
                .map(TuningParts::getName)
                .collect(Collectors.toList());

        List<String> nitrousNames = allParts.stream()
                .filter(p -> p.getName().equals("Nitrous Oxide"))
                .map(TuningParts::getName)
                .collect(Collectors.toList());

        List<String> wheelNames = allParts.stream()
                .filter(p -> p.getName().endsWith("Tires"))
                .map(TuningParts::getName)
                .collect(Collectors.toList());

        DropdownEngine.setItems(FXCollections.observableArrayList(engineNames));
        DropdownNitris.setItems(FXCollections.observableArrayList(nitrousNames));
        DropdownWheels.setItems(FXCollections.observableArrayList(wheelNames));




    }


    @FXML
    private void handleBackButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        Parent root = loader.load();
        Scene currentScene = backButton.getScene();
        currentScene.setRoot(root);
        Stage stage = (Stage) currentScene.getWindow();
        stage.setFullScreen(true);
    }
}
