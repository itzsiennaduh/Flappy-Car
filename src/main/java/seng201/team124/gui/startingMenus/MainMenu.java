package seng201.team124.gui.startingMenus;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seng201.team124.gui.importantControllers.GameController;
import seng201.team124.gui.loadingScreen.GameLoadTask;
import seng201.team124.gui.loadingScreen.LoadingScreenController;
import seng201.team124.models.raceLogic.Race;
import seng201.team124.models.vehicleUtility.Vehicle;
import seng201.team124.services.GameManager;

import java.io.IOException;
import java.util.List;

import static seng201.team124.services.GameManager.getInstance;

public class MainMenu {

    @FXML
    private Button shopButton;

    @FXML
    private Button garageButton;

    @FXML
    private Button startGameButton;

    @FXML private Label descriptionLabel;

    @FXML
    private ChoiceBox<String> chooseRace;
    private List<Race> races;
    private Race selectedRace;

    @FXML private Label moneyLabel;
    @FXML private Label seasonLength;

    @FXML
    private void initialize() {
        races = getInstance().getAvailableRaces();
        chooseRace.setItems(FXCollections.observableArrayList(races.stream().map(Race::getName).toList()));

        chooseRace.getSelectionModel().selectedIndexProperty().addListener((obs, oldI, newI) -> {
            if (newI.intValue() >= 0) {
                selectedRace = races.get(newI.intValue());
                getInstance().setSelectedRace(selectedRace);
                descriptionLabel.setText(selectedRace.getRoute().getDescription());
            }
        });

        moneyLabel.setText("$" + getInstance().getPlayer().getMoney());
        seasonLength.setText("Races left:" + getInstance().getSeasonLength());

        // default to first
        chooseRace.getSelectionModel().select(0);
        getInstance().setSelectedRace(selectedRace);
        descriptionLabel.setText(selectedRace.getRoute().getDescription());

    }



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

        // 1) Display loading screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loadingScreen.fxml"));
        Parent loadingRoot = loader.load();
        LoadingScreenController loadCtrl = loader.getController();
        currentScene.setRoot(loadingRoot);

        // 2) Run background load task
        GameController gameCtrl = new GameController();
        GameManager.getInstance().setGameController(gameCtrl);
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
            gameCtrl.setGameScene(currentScene);
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
