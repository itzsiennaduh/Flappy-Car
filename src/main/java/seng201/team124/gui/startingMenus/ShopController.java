package seng201.team124.gui.startingMenus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.models.Player;
import seng201.team124.models.vehicleUtility.TuningParts;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.services.GameManager;
import seng201.team124.services.ShopService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static seng201.team124.factories.TuningPartFactory.*;

public class ShopController {

    @FXML private Button backButton;
    @FXML private Label moneyLabel, noMoney;

    // TUNING buttons
    @FXML private Button buyNitrousOxide, buyTurbocharger, buySupercharger,
            buyOffRoadTires, buyOnRoadTires;
    @FXML private Button sellNitrousOxide, sellTurbocharger, sellSupercharger,
            sellOffRoadTires, sellOnRoadTires;

    // VEHICLE buttons
    @FXML private Button buyRedVehicle,   buyCatVehicle,   buyBlueVehicle;
    @FXML private Button sellRedVehicle,  sellCatVehicle,  sellBlueVehicle;

    private ShopService shopService;
    private Player player;

    // Maps for name → [buyBtn, sellBtn]
    private final Map<String, Button[]> tuningButtons  = new HashMap<>();
    private final Map<String, Button[]> vehicleButtons = new HashMap<>();

    // Pre-create your vehicle instances once
    private final Vehicle redCar  = VehicleFactory.createRedVehicle();
    private final Vehicle catCar  = VehicleFactory.createCatVehicle();
    private final Vehicle blueCar = VehicleFactory.createBlueVehicle();

    @FXML
    private void initialize() {
        shopService = GameManager.getInstance().getShopService();
        player      = GameManager.getInstance().getPlayer();

        // 1) Wire up the tuning-parts map
        tuningButtons.put("Nitrous Oxide", new Button[]{ buyNitrousOxide, sellNitrousOxide });
        tuningButtons.put("Turbocharger",   new Button[]{ buyTurbocharger,   sellTurbocharger });
        tuningButtons.put("Supercharger",   new Button[]{ buySupercharger,   sellSupercharger });
        tuningButtons.put("Off Road Tires", new Button[]{ buyOffRoadTires,   sellOffRoadTires });
        tuningButtons.put("Track Tires",    new Button[]{ buyOnRoadTires,     sellOnRoadTires    });

        // 2) Wire up the vehicle map
        vehicleButtons.put("Red Supra",     new Button[]{ buyRedVehicle,   sellRedVehicle });
        vehicleButtons.put("Kittyyyyy",     new Button[]{ buyCatVehicle,   sellCatVehicle });
        vehicleButtons.put("Blue Supra",    new Button[]{ buyBlueVehicle,  sellBlueVehicle });

        // 3) Hook up your buy/sell handlers for tuning
        buyNitrousOxide.  setOnAction(e -> handleBuyPart(createNitrousOxide(),   buyNitrousOxide));
        buyTurbocharger.  setOnAction(e -> handleBuyPart(createTurbocharger(),    buyTurbocharger));
        buySupercharger.  setOnAction(e -> handleBuyPart(createSupercharger(),    buySupercharger));
        buyOffRoadTires.  setOnAction(e -> handleBuyPart(createOffRoadTires(),    buyOffRoadTires));
        buyOnRoadTires.   setOnAction(e -> handleBuyPart(createTrackTires(),      buyOnRoadTires));

        sellNitrousOxide. setOnAction(e -> handleSellPart(createNitrousOxide(),  sellNitrousOxide));
        sellTurbocharger. setOnAction(e -> handleSellPart(createTurbocharger(),   sellTurbocharger));
        sellSupercharger. setOnAction(e -> handleSellPart(createSupercharger(),   sellSupercharger));
        sellOffRoadTires. setOnAction(e -> handleSellPart(createOffRoadTires(),   sellOffRoadTires));
        sellOnRoadTires.  setOnAction(e -> handleSellPart(createTrackTires(),     sellOnRoadTires));

        // 4) Hook up your buy/sell handlers for vehicles
        buyRedVehicle.    setOnAction(e -> handleBuyVehicle(redCar,  buyRedVehicle));
        buyCatVehicle.    setOnAction(e -> handleBuyVehicle(catCar,  buyCatVehicle));
        buyBlueVehicle.   setOnAction(e -> handleBuyVehicle(blueCar, buyBlueVehicle));

        sellRedVehicle.   setOnAction(e -> handleSellVehicle(redCar,  sellRedVehicle));
        sellCatVehicle.   setOnAction(e -> handleSellVehicle(catCar,  sellCatVehicle));
        sellBlueVehicle.  setOnAction(e -> handleSellVehicle(blueCar, sellBlueVehicle));

        // 5) Initial UI sync
        updateButtons();
    }

    private void updateMoneyLabel() {
        moneyLabel.setText("Balance: $" + player.getMoney());
    }

    private void handleBuyPart(TuningParts part, Button buyBtn) {
        noMoney.setText(shopService.purchasePart(part));
        updateMoneyLabel();
        updateButtons();
    }

    private void handleSellPart(TuningParts part, Button sellBtn) {
        noMoney.setText(shopService.sellPart(part));
        updateMoneyLabel();
        updateButtons();
    }

    private void handleBuyVehicle(Vehicle car, Button buyBtn) {
        noMoney.setText(shopService.purchaseVehicle(car));
        updateMoneyLabel();
        updateButtons();
    }

    private void handleSellVehicle(Vehicle car, Button sellBtn) {
        noMoney.setText(shopService.sellVehicle(car));
        updateMoneyLabel();
        updateButtons();
    }

    private void updateButtons() {
        // 1) Tuning-parts
        tuningButtons.forEach((name, btns) -> {
            boolean owned = player.getTuningParts().stream()
                    .anyMatch(p -> p.getName().equals(name));
            btns[0].setDisable(owned);      // buy
            btns[1].setDisable(!owned);     // sell
        });

        // 2) Vehicles
        vehicleButtons.forEach((name, btns) -> {
            boolean owned = player.getVehicles().stream()
                    .map(Vehicle::getName)
                    .anyMatch(n -> n.equals(name));
            btns[0].setDisable(owned);      // buy
            btns[1].setDisable(!owned);     // sell
        });

        updateMoneyLabel();
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Scene scene = backButton.getScene();
            loader.load();
            scene.setRoot(loader.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
