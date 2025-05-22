package seng201.team124.gui.startingmenus;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.gui.importantcontrollers.GameController;
import seng201.team124.gui.loadingscreen.GameLoadTask;
import seng201.team124.gui.loadingscreen.LoadingScreenController;
import seng201.team124.models.Player;
import seng201.team124.models.vehicleutility.Vehicle;
import seng201.team124.services.GameManager;

import java.io.IOException;

public class MainMenu {

    @FXML
    private Button shopButton;

    @FXML
    private Button garageButton;

    @FXML
    private Button startGameButton;



    @FXML
    private void handleShopButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Shop.fxml"));
        Parent root = loader.load();
        Scene currentScene = shopButton.getScene();
        currentScene.setRoot(root);
        Stage stage = (Stage) currentScene.getWindow();
        stage.setFullScreen(true);
    }

    @FXML
    private void handleGarageButton() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Garage.fxml"));
        Parent root = loader.load();
        Scene currentScene = garageButton.getScene();
        currentScene.setRoot(root);
    }

    @FXML
    protected void NewGame(ActionEvent event) throws IOException {


        Node sourceNode  = (Node)event.getSource();
        Scene currentScene = sourceNode.getScene();
        Stage stage = (Stage) currentScene.getWindow();


        Vehicle playerVehicle = VehicleFactory.createRedVehicle();
        GameManager gm = GameManager.getInstance();
        // Make sure you've already called initializeGame(...) somewhere earlier,
        // so gm.getPlayer() is non-null. If not, call gm.initializeWithDefaults() first.
        Player player = gm.getPlayer();
        player.getVehicles().add(playerVehicle);
        player.setCurrentVehicle(playerVehicle);
        // Optionally, if you're still using playerModel in GameManager:
        gm.setPlayerModel("/assets/models/Cars/Supra.obj");

        // 1) Display loading screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoadingScreen.fxml"));
        Parent loadingRoot = loader.load();
        LoadingScreenController loadCtrl = loader.getController();
        currentScene.setRoot(loadingRoot);

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

            FXMLLoader hudLoader = new FXMLLoader(getClass().getResource("/fxml/HudOverlay.fxml"));
            Parent hudOverlay = null;
            try {
                hudOverlay = hudLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            Region hudRegion = (Region) hudOverlay;
            hudRegion.prefWidthProperty().bind(gameSub.widthProperty());
            hudRegion.prefHeightProperty().bind(gameSub.heightProperty());

            HUDController hudController = hudLoader.getController();
            gameCtrl.setHudController(hudController);

            gameCtrl.randomEvent();


// 3a) Pre-warm the SubScene so shaders, meshes, and materials are compiled/uploaded
            gameSub.getRoot().applyCss();            // process any CSS
            gameSub.getRoot().layout();              // lay out the scene graph
            gameSub.snapshot(new SnapshotParameters(), null);  // force an offscreen render

// 4) Set camera
            PerspectiveCamera cam = findCamera(gameRoot);
            if (cam != null) gameSub.setCamera(cam);

// 5) Wrap & bind size
            Image bgTex = new Image(
                    getClass().getResource("/assets/models/RaceTacks/Images/sunflowers_4k.jpg")
                            .toExternalForm()
            );

// 2) make an ImageView that always fills the window
            ImageView bgView = new ImageView(bgTex);
            bgView.setPreserveRatio(false);
            bgView.setSmooth(true);


// 3) stack the SubScene on top of the ImageView
            StackPane root = new StackPane(bgView, gameSub, hudOverlay);
            gameSub.widthProperty().bind(root.widthProperty());
            gameSub.heightProperty().bind(root.heightProperty());

// 4) use THAT as your Scene’s root
            currentScene.setRoot(root);
            currentScene.setOnKeyPressed(gameCtrl::handleKeyPressed);
            currentScene.setOnKeyReleased(gameCtrl::handleKeyReleased);

        });

        // On failure, show error
        loadTask.setOnFailed(e -> {
            Throwable ex = loadTask.getException();
            new Alert(Alert.AlertType.ERROR, "Failed to load game: " + ex.getMessage())
                    .showAndWait();
        });

        new Thread(loadTask).start();
    }


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
