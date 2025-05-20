package seng201.team124.gui.importantcontrollers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import seng201.team124.gui.loadingscreen.GameLoadTask;
import seng201.team124.gui.loadingscreen.LoadingScreenController;

import java.io.IOException;

/**
 * Controller for main.fxml, handling navigation and loading screen.
 */
public class MainController {
    @FXML private Button defaultButton;
    @FXML private Button Gamebutton;

    /**
     * Exit the application.
     */
    @FXML
    protected void Exit() {
        Platform.exit();
    }

    /**
     * Start a new game with a loading screen, then show the 3D scene.
     */
    @FXML
    protected void NewGame(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // 1) Display loading screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoadingScreen.fxml"));
        Parent loadingRoot = loader.load();
        LoadingScreenController loadCtrl = loader.getController();
        stage.setScene(new Scene(loadingRoot));
        stage.setFullScreen(true);

        // 2) Run background load task
        GameController gameCtrl = new GameController();
        Task<Group> loadTask = new GameLoadTask(gameCtrl);

        // Bind progress UI
        loadCtrl.getProgressBar().progressProperty().bind(loadTask.progressProperty());
        loadCtrl.getStatusLabel().textProperty().bind(loadTask.messageProperty());

        // On success, wrap the 3D group in a SubScene
        loadTask.setOnSucceeded(e -> {
            Group gameRoot = loadTask.getValue();
            gameCtrl.updateCameraFollow();  // initial camera positioning

            // 3) Build SubScene around the Group
            SubScene gameSub = new SubScene(
                    gameRoot,
                    stage.getWidth(),
                    stage.getHeight(),
                    true,
                    SceneAntialiasing.BALANCED
            );



// 3a) Pre-warm the SubScene so shaders, meshes, and materials are compiled/uploaded
            gameSub.getRoot().applyCss();            // process any CSS
            gameSub.getRoot().layout();              // lay out the scene graph
            gameSub.snapshot(new SnapshotParameters(), null);  // force an offscreen render

// 4) Set camera
            PerspectiveCamera cam = findCamera(gameRoot);
            if (cam != null) gameSub.setCamera(cam);

// 5) Wrap & bind size

            Image bgTex = new Image(
                    getClass().getResource("/assets/models/sunflowers_4k.jpg")
                            .toExternalForm()
            );

// 2) make an ImageView that always fills the window
            ImageView bgView = new ImageView(bgTex);
            bgView.setPreserveRatio(false);
            bgView.setSmooth(true);


// 3) stack the SubScene on top of the ImageView
            StackPane root = new StackPane(bgView, gameSub);
            gameSub.widthProperty().bind(root.widthProperty());
            gameSub.heightProperty().bind(root.heightProperty());

// 4) use THAT as your Scene’s root
            Scene gameScene = new Scene(root);
            gameScene.setOnKeyPressed(gameCtrl::handleKeyPressed);
            gameScene.setOnKeyReleased(gameCtrl::handleKeyReleased);
            stage.setScene(gameScene);
            stage.setFullScreen(true);

        });

        // On failure, show error
        loadTask.setOnFailed(e -> {
            Throwable ex = loadTask.getException();
            new Alert(Alert.AlertType.ERROR, "Failed to load game: " + ex.getMessage())
                    .showAndWait();
        });

        new Thread(loadTask).start();
    }

    /**
     * Navigate to character naming screen.
     */
    @FXML
    protected void CharacterName(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/CharacterNameLayout.fxml")
        );
        Parent root = loader.load();
        Stage stage = (Stage) defaultButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
    }

    /**
     * Recursively search for the PerspectiveCamera in the 3D graph.
     */
    private PerspectiveCamera findCamera(Node node) {
        if (node instanceof PerspectiveCamera) {
            return (PerspectiveCamera) node;
        } else if (node instanceof Group) {
            for (Node child : ((Group) node).getChildren()) {
                PerspectiveCamera found = findCamera(child);
                if (found != null) return found;
            }
        } else if (node instanceof SubScene) {
            return (PerspectiveCamera) ((SubScene) node).getCamera();
        }
        return null;
    }
}