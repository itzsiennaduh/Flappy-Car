package seng201.team124.gui.startingMenus;

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
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.services.GameManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GarageController {

    @FXML private Button backButton;
    @FXML private ChoiceBox<String> DropdownEngine;
    @FXML private ChoiceBox<String> DropdownNitris;
    @FXML private ChoiceBox<String> DropdownWheels;
    @FXML private ChoiceBox<String> DropdownCars;
    @FXML private Button installEngineButton;
    @FXML private Button installNitroButton;
    @FXML private Button installWheelsButton;
    @FXML private Label installFeedbackLabel;

    private Player player;
    private Vehicle currentVehicle;
    private List<TuningParts> inventory;
    private List<Vehicle> allVehicles;

    @FXML
    public void initialize() {
        player = GameManager.getInstance().getPlayer();
        currentVehicle = player.getCurrentVehicle();
        allVehicles = player.getVehicles();
        refreshAllDropdowns();

        DropdownCars.setItems(FXCollections.observableArrayList(allVehicles.stream().map(Vehicle::getName).collect(Collectors.toList())));

        DropdownCars.getSelectionModel().selectedIndexProperty().addListener((obs, oldI, newI) -> {
            if (newI.intValue() >= 0) {
                currentVehicle = allVehicles.get(newI.intValue());
                player.setCurrentVehicle(currentVehicle);

            }
        });

        DropdownCars.getSelectionModel().select(allVehicles.indexOf(currentVehicle));

        installEngineButton.setOnAction(e -> swapPart(TuningParts.Type.ENGINE, DropdownEngine));
        installNitroButton .setOnAction(e -> swapPart(TuningParts.Type.NITRO,  DropdownNitris));
        installWheelsButton.setOnAction(e -> swapPart(TuningParts.Type.WHEEL, DropdownWheels));

        backButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
                Parent root = loader.load();
                Scene scene = backButton.getScene();
                scene.setRoot(root);
                ((Stage) scene.getWindow()).setFullScreen(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private void refreshAllDropdowns() {
        inventory = player.getTuningParts();
        populateDropdown(TuningParts.Type.ENGINE, DropdownEngine, currentVehicle.getInstalledEngine());
        populateDropdown(TuningParts.Type.NITRO,  DropdownNitris, currentVehicle.getInstalledNitro());
        populateDropdown(TuningParts.Type.WHEEL, DropdownWheels, currentVehicle.getInstalledWheels());
    }


    private void populateDropdown(TuningParts.Type slotType,
                                  ChoiceBox<String> dropdown,
                                  TuningParts installedPart) {
        String installedName = (installedPart != null) ? installedPart.getName() : "Install Part";
        List<String> names = new ArrayList<>();
        names.add(installedName);
        names.addAll(
                inventory.stream()
                        .filter(p -> p.getType() == slotType)
                        .map(TuningParts::getName)
                        .collect(Collectors.toList())
        );
        dropdown.setItems(FXCollections.observableArrayList(names));
        dropdown.setValue(installedName);
    }


    private void swapPart(TuningParts.Type slotType, ChoiceBox<String> dropdown) {
        String selected = dropdown.getValue();
        installFeedbackLabel.setText("");

        // if nothing chosen
        if (selected == null || selected.equals("Install Part")) {
            installFeedbackLabel.setText("Please select a part to install.");
            return;
        }

        // find new TuningParts by name
        Optional<TuningParts> newOpt = inventory.stream()
                .filter(p -> p.getName().equals(selected) && p.getType() == slotType)
                .findFirst();
        TuningParts newPart = newOpt.orElse(null);

        // get currently installed
        TuningParts oldPart = switch (slotType) {
            case ENGINE -> currentVehicle.getInstalledEngine();
            case NITRO  -> currentVehicle.getInstalledNitro();
            case BODY -> null;
            case WHEEL -> currentVehicle.getInstalledWheels();
        };

        // if re-selecting same
        if (newPart != null && newPart.equals(oldPart)) {
            installFeedbackLabel.setText("That part is already installed.");
            return;
        }

        // uninstall old
        if (oldPart != null) {
            player.uninstallTuningPart(oldPart, currentVehicle);
        }

        // install new if present
        String msg;
        if (newPart != null) {
            msg = player.installTuningPart(newPart, currentVehicle);
        } else {
            msg = "No such part in inventory.";
        }
        installFeedbackLabel.setText(msg);

        // refresh dropdowns
        refreshAllDropdowns();
    }
}
