package seng201.team124.gui.startingmenus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import seng201.team124.services.CounterService;

public class HUDController {
    @FXML private ProgressBar fuelProgress;
    @FXML private Label timerNum;
    @FXML private Label moneyLabel;

    public void updateFuel(double percent) {
        fuelProgress.setProgress(percent / 100.0);
    }

    public void updateTime(String time) {
        timerNum.setText(time);
    }

    public void setMoney(double money) {
        moneyLabel.setText(String.format("Balance: $%.2f", money));
    }
}
