package seng201.team124.gui.startingmenus;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private ListView<Vehicle> vehicleListView;
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
        List<Vehicle> allVehicles = List.of(
                VehicleFactory.createRedVehicle(),
                VehicleFactory.createCatVehicle(),
                VehicleFactory.createHorseChariot()
        );

        vehicleListView.getItems().addAll(allVehicles);

        vehicleListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Vehicle> call(ListView<Vehicle> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Vehicle item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName() + " ($" + item.getCost() + ")");
                        }
                    }
                };
            }
        });

        vehicleListView.setOnMouseClicked(event -> {
            Vehicle vehicle = vehicleListView.getSelectionModel().getSelectedItem();
            if (vehicle != null) {
                displayVehicle(vehicle);
                selectedVehicle = vehicle;
                confirmButton.setDisable(false);
            }
        });

        confirmButton.setOnAction(event -> handleConfirmPurchase());
        confirmButton.setDisable(true);
    }

    private void displayVehicle(Vehicle vehicle) {
        nameLabel.setText(vehicle.getName());
        descriptionLabel.setText(vehicle.getDescription());
        costLabel.setText("$" + vehicle.getCost());

        speedBar.setProgress(vehicle.getSpeed() / 10.0);
        handlingBar.setProgress(vehicle.getHandling() / 10.0);
        reliabilityBar.setProgress(vehicle.getReliability() / 10.0);
        fuelEcoBar.setProgress(vehicle.getFuelEconomy() / 10.0);
    }

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
    private void movingon(){
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