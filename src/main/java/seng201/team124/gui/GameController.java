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
import javafx.scene.shape.*;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

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

        // Moves the car down the y-axis
        car.setTranslateY(4);
        car.setTranslateX(-225);
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

        // Load and add the track
        // This holds the 3D model of the racetrack. I've used a group because might consist of multiple meshes
        Group modelGroup = loadModel(getClass().getResource("/assets/models/Track2.obj"));

        Group wall = loadModel(getClass().getResource("/assets/models/wall.obj"));
        wall.setTranslateY(5.5);
        wall.setTranslateX(-230);
        wall.setTranslateZ(0);

        root3D.getChildren().add(wall);
// Declare references
        MeshView racetrack = null;
//        MeshView startMesh = null;
        MeshView finishMesh = null;
        List<Point3D> waypoints = new ArrayList<>();
        PhongMaterial checkpointMaterial = new PhongMaterial(Color.LIMEGREEN);
        List<MeshView> checkpointMeshes = new ArrayList<>();

// Set shared transform values
        double translateX = -230;
        double translateY = 10;
        double translateZ = 0;

// Apply global transform to model group
        modelGroup.setTranslateX(translateX);
        modelGroup.setTranslateY(translateY);
        modelGroup.setTranslateZ(translateZ);


// Process all MeshViews inside the model
        for (Node node : modelGroup.getChildren()) {
            if (node instanceof MeshView mesh) {
                String id = mesh.getId();

                // Match based on names assigned in Blender
                switch (id) {
                    case "Track" -> racetrack = mesh;
//                    case "Start" -> startMesh = mesh;
                    case "Finish" -> finishMesh = mesh;
                    default -> {
                        mesh.setMaterial(checkpointMaterial);
                        checkpointMeshes.add(mesh); // Store for deferred processing
                    }
                }
            }
        }

// Add the full model to the scene
        root3D.getChildren().add(modelGroup);

// Perform raycasting
        if (racetrack != null) raycast.MeshRaycaster(racetrack);
//        if (startMesh != null) startRay.MeshRaycaster(startMesh);
        if (finishMesh != null) finishRay.MeshRaycaster(finishMesh);

// Defer waypoint collection until scene layout is complete
        Platform.runLater(() -> {
            for (MeshView mesh : checkpointMeshes) {
                Bounds bounds = mesh.getBoundsInParent();
                Point3D localCenter = new Point3D(
                        (bounds.getMinX() + bounds.getMaxX()) / 2,
                        (bounds.getMinY() + bounds.getMaxY()) / 2,
                        (bounds.getMinZ() + bounds.getMaxZ()) / 2
                );
                Point3D worldPosition = mesh.localToScene(localCenter);
                waypoints.add(worldPosition);
                System.out.println("Waypoint: " + worldPosition);
            }

            Collections.reverse(waypoints); // Optional: reverse direction if needed


            // Now that waypoints are valid, spawn bots
            spawnBots(waypoints);
        });


        racetrack.setTranslateY(4.5);
        racetrack.setTranslateX(-230);
        racetrack.setTranslateZ(0);


        root3D.getChildren().add(racetrack);


        raycast.MeshRaycaster(racetrack);

//        startRay.MeshRaycaster(startMesh);
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

    private void spawnBots (List<Point3D> waypoints) {
        int numberOfBots = 15;
        for (int i = 0; i < numberOfBots; i++) {
            Group botGroup = loadModel(getClass().getResource("/assets/models/Supra.obj"));

            float botSize = 0.5F;
            double startOffset = i * 5;

            botGroup.setTranslateX(-210);
            botGroup.setTranslateY(4);
            botGroup.setTranslateZ(startOffset);
            botGroup.setScaleX(botSize);
            botGroup.setScaleY(botSize);
            botGroup.setScaleZ(-botSize); // Reversed Z scale if your model faces backward

            Bots bot = new Bots(botGroup, new ArrayList<>(waypoints));
            root3D.getChildren().add(bot.getModel());
            bots.add(bot);
        }


    }


    private Raycast.RaycastHit getAverageGroundHit(Point3D carPosition) {

        double yaw = this.carAngle; // Car's Y-axis rotation
        Affine rotateY = new Affine();
        rotateY.appendRotation(yaw, Point3D.ZERO, Rotate.Y_AXIS);


        List<Raycast.RaycastHit> hits = new ArrayList<>();
        double carWidth = 1.5;  // Car dimensions
        double carLength = 3.5;
        double rayOffsetY = -1; // Drop rays slightly below base

        // Local offsets relative to car center (in car space)
        Point3D[] localOffsets = new Point3D[] {
                new Point3D(-carWidth / 2, rayOffsetY, carLength / 2),  // RF
                new Point3D(carWidth / 2, rayOffsetY, carLength / 2),   // LF
                new Point3D(-carWidth / 2, rayOffsetY, -carLength / 2),  // RB
                new Point3D(carWidth / 2, rayOffsetY, -carLength / 2)  // LB

        };

        Point3D down = new Point3D(0, 1, 0); // Cast downwards (positive Y since Y is flipped)

        for (Point3D localOffset : localOffsets) {
            // Rotate local offset according to car yaw
            Point3D rotatedOffset = rotateY.transform(localOffset);
            Point3D origin = carPosition.add(rotatedOffset);

            // Perform raycast
            Raycast.RaycastHit hit = raycast.getIntersection(origin, down);
            if (hit != null) {
                hits.add(hit);
            }
        }

        if (hits.isEmpty()) {
            return null;
        }

        // Average hit points and normals
        Point3D averagePoint = new Point3D(0, 0, 0);
        Point3D averageNormal = new Point3D(0, 0, 0);
        for (Raycast.RaycastHit hit : hits) {
            averagePoint = averagePoint.add(hit.point);
            averageNormal = averageNormal.add(hit.normal);
        }

        int numHits = hits.size();
        averagePoint = averagePoint.multiply(1.0 / numHits);
        averageNormal = averageNormal.normalize();

        return new Raycast.RaycastHit(averagePoint, averageNormal);
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
        double rotateSpeed = 50.0;
        double rotationSmoothFactor = 0.15;
        double acceleration = 50;
        final double deceleration = 80;
        final double maxVelocity = 73;
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

        carCollisionBox.setRotationAxis(Rotate.Y_AXIS);
        carCollisionBox.setRotate(carAngle);


        // This converts degrees into rads and then its how much it should move in the x and z axes using trig
        double angleRad = Math.toRadians(carAngle);
        double dx = Math.sin(angleRad);
        double dz = Math.cos(angleRad);

        Point3D down = new Point3D(0, 1, 0);
        Point3D carPosition = new Point3D(car.getTranslateX(), car.getTranslateY(), car.getTranslateZ());
        Raycast.RaycastHit groundHit = getAverageGroundHit(carPosition);

        if (groundHit != null) {
            double carYOffset = 6;
            double targetY = groundHit.point.getY() - carYOffset;
            car.setTranslateY(targetY);
            carCollisionBox.setTranslateY(car.getTranslateY());


            Point3D normal = new Point3D(
                    -groundHit.normal.getX(),
                    -groundHit.normal.getY(),
                    -groundHit.normal.getZ()
            ).normalize(); // Surface "up"
            Point3D carIntentForward = new Point3D(-Math.sin(Math.toRadians(carAngle)), 0, -Math.cos(Math.toRadians(carAngle))).normalize();
            
            Point3D localX = carIntentForward.crossProduct(normal).normalize(); // local X axis
            if (localX.magnitude() < 0.001) {
                Point3D worldAxes = new Point3D(1, 0, 0);
                localX = worldAxes.crossProduct(normal).normalize();
                if (localX.magnitude() < 0.001) {
                    Point3D worldZAxis = new Point3D(0, 0, 1);
                    localX = worldZAxis.crossProduct(normal).normalize();
                }
            }
            Point3D localZ = normal.crossProduct(localX).normalize().multiply(1);


            double carAngleX = 0;
            double carAngleZ = 0;
            Rotate rotateX = new Rotate(carAngleX, Rotate.X_AXIS);
            Rotate rotateZ = new Rotate(-carAngleZ, Rotate.Z_AXIS);

            Affine orientationTransform = new Affine(
                    localX.getX(), normal.getX(), localZ.getX(), 0.0,
                    localX.getY(), normal.getY(), localZ.getY(), 0.0,
                    localX.getZ(), normal.getZ(), localZ.getZ(), 0.0
            );

            car.getTransforms().setAll(rotateX, rotateZ, orientationTransform);

            carCollisionBox.setRotationAxis(Rotate.Y_AXIS);
            carCollisionBox.setRotate(carAngle);

        } else if (velocity != 0) {

            Rotate airRotation = new Rotate(carAngle, Rotate.Y_AXIS);
            car.getTransforms().add(airRotation);
            carCollisionBox.getTransforms().add(airRotation);

            friction *= 2;
            if (velocity > maxVelocity / 4) {
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



        boolean finished = finishRay.isRayHitting(carPosition, down);
        boolean started = startRay.isRayHitting(carPosition, down);
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