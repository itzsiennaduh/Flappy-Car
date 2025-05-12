package seng201.team124.gui;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
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
import seng201.team124.models.Bots;
import java.net.URL;
import java.util.*;


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
    private double targetCarAngle = 0;
    private double velocity = 0;


    private Box carCollisionBox;
    private final List<Box> obstacles = new ArrayList<>();
    private final Raycast raycast = new Raycast();
    Raycast startRay = new Raycast();
    Raycast finishRay = new Raycast();


    private final List<Bots> bots = new ArrayList<>();



    // Stores the current x,y,z coordinates of the camera
    private double camX = 0;
    private double camY = 0;
    private double camZ = 0;

    @FXML
    public void setupGameScene(Stage stage) {

        // Loads character-model
        car = loadModel(getClass().getResource("/assets/models/Supra.obj"));

        // Rotates the car a set amount of degrees on the y-axis. The first line just creates the rotation transform
        Rotate rotateY = new Rotate(180, Rotate.Y_AXIS);
        car.getTransforms().add(rotateY);
        // Moves the car down the y-axis
        car.setTranslateY(4);
        car.setTranslateX(-215);
        // Sets the scale
        float size = 0.5F;
        car.setScaleX(size);
        car.setScaleY(size);
        car.setScaleZ(size);
        // The model gets added as a child to the main root3D
        root3D.getChildren().add(car);

        // Sets up the collision box for the car
        // Troubleshoot this it's not moving correctly
        carCollisionBox = new Box(1.5, 1, 3.5);
        carCollisionBox.setVisible(false);
        root3D.getChildren().add(carCollisionBox);

        Box wall = new Box(2, 2, 2);
        wall.setTranslateX(30);
        wall.setTranslateY(0);
        wall.setTranslateZ(10);
        wall.setMaterial(new PhongMaterial(Color.DARKRED));
        root3D.getChildren().add(wall);


        // Load and add the track
        // This holds the 3D model of the racetrack. I've used a group because might consist of multiple meshes
        Group modelGroup = loadModel(getClass().getResource("/assets/models/racetrack.obj"));

        MeshView racetrack = null;
        MeshView startMesh = null;
        MeshView finishMesh = null;

        for (Node node : modelGroup.getChildren()) {
            if (node instanceof MeshView) {
                MeshView mesh = (MeshView) node;
                String name = mesh.getId(); // Try getId() if it's set
                if (name == null) name = mesh.toString(); // Fallback debug

                // Try matching by checking string output or assign manually
                if (mesh.getId() != null) {
                    switch (mesh.getId()) {
                        case "Plane.001" -> racetrack = mesh;
                        case "Start" -> startMesh = mesh;
                        case "Finish" -> finishMesh = mesh;
                    }
                }
            }
        }

        Group trackGroup = loadModel(getClass().getResource("/assets/models/CheckPoints.obj"));
        PhongMaterial checkpointMaterial = new PhongMaterial(Color.LIMEGREEN);
        float trackSize = 2;

// Set transforms before adding to scene
        trackGroup.setTranslateX(-230);
        trackGroup.setTranslateY(10);
        trackGroup.setTranslateZ(6825);
        trackGroup.setScaleX(trackSize);
        trackGroup.setScaleY(trackSize);
        trackGroup.setScaleZ(trackSize);

// Add to scene
        root3D.getChildren().add(trackGroup);

// Defer waypoint collection until JavaFX layout is complete
        Platform.runLater(() -> {
            List<Point3D> waypoints = new ArrayList<>();
            for (Node node : trackGroup.getChildren()) {
                if (node instanceof MeshView mesh) {
                    mesh.setMaterial(checkpointMaterial);

                    Bounds bounds = mesh.getBoundsInParent(); // or getBoundsInLocal() depending on what works
                    Point3D localCenter = new Point3D(
                            (bounds.getMinX() + bounds.getMaxX()) / 2,
                            (bounds.getMinY() + bounds.getMaxY()) / 2,
                            (bounds.getMinZ() + bounds.getMaxZ()) / 2
                    );
                    Point3D worldPosition = mesh.localToScene(localCenter);
                    waypoints.add(worldPosition);


                    System.out.println("Waypoint: " + worldPosition);
                }
            }


            Collections.reverse(waypoints); // Optional

            // Load bot model
            Group botGroup = loadModel(getClass().getResource("/assets/models/Supra.obj"));

            // Position and scale the bot
            float botSize = 0.5F;
            botGroup.setTranslateX(-210);
            botGroup.setTranslateY(4);
            botGroup.setTranslateZ(0);
            botGroup.setScaleX(botSize);
            botGroup.setScaleY(botSize);
            botGroup.setScaleZ(-botSize);

            // Create bot and add to scene
            Bots bot = new Bots(botGroup, waypoints);
            root3D.getChildren().add(bot.getModel());
            bots.add(bot); // Assuming you have a List<Bots> bots;
        });



        racetrack.setTranslateY(4.5);
        racetrack.setTranslateX(-230);
        racetrack.setTranslateZ(6800);

        racetrack.setScaleX(trackSize);
        racetrack.setScaleY(trackSize);
        racetrack.setScaleZ(trackSize);
        root3D.getChildren().add(racetrack);


        raycast.MeshRaycaster(racetrack);

        startRay.MeshRaycaster(startMesh);
        finishRay.MeshRaycaster(finishMesh);




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
                new Translate(0, 0, -15)
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
                    for (Bots bot : bots) {
                        bot.update(deltaTime);
                    }
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
    private Group loadModel(URL url) {
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
        double rotateSpeed = 35.0;
        double rotationSmoothFactor = 0.15;
        double acceleration = 100;
        final double deceleration = 80;
        final double maxVelocity = 200;
        double friction = 40;


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

        carCollisionBox.setRotationAxis(Rotate.Y_AXIS);
        carCollisionBox.setRotate(carAngle);


        // This converts degrees into rads and then its how much it should move in the x and z axes using trig
        double angleRad = Math.toRadians(carAngle);
        double dx = Math.sin(angleRad);
        double dz = Math.cos(angleRad);

        Point3D carPosition = new Point3D(car.getTranslateX(), car.getTranslateY() + 2, car.getTranslateZ());
        Point3D rayDirection = new Point3D(0, -1, 0); // cast straight down

        boolean isOnTrack = raycast.isRayHitting(carPosition, rayDirection);

        if (velocity != 0 && !isOnTrack) {
//            System.out.println("Car is off the track!");
            friction *=2;
            if (velocity > maxVelocity/4) {
                acceleration = -acceleration;
            }
        }





        // This checks if either w or s is pressed if so they update the location also making to take into account the timer
        if (pressedKeys.contains(KeyCode.W)) {
            velocity += acceleration * deltaTime;
        } else if (pressedKeys.contains(KeyCode.S)) {
            velocity -= deceleration * deltaTime;
        } else {
            if (velocity > 0) {
                velocity -= friction * deltaTime;
                if (velocity < 0)  velocity  = 0;
            } else if (velocity < 0) {
                velocity += friction * deltaTime;
                if (velocity > 0) velocity = 0;
            }
        }

        if (velocity > maxVelocity) velocity = maxVelocity;
        if (velocity < -maxVelocity /2 ) velocity = -maxVelocity /2;



        car.setTranslateX(car.getTranslateX() + dx * velocity * deltaTime);
        car.setTranslateZ(car.getTranslateZ() + dz * velocity * deltaTime);


        carCollisionBox.setTranslateX(car.getTranslateX());
        carCollisionBox.setTranslateZ(car.getTranslateZ());
        carCollisionBox.setTranslateY(car.getTranslateY());




        boolean finished = finishRay.isRayHitting(carPosition, rayDirection);
        boolean started = startRay.isRayHitting(carPosition, rayDirection);
        if (finished) {
            System.out.println("Finished!");
        }

        for (Box obstacle : obstacles) {
            if (checkCollision(carCollisionBox, obstacle)) {
                System.out.println("Collision detected!");
                // Stop movement, or bounce back
                break;
            }
        }



    }
    private boolean checkCollision(Box a, Box b) {
        Bounds boundsA = a.localToScene(a.getBoundsInLocal());
        Bounds boundsB = b.localToScene(b.getBoundsInLocal());
        return boundsA.intersects(boundsB);
    }

    private void updateCameraFollow() {
        // Location of the camera behind the car
        double distanceBehind = 0.5;
        double heightAbove = 1;

        // Calculates how much the camera should rotate due to the movement of the car
        double angleRad = Math.toRadians(carAngle);
        double targetX = car.getTranslateX() - Math.sin(angleRad) * distanceBehind;
        double targetY = car.getTranslateY() - heightAbove;
        double targetZ = car.getTranslateZ() - Math.cos(angleRad) * distanceBehind;

        // Adds smoothFactor smooths the camera movement and updates where the camera is point/located
        double smoothFactor = 0.04;
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

