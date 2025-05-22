package seng201.team124.gui.loadingScreen;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LoadingScreenController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    @FXML
    public void initialize() {
        progressBar.setProgress(0);
        statusLabel.setText("Loading assets...");
    }

    public void updateProgress(double progress) {
        progressBar.setProgress(progress);
    }

}
