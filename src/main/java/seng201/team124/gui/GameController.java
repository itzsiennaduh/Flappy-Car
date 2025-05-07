package seng201.team124.gui;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.shape.MeshView;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;



// ... imports and class declaration stay the same

public class GameController {
    private final Group root3D = new Group();
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private PerspectiveCamera camera;
    private Group car; // changed from Box to Group
    private double carAngle = 0;
    private Box box;

    private double camX = 0;
    private double camY = 0;
    private double camZ = 0;

    @FXML
    public void setupGameScene(Stage stage) {

        // Load character model
        car = laodModel(getClass().getResource("/assets/models/Main_Character.obj")); // renamed from 'model' to 'car'

        Rotate rotateY = new Rotate(180, Rotate.Y_AXIS);
        car.getTransforms().add(rotateY);
        car.setTranslateY(-3); // optional adjustment



        car.setScaleX(4);
        car.setScaleY(4);
        car.setScaleZ(4);

        root3D.getChildren().add(car);

        // Load and add the track
        box = new Box(100, 0.5, 100);
        box.setMaterial(new PhongMaterial(Color.GRAY));
        box.setTranslateY(1.5);
        root3D.getChildren().add(box);

        // Set up camera
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        root3D.getChildren().add(camera);

        // SubScene setup
        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.SKYBLUE);

        // Scene setup
        Group mainRoot = new Group();
        mainRoot.getChildren().add(subScene);
        Scene scene = new Scene(mainRoot, 800, 600, true);

        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        camera.getTransforms().addAll(
                new Rotate(-10, Rotate.X_AXIS),
                new Translate(0, 0, -30)
        );
        updateCameraFollow();

        new AnimationTimer() {
            private long lastUpdate = -1;
            public void handle(long now) {
                if (lastUpdate > 0) {
                    double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                    update(deltaTime);
                    updateCameraFollow();
                }
                lastUpdate = now;
            }
        }.start();

        stage.setScene(scene);
        stage.show();
    }

    private Group laodModel(URL url) {
        Group modelroot = new Group();
        if (url == null) {
            System.err.println("Error: Model URL is null!");
            return modelroot;
        }

        ObjModelImporter importer = new ObjModelImporter();
        try {
            importer.read(url);
            for (MeshView view : importer.getImport()) {
                modelroot.getChildren().add(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelroot;
    }

    private void handleKeyPressed(KeyEvent event) {
        pressedKeys.add(event.getCode());
    }

    private void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
    }

    private void update(double deltaTime) {
        double moveSpeed = 50.0;
        double rotateSpeed = 90.0;

        if (pressedKeys.contains(KeyCode.A)) {
            carAngle -= rotateSpeed * deltaTime;
        }
        if (pressedKeys.contains(KeyCode.D)) {
            carAngle += rotateSpeed * deltaTime;
        }

        car.setRotationAxis(Rotate.Y_AXIS);
        car.setRotate(carAngle);

        double angleRad = Math.toRadians(carAngle);
        double dx = Math.sin(angleRad);
        double dz = Math.cos(angleRad);

        if (pressedKeys.contains(KeyCode.W)) {
            car.setTranslateX(car.getTranslateX() + dx * moveSpeed * deltaTime);
            car.setTranslateZ(car.getTranslateZ() + dz * moveSpeed * deltaTime);
        }
        if (pressedKeys.contains(KeyCode.S)) {
            car.setTranslateX(car.getTranslateX() - dx * moveSpeed * deltaTime);
            car.setTranslateZ(car.getTranslateZ() - dz * moveSpeed * deltaTime);
        }
    }

    private void updateCameraFollow() {
        double distanceBehind = 30;
        double heightAbove = 10;

        double angleRad = Math.toRadians(carAngle);
        double targetX = car.getTranslateX() - Math.sin(angleRad) * distanceBehind;
        double targetY = car.getTranslateY() - heightAbove;
        double targetZ = car.getTranslateZ() - Math.cos(angleRad) * distanceBehind;

        double smoothFactor = 0.1;
        camX += (targetX - camX) * smoothFactor;
        camY += (targetY - camY) * smoothFactor;
        camZ += (targetZ - camZ) * smoothFactor;

        camera.setTranslateX(camX);
        camera.setTranslateY(camY);
        camera.setTranslateZ(camZ);

        camera.setRotationAxis(Rotate.Y_AXIS);
        camera.setRotate(carAngle);
    }
}

