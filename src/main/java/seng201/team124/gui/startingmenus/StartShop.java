package seng201.team124.gui.startingmenus;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.models.vehicleutility.Vehicle;
import seng201.team124.services.GameManager;

import java.util.List;

public class StartShop {

    @FXML
    private VBox vehicleButtonBox;
    @FXML
    private VBox previewPane;
    @FXML
    private Label nameLabel, descriptionLabel, costLabel;
    @FXML
    private ProgressBar speedBar, handlingBar, reliabilityBar, fuelEcoBar;
    @FXML
    private Button confirmButton;
    @FXML
    private Button continueButton;
    private Vehicle selectedVehicle;

    @FXML
    private void initialize() {
        vehicleButtonBox.getChildren().clear();

        for (Vehicle vehicle : VehicleFactory.getAllVehicles()) {
            Button button = new Button(vehicle.getName());
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(event -> showVehicleDetails(vehicle));
            vehicleButtonBox.getChildren().add(button);
        }

        previewPane.setVisible(false);
    }

    private void showVehicleDetails(Vehicle vehicle) {
        selectedVehicle = vehicle;
        previewPane.setVisible(true);

        nameLabel.setText(vehicle.getName());
        descriptionLabel.setText(vehicle.getDescription());
        costLabel.setText("Cost: $" + vehicle.getCost());

        speedBar.setProgress(vehicle.getSpeed() / 10.0);
        handlingBar.setProgress(vehicle.getHandling() / 10.0);
        reliabilityBar.setProgress(vehicle.getReliability() / 10.0);
        fuelEcoBar.setProgress(vehicle.getFuelEconomy() / 10.0);

        confirmButton.setDisable(false);
    }

    //confirmButton.setOnAction(event -> handleConfirmPurchase());

    @FXML
    private void handleConfirmPurchase() {
        if (selectedVehicle == null) return;

        GameManager gm = GameManager.getInstance();
        double startingMoney = gm.getPlayer().getMoney();

        if (selectedVehicle.getCost() > startingMoney) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You cannot afford this vehicle!");
            alert.showAndWait();
            return;
        }

        gm.getPlayer().subtractMoney(selectedVehicle.getCost());
        gm.getPlayer().getVehicles().add(selectedVehicle);
        gm.getPlayer().setCurrentVehicle(selectedVehicle);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            Scene currentScene = confirmButton.getScene();
            currentScene.setRoot(root);
            Stage stage = (Stage) currentScene.getWindow();
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void movingon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            Scene currentScene = confirmButton.getScene();
            currentScene.setRoot(root);
            Stage stage = (Stage) currentScene.getWindow();
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}