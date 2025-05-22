package seng201.team124.gui.importantcontrollers;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import seng201.team124.factories.VehicleFactory;
import seng201.team124.gui.bots.Bots;
import seng201.team124.gui.bots.Raycast;
import seng201.team124.gui.endingmenus.DNFController;
import seng201.team124.gui.endingmenus.RaceCompleteController;
import seng201.team124.gui.startingmenus.HUDController;
import seng201.team124.models.racelogic.RaceEvent;
import seng201.team124.models.vehicleutility.Vehicle;
import seng201.team124.services.CounterService;
import seng201.team124.services.GameManager;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameController {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private PerspectiveCamera camera;
    private Group car;
    private double carAngle = 0;
    private double targetCarAngle = 0;
    private double velocity = 0;
    private HUDController hudController;
    private Scene gameScene;
    private boolean dnfShown = false;

    private Box carCollisionBox;
    private final List<Box> obstacles = new ArrayList<>();
    private final Raycast raycast = new Raycast();
    private final Raycast finishRay = new Raycast();
    private final Raycast safeRay = new Raycast();
    private final Raycast death = new Raycast();
    private final Raycast gasstation = new Raycast();
    private final Raycast personEventRay = new Raycast();
    private final List<Bots> bots = new ArrayList<>();
    private final CounterService counterService = new CounterService();
    private boolean raceFinished = false;
    private int playerPlacement = 1;
    private final List<String> finishedEntities = new ArrayList<>();

    private double camX = 0;
    private double camY = 0;
    private double camZ = 0;

    private PauseTransition oneShotEventTimer;
    private final Random rng = new Random();
    private static final int MIN_DELAY = 10;   // seconds
    private static final int MAX_DELAY = 20;
    private MeshView personEvent;

    public Group setupGameRootNode() throws Exception {

//        String musicPath = Objects.requireNonNull(getClass().getResource("/assets/models/Music/dont-talk-315229.mp3")).toExternalForm();
//        Media bgm = new Media(musicPath);
//        MediaPlayer player = new MediaPlayer(bgm);
//        player.setCycleCount(MediaPlayer.INDEFINITE);
//        player.setVolume(0.3);
//        player.play();

          Group root3D = new Group();

//        car = loadModel(getClass().getResource("/assets/models/Supra.obj"));
        Vehicle playerVehicle = VehicleFactory.createRedVehicle();
        car = playerVehicle.getModel();
        car.setTranslateX(5);
        float size = 0.75F;
        car.setScaleX(size);
        car.setScaleY(size);
        car.setScaleZ(size);
        root3D.getChildren().add(car);

        // Collision box
        carCollisionBox = new Box(1.5, 1, 3.5);
        carCollisionBox.setVisible(false);
        root3D.getChildren().add(carCollisionBox);

        // Load track & checkpoints
        Group modelGroup = loadModel(getClass().getResource("/assets/models/RaceTacks/racetrackplease_v0.3.obj"));
        MeshView racetrack = null;
        MeshView racetrack2 = null;
        MeshView finishMesh = null;
        MeshView wall = null;
        MeshView safe = null;
        MeshView Tree = null;
        MeshView GasStation = null;
        MeshView gasstation2 = null;
        MeshView floor = null;
        List<MeshView> checkpointMeshes = new ArrayList<>();
        PhongMaterial checkpointMat = new PhongMaterial(Color.LIMEGREEN);

        modelGroup.setTranslateX(0);
        modelGroup.setTranslateY(0); // align track & checkpoints at the same height
        modelGroup.setTranslateZ(0);

        for (Node node : modelGroup.getChildren()) {
            if (node instanceof MeshView mesh) {
                switch (mesh.getId()) {
                    case "Track":
                        racetrack = mesh;
                    case "Track.001":
                        racetrack2 = mesh;
                        break;
                    case "Finish":
                        finishMesh = mesh;
                        break;
                    case "wall":
                        wall = mesh;
                        break;
                    case "safe":
                        safe = mesh;
                        break;
                    case "TreeeMerged":
                        Tree = mesh;
                        break;
                    case "GasStation2":
                        gasstation2 = mesh;
                        break;
                    case "GasStation1":
                        GasStation = mesh;
                        break;
                    case "floor":
                        floor = mesh;
                        break;
                    case "PersonEvent":
                        personEvent = mesh;
                        personEvent.setVisible(false);
                        break;
                    default:
                        mesh.setMaterial(checkpointMat);
                        mesh.setTranslateY(mesh.getTranslateY() + 10);
                        checkpointMeshes.add(mesh);
                }
            }
        }
        root3D.getChildren().add(modelGroup);

        if (racetrack != null) raycast.MeshRaycaster(racetrack);
        if (finishMesh != null) finishRay.MeshRaycaster(finishMesh);
        if (safe != null) safeRay.MeshRaycaster(safe);
        if (wall != null) death.MeshRaycaster(wall);
        if (gasstation2 != null) gasstation.MeshRaycaster(gasstation2);
        if (GasStation != null) gasstation.MeshRaycaster(GasStation);
        if (personEvent != null) personEventRay.MeshRaycaster(personEvent);

        // Compute waypoints in background without Platform.runLater
        List<Point3D> waypoints = new ArrayList<>();
        for (MeshView mesh : checkpointMeshes) {
            Bounds b = mesh.getBoundsInLocal();
            // center in mesh-local coordinates
            Point3D localCenter = new Point3D(
                    (b.getMinX()+b.getMaxX())/2,
                    (b.getMinY()+b.getMaxY())/2,
                    (b.getMinZ()+b.getMaxZ())/2
            );
            // apply modelGroup translation
            Point3D worldPosition = localCenter
                    .add(modelGroup.getTranslateX(), modelGroup.getTranslateY(), modelGroup.getTranslateZ());
            waypoints.add(worldPosition);
        }
        Collections.reverse(waypoints);
// spawn bots in background thread
        spawnBots(waypoints, root3D);

        // No per-mesh translation; modelGroup translation applies to all children (track & checkpoints)
// if you want to adjust track separately, do it on modelGroup only
        if (racetrack != null) {
            raycast.MeshRaycaster(racetrack);
        }

        // Camera initialization
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(600);
        root3D.getChildren().add(camera);
        updateCameraFollow(); // initial placement

        // Game loop
        new AnimationTimer() {
            private long last = -1;
            @Override
            public void handle(long now) {
                if (dnfShown || raceFinished) {
                    this.stop();
                    return;
                }

                if (last > 0) {
                    double dt = (now - last) / 1e9;
                    update(dt);
                    updateCameraFollow();

                    bots.forEach(bot -> {
                        bot.update(dt);

                        Point3D botPosition = new Point3D(
                                bot.getModel().getTranslateX(),
                                bot.getModel().getTranslateY(),
                                bot.getModel().getTranslateZ()
                        );
                        Point3D down = new Point3D(0, 1, 0);

                        if (finishRay.isRayHitting(botPosition, down) && !bot.hasFinished()) {
                            bot.setFinished(true);
                            System.out.println("Bot finished the race!");
                        }
                    });

                    if (counterService.isRaceInProgress()) {
                        counterService.incrementRaceTime(dt);
                        hudController.updateTime(counterService.getFormattedElapsedTime());

                        if (counterService.hasRaceTimeExpired() && !raceFinished && counterService.getTotalRaceHours() > 0) {
                            handleDNF("Time limit exceeded! You did not finish within the race duration.");
                            return;
                        }
                    }
                }
                last = now;
            }
        }.start();


        return root3D;
    }


    public void updateCameraFollow() {
        double d = 20, h=2;
        double angleRad = Math.toRadians(carAngle);
        double tx = car.getTranslateX() - Math.sin(angleRad)*d;
        double ty = car.getTranslateY() - h;
        double tz = car.getTranslateZ() - Math.cos(angleRad)*d;
        double s=0.04;
        camX += (tx-camX)*s; camY+=(ty-camY)*s; camZ+=(tz-camZ)*s;
        camera.setTranslateX(camX);
        camera.setTranslateY(camY);
        camera.setTranslateZ(camZ);
        camera.setRotationAxis(Rotate.Y_AXIS);
        camera.setRotate(carAngle);
    }

    private void spawnBots(List<Point3D> waypoints, Group root3D) {
        int numberOfBots = 15;
        for (int i = 0; i < numberOfBots; i++) {
            Group botGroup = loadModel(getClass().getResource("/assets/models/Cars/Supra.obj"));
            float botSize = 0.5F;
            double startOffset = i * 5;
            botGroup.setTranslateX(0);
            botGroup.setTranslateY(-0.4);
            botGroup.setTranslateZ(startOffset);
            botGroup.setScaleX(botSize);
            botGroup.setScaleY(botSize);
            botGroup.setScaleZ(-botSize);
            Bots bot = new Bots(botGroup, new ArrayList<>(waypoints));
            root3D.getChildren().add(bot.getModel());
            bots.add(bot);
        }
    }

    private Raycast.RaycastHit getAverageGroundHit(Point3D carPosition) {
        double yaw = this.carAngle;
        Affine rotateY = new Affine();
        rotateY.appendRotation(yaw, Point3D.ZERO, Rotate.Y_AXIS);
        List<Raycast.RaycastHit> hits = new ArrayList<>();
        double carWidth = 1.5, carLength = 3.5, rayOffsetY = -1;
        Point3D down = new Point3D(0, 1, 0);
        Point3D[] localOffsets = new Point3D[]{
                new Point3D(-carWidth / 2, rayOffsetY,  carLength / 2),
                new Point3D( carWidth / 2, rayOffsetY,  carLength / 2),
                new Point3D(-carWidth / 2, rayOffsetY, -carLength / 2),
                new Point3D( carWidth / 2, rayOffsetY, -carLength / 2)
        };
        for (Point3D local : localOffsets) {
            Point3D rotated = rotateY.transform(local);
            Raycast.RaycastHit hit = safeRay.getIntersection(carPosition.add(rotated), down);
            if (hit != null) hits.add(hit);
        }
        if (hits.isEmpty()) return null;
        Point3D avgP = new Point3D(0,0,0), avgN = new Point3D(0,0,0);
        for (Raycast.RaycastHit h : hits) {
            avgP = avgP.add(h.point);
            avgN = avgN.add(h.normal);
        }
        int n = hits.size();
        avgP = avgP.multiply(1.0/n);
        avgN = avgN.normalize();
        return new Raycast.RaycastHit(avgP, avgN);
    }

    private Group loadModel(URL url) {
        Group modelroot = new Group();
        ObjModelImporter importer = new ObjModelImporter();
        try {
            importer.read(url);
            for (MeshView view : importer.getImport()) {
                view.setCullFace(CullFace.BACK);
                modelroot.getChildren().add(view);
            }
        } catch (ImportException e) {
            // if the only problem is a missing material, log and continue
            System.err.println("Warning: OBJ referenced missing material: " + e.getMessage());
            // optionally try a second pass without materials, or assign a default PhongMaterial here
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelroot;
    }

    public void handleKeyPressed(KeyEvent event) { pressedKeys.add(event.getCode()); }
    public void handleKeyReleased(KeyEvent event) { pressedKeys.remove(event.getCode()); }

    private void update(double deltaTime) {

        if (raceFinished || dnfShown) {
            return;
        }

        double rotateSpeed = 50,
                smoothing = 0.15,
                acceleration = 50;
        double decel = 80,
                maxVel = 73,
                friction = 40;


        if (pressedKeys.contains(KeyCode.A)) targetCarAngle -= rotateSpeed*deltaTime;
        if (pressedKeys.contains(KeyCode.D)) targetCarAngle += rotateSpeed*deltaTime;

        double diff = targetCarAngle - carAngle;
        carAngle += diff * smoothing;
        carCollisionBox.setRotationAxis(Rotate.Y_AXIS);
        carCollisionBox.setRotate(carAngle);

        double angleRad = Math.toRadians(carAngle);
        double dx = Math.sin(angleRad), dz = Math.cos(angleRad);

        Point3D down = new Point3D(0,1,0);
        Point3D carPos = new Point3D(car.getTranslateX(), car.getTranslateY(), car.getTranslateZ());
        Raycast.RaycastHit groundHit = getAverageGroundHit(carPos);



        if (groundHit != null) {

            double targetY = groundHit.point.getY() - 0.4;
            car.setTranslateY(targetY);
            carCollisionBox.setTranslateY(targetY);

            Point3D normal = new Point3D(-groundHit.normal.getX(),-groundHit.normal.getY(),-groundHit.normal.getZ()).normalize();
            Point3D forward = new Point3D(-Math.sin(Math.toRadians(carAngle)),0,-Math.cos(Math.toRadians(carAngle))).normalize();
            Point3D localX = forward.crossProduct(normal).normalize();

            if (localX.magnitude() < 0.001) localX = new Point3D(1,0,0).crossProduct(normal).normalize();
            Point3D localZ = normal.crossProduct(localX).normalize();
            Rotate rX = new Rotate(0, Rotate.X_AXIS);
            Rotate rZ = new Rotate(0, Rotate.Z_AXIS);
            Affine affine = new Affine(
                    localX.getX(), normal.getX(), localZ.getX(), 0,
                    localX.getY(), normal.getY(), localZ.getY(), 0,
                    localX.getZ(), normal.getZ(), localZ.getZ(), 0
            );

            car.getTransforms().setAll(rX, rZ, affine);
            carCollisionBox.setRotationAxis(Rotate.Y_AXIS);
            carCollisionBox.setRotate(carAngle);
        }

        if (safeRay.isRayHitting(carPos, down) && !raycast.isRayHitting(carPos, down)) {
            acceleration *= 0.8;
            maxVel *= 0.8;
        }

        if (pressedKeys.contains(KeyCode.W))      velocity += acceleration*deltaTime;
        else if (pressedKeys.contains(KeyCode.S)) velocity -= decel*deltaTime;
        else if (velocity>0) { velocity -= friction*deltaTime; if (velocity<0) velocity=0; }
        else if (velocity<0) { velocity += friction*deltaTime; if (velocity>0) velocity=0; }
        if (velocity>maxVel) velocity=maxVel;
        if (velocity< -maxVel/2) velocity=-maxVel/2;


        if (finishRay.isRayHitting(carPos, down) && !raceFinished) {
            finishRace();
            System.out.println("You finished the race!");
        }


        for (Box obs : obstacles) {
            if (checkCollision(carCollisionBox, obs)) { System.out.println("Collision!"); break; }
        }

        if (death.isRayHitting(carPos, down)) {
            if (!raceFinished && !dnfShown) {
                handleDNF("Crashed into wall! Be more careful next time.");
                System.out.println("You Crashed");
            }
//            velocity = 0;
            return;

        }

        if (Math.abs(velocity) > 0) {
            try {
                // Get current vehicle's fuel economy and adjust consumption based on it
                Vehicle currentVehicle = GameManager.getInstance().getCurrentVehicle();
                double fuelConsumptionRate = Math.abs(velocity) / (currentVehicle.getEffectiveFuelEconomy() * 25);
                currentVehicle.consumeFuel(fuelConsumptionRate * deltaTime);
            } catch (IllegalStateException e) {
                if (!raceFinished && !dnfShown) {
                    handleDNF("Out of fuel! You did not reach a gas station in time.");
                }
                velocity = 0;
                return;
            }
        }

        // Handle refueling at gas station
        if (gasstation.isRayHitting(carPos, down) && velocity == 0) {
            GameManager.getInstance().getCurrentVehicle().refuel();
        }

//        System.out.println(GameManager.getInstance().getCurrentVehicle().getFuelLevel() + "%" + " remaining");

        car.setTranslateX(car.getTranslateX()+dx*velocity*deltaTime);
        car.setTranslateZ(car.getTranslateZ()+dz*velocity*deltaTime);
        carCollisionBox.setTranslateX(car.getTranslateX());
        carCollisionBox.setTranslateZ(car.getTranslateZ());

        if (hudController != null) {
            //long currentTime = System.currentTimeMillis();
            //String formattedTime = CounterService.getFormattedElapsedTime(currentTime);
            //hudController.setElapsedTime(CounterService.getElapsedHours());
            //hudController.updateTime(formattedTime);

            hudController.updateFuel(GameManager.getInstance().getCurrentVehicle().getFuelLevel());
            hudController.setMoney(GameManager.getInstance().getPlayer().getMoney());
        }


    }

    public void setHudController(HUDController controller) {
        this.hudController = controller;
    }

    private void finishRace() {
        raceFinished = true;

        for (Bots bot : bots) {
            if (bot.hasFinished()) {
                playerPlacement++;
            }
        }

        counterService.stopRace();
        showRaceCompleteScreen();
    }

    private void showRaceCompleteScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RaceCompleteScreen.fxml"));
            Parent root = loader.load();
            RaceCompleteController controller = loader.getController();
            controller.setPlacement(playerPlacement);
            controller.setMainMenuReference(this);

            if (gameScene != null) {
                gameScene.setRoot(root);
                Stage stage = (Stage) gameScene.getWindow();
                stage.setFullScreen(true);
            } else {
                System.err.println("Game scene is null, cannot show race complete screen!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkCollision(Box a, Box b) {
        Bounds A = a.localToScene(a.getBoundsInLocal());
        Bounds B = b.localToScene(b.getBoundsInLocal());
        return A.intersects(B);
    }

    public void randomEvent() {
        RaceEvent event = RaceEvent.getRandomEvent();
        // show it immediately
        if (hudController != null) {
//            hudController.showEvent(event.getDescription());
            System.out.println(event.getDescription());
        }
        // apply auto effects
        GameManager.getInstance().getPlayer().addMoney(event.getMoneyChange());

        if (event == RaceEvent.STRANDED_TRAVELLER && personEvent != null) {
            personEvent.setVisible(true);
        } else if (event.isPleaseChoose() && hudController != null) {
            scheduleOneTimedEvent();
        }


    }

    public void scheduleOneTimedEvent() {
        int seconds = rng.nextInt(MAX_DELAY - MIN_DELAY + 1) + MIN_DELAY;
        System.out.println("Scheduling non-traveller event in " + seconds + "s");

        // cancel any previous just in case
        if (oneShotEventTimer != null) oneShotEventTimer.stop();

        oneShotEventTimer = new PauseTransition(Duration.seconds(seconds));
        oneShotEventTimer.setOnFinished(e -> fireRandomNonTravellerEvent());
        oneShotEventTimer.play();
    }


    private void fireRandomNonTravellerEvent() {
        RaceEvent event;
        do {
            event = RaceEvent.getRandomEvent();
        } while (event == RaceEvent.STRANDED_TRAVELLER);

        // show it
        if (hudController != null) {
//            hudController.showEvent(event.getDescription());
            System.out.println();
        }
        GameManager.getInstance().getPlayer().addMoney(event.getMoneyChange());

        if (hudController != null && event.isPleaseChoose()) {
            hudController.promptChoices(
                    event.getChoice1(),
                    event.getChoice2(),
                    choice -> {
                        if (choice == 1) System.out.println("Choose 1");
                        else            System.out.println("Choose 2");
                        hudController.updateTime(counterService.getFormattedElapsedTime());
                        hudController.setMoney(GameManager.getInstance().getPlayer().getMoney());
                    }
            );
        }
    }

    private void handleDNF(String reason) {
        if (dnfShown || raceFinished) {
            return;
        }

        dnfShown = true;
        raceFinished = true;
        counterService.stopRace();

        GameManager.getInstance().completeRace(0);
        showDNFScreen(reason);
    }

    private void showDNFScreen(String reason) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DNFRaceScreen.fxml"));
            Parent root = loader.load();
            DNFController controller = loader.getController();
            controller.setReason(reason);

            if (gameScene != null) {
                gameScene.setRoot(root);
                Stage stage = (Stage) gameScene.getWindow();
                stage.setFullScreen(true);
            } else {
                System.err.println("Game scene is null, cannot show DNF screen");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGameScene(Scene scene) {
        this.gameScene = scene;
    }

    private void showDNFScreenAlternative(String reason) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DNFRaceScreen.fxml"));
            Parent root = loader.load();
            DNFController controller = loader.getController();
            controller.setReason(reason);

            Scene currentScene = null;
            if (car != null && car.getScene() != null) {
                currentScene = car.getScene();
            } else if (camera != null && camera.getScene() != null) {
                currentScene = camera.getScene();
            }

            if (currentScene != null) {
                currentScene.setRoot(root);
                Stage stage = (Stage) currentScene.getWindow();
                stage.setFullScreen(true);
            } else {
                System.err.println("Cannot find scene to show DNF screen");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}