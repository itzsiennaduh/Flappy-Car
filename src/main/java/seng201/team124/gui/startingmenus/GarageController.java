package seng201.team124.gui.startingmenus;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seng201.team124.models.Player;
import seng201.team124.models.vehicleutility.TuningParts;
import seng201.team124.models.vehicleutility.Vehicle;
import seng201.team124.services.GameManager;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GarageController {

    @FXML private Button backButton;
    @FXML private ChoiceBox<String> DropdownEngine;
    @FXML private ChoiceBox<String> DropdownNitris;
    @FXML private ChoiceBox<String> DropdownWheels;
    @FXML private Button installEngineButton;
    @FXML private Button installNitroButton;
    @FXML private Button installWheelsButton;
    @FXML private Label installFeedbackLabel;

    private Player player;
    private Vehicle currentVehicle;
    private List<TuningParts> inventory;

    @FXML
    public void initialize() {

        player = GameManager.getInstance().getPlayer();
        currentVehicle = player.getCurrentVehicle();
        reloadInventory();


        DropdownEngine.setItems(FXCollections.observableArrayList(
                inventory.stream()
                        .filter(p -> p.getName().equals("Turbocharger") || p.getName().equals("Supercharger"))
                        .map(TuningParts::getName)
                        .collect(Collectors.toList())
        ));

        DropdownNitris.setItems(FXCollections.observableArrayList(
                inventory.stream()
                        .filter(p -> p.getName().equals("Nitrous Oxide"))
                        .map(TuningParts::getName)
                        .collect(Collectors.toList())
        ));

        DropdownWheels.setItems(FXCollections.observableArrayList(
                inventory.stream()
                        .filter(p -> p.getName().endsWith("Tires"))
                        .map(TuningParts::getName)
                        .collect(Collectors.toList())
        ));

        installEngineButton.setOnAction(e -> installPart(DropdownEngine));
        installNitroButton.setOnAction(e -> installPart(DropdownNitris));
        installWheelsButton.setOnAction(e -> installPart(DropdownWheels));

    }

    private void reloadInventory() {
        inventory = player.getTuningParts();
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


    private void installPart(ChoiceBox<String> dropdown) {
        String selectedName = dropdown.getValue();
        installFeedbackLabel.setText(""); // clear old feedback

        if (selectedName == null) {
            installFeedbackLabel.setText("Please select a part first.");
            return;
        }

        Optional<TuningParts> optPart = inventory.stream()
                .filter(p -> p.getName().equals(selectedName))
                .findFirst();

        if (optPart.isEmpty()) {
            installFeedbackLabel.setText("You don’t own “" + selectedName + "” anymore.");
            return;
        }

        TuningParts part = optPart.get();
        String result = player.installTuningPart(part, currentVehicle);
        installFeedbackLabel.setText(result);

        if (result.endsWith("!")) {
            reloadInventory();
            dropdown.setItems(FXCollections.observableArrayList(
                    inventory.stream()
                            .filter(p -> p.getName().equals(selectedName))
                            .map(TuningParts::getName)
                            .collect(Collectors.toList())
            ));
            dropdown.getSelectionModel().clearSelection();
        }
    }
}

