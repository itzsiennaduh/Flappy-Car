package seng201.team124.gui.startingmenus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HUDController {
    @FXML private Label timerLabel;
    @FXML private Label timerNum;
    @FXML private Label moneyLabel;
    @FXML private Label fuelLabel;

    public void updateFuel(double percent) {
        fuelLabel.setText(String.format("Fuel: " + percent + "%"));
    }

    public void updateTime(String time) {
        timerNum.setText(time);
    }
}
