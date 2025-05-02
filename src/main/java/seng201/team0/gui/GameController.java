//package seng201.team0.gui;
//
//import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
//import javafx.animation.AnimationTimer;
//import javafx.fxml.FXML;
//import javafx.scene.*;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.Box;
//import javafx.scene.transform.Rotate;
//import javafx.scene.transform.Translate;
//import javafx.stage.Stage;
//import javafx.scene.shape.MeshView;
//import java.net.URL;
//import java.util.HashSet;
//import java.util.Set;
//
//
//
//public class GameController {
//    // Example fix for line 22:
//
//
//    private final Group root3D = new Group(); // This holds the 3D objects
//    private final Set<KeyCode> pressedKeys = new HashSet<>(); // A set that keeps track of which keys are pressed
//    private PerspectiveCamera camera; // A perspective camera that shows that viewpoint
//    private Box car; // The car
//    private double carAngle = 0;
//    private Box box;
//
//    // Camera position
//    private double camX = 0;
//    private double camY = 0;
//    private double camZ = 0;
//
//    @FXML
//    public void setupGameScene(Stage stage) {
//
//        Group model = laodModel(getClass().getResource("main character_retoptime;-;_RetopoFlow_AutoSave.obj"));
//
//
//        // Load and add the track
//        box = new Box(100,0.5,100);
//        box.setMaterial(new PhongMaterial(Color.GRAY));
//        box.setTranslateY(1.5);
//        root3D.getChildren().add(box);
//
//        // Create the car
//        car = new Box(2, 1, 4);
//        car.setMaterial(new PhongMaterial(Color.RED));
//        car.setTranslateY(-1);
//        root3D.getChildren().add(car);
//
//        // Set up camera
//        camera = new PerspectiveCamera(true);
//        // Render distance
//        camera.setNearClip(0.1);
//        camera.setFarClip(1000.0);
//        root3D.getChildren().add(camera);
//
//        // SubScene for 3D content
//        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
//        subScene.setCamera(camera);
//        subScene.setFill(Color.SKYBLUE);
//
//        // Create main-root and scene
//        Group mainRoot = new Group();
//        mainRoot.getChildren().add(subScene);
//        Scene scene = new Scene(mainRoot, 800, 600, true);
//
//        // Connects the key presses with the movement
//        scene.setOnKeyPressed(this::handleKeyPressed);
//        scene.setOnKeyReleased(this::handleKeyReleased);
//
//        // Position camera
//        camera.getTransforms().addAll(
//                new Rotate(-10, Rotate.X_AXIS),
//                new Translate(0, 0, -30)
//        );
//        updateCameraFollow();
//
//        // frame loop
//        new AnimationTimer() {
//            private long lastUpdate = -1;
//
//            // This changes it from relying on frame count to dictate the speed, it makes it ralative
//            public void handle(long now) {
//                if (lastUpdate > 0) {
//                    double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // Convert nanoseconds to seconds
//                    update(deltaTime); // Updates the car movement
//                    updateCameraFollow(); // Updates the camera's movement
//                }
//                lastUpdate = now;
//            }
//        }.start();
//
//        stage.setScene(scene);
//        stage.show(); // Display
//    }
//
//
//    private Group laodModel(URL url){
//        Group modelroot = new Group();
//
//        ObjModelImporter importer = new ObjModelImporter();
//        importer.read(url);
//
//        for (MeshView view : importer.getImport()) {
//            modelroot.getChildren().add(view);
//        }
//    return modelroot;
//
//    }
//
//    // Adds keys to the pressedKeys
//    private void handleKeyPressed(KeyEvent event) {
//        pressedKeys.add(event.getCode());
//    }
//
//    // Removes keys from the pressedKeys
//    private void handleKeyReleased(KeyEvent event) {
//        pressedKeys.remove(event.getCode());
//    }
//
//    private void update(double deltaTime) {
//        double moveSpeed = 50.0;
//        double rotateSpeed = 90.0;
//
//        // Rotate car left
//        if (pressedKeys.contains(KeyCode.A)) {
//            carAngle -= rotateSpeed * deltaTime;
//        }
//
//        // Rotate car right
//        if (pressedKeys.contains(KeyCode.D)) {
//            carAngle += rotateSpeed * deltaTime;
//        }
//
//        // Apply rotation to the car's visual transform
//        car.setRotationAxis(Rotate.Y_AXIS);
//        car.setRotate(carAngle);
//
//        // Calculate direction-vector from the angle the car is facing now
//        double angleRad = Math.toRadians(carAngle);
//        double dx = Math.sin(angleRad);
//        double dz = Math.cos(angleRad);
//
//        // Move forward
//        if (pressedKeys.contains(KeyCode.W)) {
//            car.setTranslateX(car.getTranslateX() + dx * moveSpeed * deltaTime);
//            car.setTranslateZ(car.getTranslateZ() + dz * moveSpeed * deltaTime);
//        }
//
//        // Move backward
//        if (pressedKeys.contains(KeyCode.S)) {
//            car.setTranslateX(car.getTranslateX() - dx * moveSpeed * deltaTime);
//            car.setTranslateZ(car.getTranslateZ() - dz * moveSpeed * deltaTime);
//        }
//    }
//
//    private void updateCameraFollow() {
//        double distanceBehind = 30;
//        double heightAbove = 10;
//
//        // Tracks the car movement but taking the rotation data
//        double angleRad = Math.toRadians(carAngle);
//        double targetX = car.getTranslateX() - Math.sin(angleRad) * distanceBehind;
//        double targetY = car.getTranslateY() - heightAbove;
//        double targetZ = car.getTranslateZ() - Math.cos(angleRad) * distanceBehind;
//
//        // Add a little delay so it feels smoother
//        double smoothFactor = 0.1;
//        camX += (targetX - camX) * smoothFactor;
//        camY += (targetY - camY) * smoothFactor;
//        camZ += (targetZ - camZ) * smoothFactor;
//
//        camera.setTranslateX(camX);
//        camera.setTranslateY(camY);
//        camera.setTranslateZ(camZ);
//
//        // Faces the same direction as the car
//        camera.setRotationAxis(Rotate.Y_AXIS);
//        camera.setRotate(carAngle);
//    }
//}
