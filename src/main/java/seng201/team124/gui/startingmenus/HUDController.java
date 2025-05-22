package seng201.team124.gui.startingMenus;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import seng201.team124.services.CounterService;

import java.util.function.Consumer;

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


    public void promptChoices(String choice1, String choice2, Consumer<Integer> callback) {
        // Run on the JavaFX Application Thread
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Race Event");
            alert.setHeaderText("You must choose:");

            ButtonType btn1 = new ButtonType(choice1, ButtonBar.ButtonData.OK_DONE);
            ButtonType btn2 = new ButtonType(choice2, ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btn1, btn2);

            alert.showAndWait().ifPresent(chosen -> {
                if (chosen == btn1) {
                    callback.accept(1);
                } else {
                    callback.accept(2);
                }
            });
        });

    }



}
