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

import static seng201.team124.factories.TuningPartFactory.*;

public class Shop {

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


    @FXML
    private void initialize() {

        shopService = GameManager.getInstance().getShopService();
        updateMoneyLabel();


        buyVehicle(buyRedVehicle, VehicleFactory.createRedVehicle());
        buyVehicle(buyCatVehicle, VehicleFactory.createCatVehicle());
        buyVehicle(buyBlueVehicle, VehicleFactory.createBlueVehicle());

        for (Vehicle vehicle : VehicleFactory.getAllVehicles()) {
            if (vehicle.getName().equals("Red Car")) buyRedVehicle.setDisable(false);
            else if (vehicle.getName().equals("Cat Car")) buyCatVehicle.setDisable(false);
            else if (vehicle.getName().equals("Blue Car")) buyBlueVehicle.setDisable(false);
        }

        buyNitrousOxide.setOnAction(e -> buyItemTuning(createNitrousOxide(), buyNitrousOxide));
        buyTurbocharger.setOnAction(e -> buyItemTuning(createTurbocharger(), buyTurbocharger));
        buySupercharger.setOnAction(e -> buyItemTuning(createSupercharger(), buySupercharger));
        buyOffRoadTires.setOnAction(e -> buyItemTuning(createOffRoadTires(), buyOffRoadTires));
        buyOnRoadTires.setOnAction(e -> buyItemTuning(createTrackTires(), buyOnRoadTires));

        GameManager gm = GameManager.getInstance();
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
        GameManager gm = GameManager.getInstance();
        double cost = part.getCost();
        if (gm.getPlayer().getMoney() >= cost) {
            gm.getPlayer().subtractMoney(cost);
            gm.getPlayer().addTuningPart(part);

            btn.setDisable(true);
            updateMoneyLabel();
            noMoney.setText("Purchased");
        } else {
            noMoney.setText("Not enough money!");
        }
    }

    private void buyVehicle(Button btn,Vehicle car) {
        btn.setOnAction(evt -> {
            String result = shopService.purchaseVehicle(car);
            noMoney.setText(result);
            updateMoneyLabel();
            if (result.startsWith("Purchase successful")) {
                updateMoneyLabel();
                btn.setDisable(true);

            }
        });
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
