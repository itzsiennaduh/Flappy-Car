package seng201.team124.gui.startingmenus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng201.team124.models.vehicleutility.TuningParts;
import seng201.team124.services.GameManager;

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
    private void initialize() {
        updateMoneyLabel();

        buyNitrousOxide.setOnAction(e -> buyItem(createNitrousOxide(), buyNitrousOxide));
        buyTurbocharger .setOnAction(e -> buyItem(createTurbocharger(), buyTurbocharger));
        buySupercharger.setOnAction(e -> buyItem(createSupercharger(), buySupercharger));
        buyOffRoadTires.setOnAction(e -> buyItem(createOffRoadTires(), buyOffRoadTires));
        buyOnRoadTires.   setOnAction(e -> buyItem(createTrackTires(), buyOnRoadTires));

        // disable any buttons for parts the player already has
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

    private void buyItem(TuningParts part, Button btn) {
        GameManager gm = GameManager.getInstance();
        double cost = part.getCost();
        if (gm.getPlayer().getMoney() >= cost) {
            gm.getPlayer().subtractMoney(cost);
            gm.getPlayer().addTuningPart(part);

            btn.setDisable(true);
//
            updateMoneyLabel();
            noMoney.setText("Purchased");
        } else {
            noMoney.setText("Not enough money!");
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
