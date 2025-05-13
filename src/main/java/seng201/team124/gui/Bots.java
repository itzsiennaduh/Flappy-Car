package seng201.team124.gui;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;

import java.util.List;

public class Bots {

    private final Group model; // 3D model 
    private int currentWaypointIndex = 0; // Index of the current waypoint in the list of waypoints 
    private final List<Point3D> waypoints; // List of waypoints to follow. Each waypoint is a 3D point in space.
    private double velocity = 0; // Current velocity 
    private double deceleration = 0.02; // How much it is going to slow down

    
    // Constructor for the bot class
    public Bots(Group model, List<Point3D> waypoints) {
        this.model = model;
        this.waypoints = waypoints;
    }

    // Getter for the bot's 3D model
    public Group getModel() {
        return model;
    }


    public void update(double deltaTime) {
        // Check if there are any waypoints to follow
        if (waypoints == null || waypoints.isEmpty()) return;
        if (currentWaypointIndex >= waypoints.size()) return;

        double rotateSpeed = 270; // how much the bot can turn degrees per second
        double acceleration = 50; // Acceleration of the bot


        Point3D toTarget = getPoint3D();
        double distance = toTarget.magnitude();

        // Checks if the bot is close enough to the target waypoint then updates the waypoint index
        if (distance < 5.0) {
            currentWaypointIndex++;
            return;
        }

        // Acceleration
        velocity += acceleration * deltaTime;
        double maxSpeed = 150;
        if (velocity > maxSpeed) velocity = maxSpeed;

        // Gets the current forward direction based on rotation
        double angleRad = Math.toRadians(model.getRotate());
        Point3D forward = new Point3D(Math.sin(angleRad), 0, Math.cos(angleRad));

        // Makes sure the bot only drives forwards not sideways
        Point3D movement = forward.multiply(velocity * deltaTime);
        model.setTranslateX(model.getTranslateX() + movement.getX());
        model.setTranslateZ(model.getTranslateZ() + movement.getZ());

        // Smoothen the turning
        double desiredAngle = Math.toDegrees(Math.atan2(toTarget.getX(), toTarget.getZ()));
        double currentAngle = model.getRotate();
        double angleDiff = normalizeAngle(desiredAngle - currentAngle);

        double maxTurn = rotateSpeed * deltaTime;
        double clampedTurn = Math.max(-maxTurn, Math.min(maxTurn, angleDiff));

        double newAngle = currentAngle + clampedTurn;
        model.setRotationAxis(Rotate.Y_AXIS);
        model.setRotate(newAngle);
    }

    // Gets the distance between the bot and the target waypoint
    private Point3D getPoint3D() {
        Point3D pos = new Point3D(model.getTranslateX(), model.getTranslateY(), model.getTranslateZ()); // Gets the current 3D position of the bot
        Point3D target = waypoints.get(currentWaypointIndex); // Gets the target waypoint

        // Removes the y-axes value from the waypoints to make them 2D
        Point3D flatPos = new Point3D(pos.getX(), 0, pos.getZ());

        Point3D currentTarget = waypoints.get(currentWaypointIndex);
        Point3D flatCurrentTarget = new Point3D(currentTarget.getX(), 0, currentTarget.getZ());

        int lookAheadDistance = Math.min(3, waypoints.size() - 1 - currentWaypointIndex);
        Point3D lookAheadTarget = null;
        if (lookAheadDistance > 0) {
            Point3D nextTarget = waypoints.get(currentWaypointIndex + lookAheadDistance);
            Point3D flatNextTarget = new Point3D(nextTarget.getX(), 0, nextTarget.getZ());
            Point3D directionToNext = flatNextTarget.subtract(flatCurrentTarget).normalize();
            lookAheadTarget = flatCurrentTarget.add(directionToNext.multiply(5 * lookAheadDistance));
        }
        return lookAheadTarget.subtract(flatPos);
    }

    // makes sure that the angle is between -180 and 180 degrees
    private double normalizeAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

}

