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
    // This is a container node in the javafx scene graph that can hold other nodes.
    // The root3D is the main group that will hold the 3D
    private final Group root3D = new Group();
    // This stores the KeyCode of each key that is currently being held down
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    // Defines the viewpoint from which the 3D scene is rendered
    private PerspectiveCamera camera;
    // This holds the 3D model of the car. I've used a group because might consist of multiple meshes
    private Group car;
    // Stores the rotation of the car on y-axes
    private double carAngle = 0;
    // temp
    private Box box;

    private double targetCarAngle = 0;
    private double rotationSmoothFactor = 0.15;


    // Stores the current x,y,z coordinates of the camera
    private double camX = 0;
    private double camY = 0;
    private double camZ = 0;

    @FXML
    public void setupGameScene(Stage stage) {

        // Loads character-model
        car = laodModel(getClass().getResource("/assets/models/Main_Character.obj"));
        // Rotates the car a set amount of degrees on the y-axis. The first line just creates the rotation transform
        Rotate rotateY = new Rotate(180, Rotate.Y_AXIS);
        car.getTransforms().add(rotateY);
        // Moves the car down the y-axis
        car.setTranslateY(-3);


        // Sets the scale
        car.setScaleX(4);
        car.setScaleY(4);
        car.setScaleZ(4);

        // The model the gets added as a child to the main root3D
        root3D.getChildren().add(car);

        // Load and add the track
        box = new Box(100, 0.5, 100);
        box.setMaterial(new PhongMaterial(Color.GRAY));
        box.setTranslateY(1.5);
        root3D.getChildren().add(box);

        // Sets up the camera and adds it to the root 3D group
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        root3D.getChildren().add(camera);

        // SubScene setup. This is a container for 3D content that can be embedded within a regular 2D scene this also displaces the root 3D
        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        // Sets up the camera
        subScene.setCamera(camera);
        // Make the hdri skyblue
        subScene.setFill(Color.SKYBLUE);

        // Scene setup
        Group mainRoot = new Group();
        mainRoot.getChildren().add(subScene);
        Scene scene = new Scene(mainRoot, 800, 600, true);

        // Set up the key press and release
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        // sets up the location and rotation of the camera
        camera.getTransforms().addAll(
                new Rotate(-10, Rotate.X_AXIS),
                new Translate(0, 0, -30)
        );

        // Updates the location of the camera
        updateCameraFollow();

        // This makes sure that the frame rate is consistent for all computers
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

        // Sets the subscene to be fullscreen as well
        subScene.widthProperty().bind(scene.widthProperty());
        subScene.heightProperty().bind(scene.heightProperty());

        // Creats the scene in the application and makes it fullscreen
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    // This loads the 3D model this is also credited to a youtube video
    private Group laodModel(URL url) {
        Group modelroot = new Group();
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

    // When a key is pressed, it adds the keyCode to the pressedKeys Hashset and then Released removes it form the pressedKets Hashset
    private void handleKeyPressed(KeyEvent event) {
        pressedKeys.add(event.getCode());
    }
    private void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
    }


    private void update(double deltaTime) {
        // The set movement of the car and the rotation
        double moveSpeed = 50.0;
        double rotateSpeed = 70.0;


        // This checks if either a or d is pressed if so they update the rotation also making to take into account the timer
        if (pressedKeys.contains(KeyCode.A)) {
            targetCarAngle -= rotateSpeed * deltaTime;
        }
        if (pressedKeys.contains(KeyCode.D)) {
            targetCarAngle += rotateSpeed * deltaTime;
        }

        double difference = targetCarAngle - carAngle;
        carAngle += difference * rotationSmoothFactor;

        car.setRotationAxis(Rotate.Y_AXIS);
        car.setRotate(carAngle);

        // This converts degrees into rads and then its how much it should move in the x and z axes using trig
        double angleRad = Math.toRadians(carAngle);
        double dx = Math.sin(angleRad);
        double dz = Math.cos(angleRad);

        // This checks if either w or s is pressed if so they update the location also making to take into account the timer
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
        // Location of the camera behind the car
        double distanceBehind = 30;
        double heightAbove = 10;

        // Calculates how much the camera should rotate due the movement of the car
        double angleRad = Math.toRadians(carAngle);
        double targetX = car.getTranslateX() - Math.sin(angleRad) * distanceBehind;
        double targetY = car.getTranslateY() - heightAbove;
        double targetZ = car.getTranslateZ() - Math.cos(angleRad) * distanceBehind;

        // Adds smoothFactor smooths the camera movement and updates where the camera is point/located
        double smoothFactor = 0.1;
        camX += (targetX - camX) * smoothFactor;
        camY += (targetY - camY) * smoothFactor;
        camZ += (targetZ - camZ) * smoothFactor;

        // updates the location of the camera
        camera.setTranslateX(camX);
        camera.setTranslateY(camY);
        camera.setTranslateZ(camZ);

        // Rotates the camera
        camera.setRotationAxis(Rotate.Y_AXIS);
        camera.setRotate(carAngle);
    }
}

