package seng201.team124.gui.startingmenus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.models.vehicleutility.TuningParts;
import seng201.team124.models.vehicleutility.Vehicle;
import seng201.team124.services.GameManager;
import seng201.team124.services.ShopService;

import java.io.IOException;
import java.util.List;

import static seng201.team124.factories.TuningPartFactory.*;

public class ShopController {

    @FXML
    private Button backButton;

    @FXML
    private Button buyNitrousOxide;

    @FXML
    private Button buyTurbocharger;

    @FXML
    private Button buySupercharger;

    @FXML
    private Button buyOffRoadTires;

    @FXML
    private Button buyOnRoadTires;

    @FXML
    private Label moneyLabel;

    @FXML
    private Label noMoney;

    @FXML
    private Button buyRedVehicle;
    
    @FXML
    private Button buyCatVehicle;
    
    @FXML
    private Button buyBlueVehicle;

    private ShopService shopService;


    private final Vehicle redCar   = VehicleFactory.createRedVehicle();
    private final Vehicle catCar   = VehicleFactory.createCatVehicle();
    private final Vehicle blueCar  = VehicleFactory.createBlueVehicle();

    @FXML
    private void initialize() {
        shopService = GameManager.getInstance().getShopService();
        updateMoneyLabel();

        buyRedVehicle.setOnAction(e -> buyVehicle(redCar, buyRedVehicle));
        buyCatVehicle.setOnAction(e -> buyVehicle(catCar, buyCatVehicle));
        buyBlueVehicle.setOnAction(e -> buyVehicle(blueCar, buyBlueVehicle));


        GameManager gm = GameManager.getInstance();
        gm.getVehicles().stream()
                .map(Vehicle::getName)
                .forEach(name -> {
                    if (name.equals("Red Supra"))  buyRedVehicle.setDisable(true);
                    if (name.equals("Kittyyyyy"))    buyCatVehicle.setDisable(true);
                    if (name.equals("Blue Supra"))    buyBlueVehicle.setDisable(true);
                });


        buyNitrousOxide.setOnAction(e -> buyItemTuning(createNitrousOxide(), buyNitrousOxide));
        buyTurbocharger.setOnAction(e -> buyItemTuning(createTurbocharger(), buyTurbocharger));
        buySupercharger.setOnAction(e -> buyItemTuning(createSupercharger(), buySupercharger));
        buyOffRoadTires.setOnAction(e -> buyItemTuning(createOffRoadTires(), buyOffRoadTires));
        buyOnRoadTires.setOnAction(e -> buyItemTuning(createTrackTires(), buyOnRoadTires));

        gm.getTuningParts().stream()
                .map(TuningParts::getName)
                .forEach(name -> {
                    if (name.equals("Nitrous Oxide"))  buyNitrousOxide.setDisable(true);
                    if (name.equals("Turbocharger"))    buyTurbocharger.setDisable(true);
                    if (name.equals("Supercharger"))    buySupercharger.setDisable(true);
                    if (name.equals("Off Road Tires"))  buyOffRoadTires.setDisable(true);
                    if (name.equals("Track Tires"))     buyOnRoadTires.setDisable(true);
                });

    }

    private void updateMoneyLabel() {
        double money = GameManager.getInstance().getPlayer().getMoney();
        moneyLabel.setText("Balance: $" + money);
    }

    private void buyItemTuning(TuningParts part, Button btn) {
        String result = shopService.purchasePart(part);
        noMoney.setText(result);
        updateMoneyLabel();
        if (result.startsWith("Purchase successful")) {
            btn.setDisable(true);
        }
    }

    private void buyVehicle(Vehicle car, Button btn) {
        String result = shopService.purchaseVehicle(car);
        noMoney.setText(result);
        System.out.println(result);
        updateMoneyLabel();
        if (result.startsWith("Purchase successful")) {
            btn.setDisable(true);
        }
    }

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
