package seng201.team124.gui.startingMenus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.services.GameManager;

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
    @FXML
    private Label confirmLabel;
    private Vehicle selectedVehicle;
    @FXML private ImageView stack;

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
        String img = vehicle.getPreviewImagePath();

        stack.setStyle(
                "-fx-background-image: url('/FXML/Images/mainmenu.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center center;" +
                        "-fx-background-repeat: no-repeat;"
        );

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
        gm.getPlayer().addVehicle(selectedVehicle);
        gm.getPlayer().setCurrentVehicle(selectedVehicle);

        confirmLabel.setText("Purchased " + selectedVehicle.getName() + "!" + "\nPlease continue to the next screen.");
        confirmLabel.setVisible(true);
        confirmButton.setVisible(false);
        continueButton.setVisible(true);

    }

    @FXML
    private void handleContinue() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            Scene currentScene = continueButton.getScene();
            currentScene.setRoot(root);
            Stage stage = (Stage) currentScene.getWindow();
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}