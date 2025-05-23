package seng201.team124.gui.startingMenus;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import seng201.team124.models.raceLogic.Race;
import seng201.team124.services.CounterService;
import seng201.team124.services.GameManager;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.function.Consumer;

public class HUDController {
    @FXML private ProgressBar fuelProgress;
    @FXML private ProgressBar timeProgress;
    @FXML private Label timerNum, moneyLabel;
    @FXML private GridPane breakDown, innerPane;
    @FXML private Button payBtn, retireBtn;

    //private Race currentRace;
    //private final double timeLimitSeconds = currentRace.getHours();

    public void updateFuel(double percent) {
        fuelProgress.setProgress(percent/100.0);
    }

    public void updateTime(double totalSeconds) {
        Platform.runLater(() -> {
            int hours = (int)(totalSeconds / 3600);
            int minutes = (int)((totalSeconds % 3600) / 60);
            int seconds = (int)(totalSeconds % 60);
            timerNum.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

            //if (timeProgress != null && timeLimitSeconds > 0) {
              //  timeProgress.setProgress(Math.min(1.0, totalSeconds / timeLimitSeconds));
            //}
        });
    }

    public void setMoney(double money) {
        moneyLabel.setText(String.format("Balance: $%.2f", money));
    }

    /**
     * Shows your overlay + inner pane, wires the choice buttons,
     * then hides everything and clears handlers when done.
     */
    public void promptChoices(String choice1, String choice2, Consumer<Integer> callback) {
        Platform.runLater(() -> {
            // 1) Show both panes
            breakDown.setVisible(true);
            innerPane.setVisible(true);

            // 2) Configure texts
            payBtn   .setText(choice2);
            retireBtn.setText(choice1);

            // 3) Build handlers
            EventHandler<javafx.event.ActionEvent> payHandler = e -> {
                callback.accept(1);

                // example side-effect
                GameManager.getInstance()
                        .getPlayer()
                        .subtractMoney(1000);

                // refresh your displayed balance
                setMoney(GameManager.getInstance().getPlayer().getMoney());

                // tear everything down
                hideOverlay();
            };

            EventHandler<javafx.event.ActionEvent> retireHandler = e -> {
                callback.accept(2);
                hideOverlay();

                // then load main menu
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/fxml/MainMenu.fxml"));
                    Parent root = loader.load();
                    Scene scene = retireBtn.getScene();
                    scene.setRoot(root);

                    // optional: re-enter full screen
                    Stage stage = (Stage) scene.getWindow();
                    stage.setFullScreen(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            };

            // 4) Wire them
            payBtn   .setOnAction(payHandler);
            retireBtn.setOnAction(retireHandler);
        });
    }

    /** Hides both panes and clears the button handlers so they don’t accumulate. */
    private void hideOverlay() {
        breakDown.setVisible(false);
        innerPane.setVisible(false);

        // clear handlers
        payBtn   .setOnAction(null);
        retireBtn.setOnAction(null);
    }

}

