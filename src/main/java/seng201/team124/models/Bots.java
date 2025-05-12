package seng201.team124.models;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;

import java.util.List;

public class Bots {

    private Group model; // 3D model node
    private Point3D direction;
    private int currentWaypointIndex = 0;
    private List<Point3D> waypoints;
    private double velocity = 0;
    private double maxSpeed = 1.0;
    private double acceleration = 0.05;
    private double deceleration = 0.02;
    private double turnSpeed = 2.0; // degrees per frame



    private double speed = 0.75;

    public Bots(Group model, List<Point3D> waypoints) {
        this.model = model;
        this.waypoints = waypoints;
        this.direction = new Point3D(1, 0, 0); // default
    }

    public Group getModel() {
        return model;
    }

    public void update(double deltaTime) {
        if (waypoints == null || waypoints.isEmpty()) return;

        // Make sure we don't go past the final waypoint
        if (currentWaypointIndex >= waypoints.size()) return;

        Point3D pos = new Point3D(model.getTranslateX(), model.getTranslateY(), model.getTranslateZ());
        Point3D target = waypoints.get(currentWaypointIndex);
        System.out.println(target);

        // Only use XZ plane
        Point3D flatPos = new Point3D(pos.getX(), 0, pos.getZ());
        Point3D flatTarget = new Point3D(target.getX(), 0, target.getZ());
        Point3D toTarget = flatTarget.subtract(flatPos);

        double distance = toTarget.magnitude();

        if (distance < 0.5) {
            if (currentWaypointIndex + 1 < waypoints.size()) {
                currentWaypointIndex++;
            } else {
                return; // Final checkpoint reached
            }
            return; // Skip movement this frame after increment
        }

        velocity = acceleration * deltaTime;
        if (velocity > maxSpeed) velocity = maxSpeed;

        // Normalize and move
        Point3D direction = toTarget.normalize();
        Point3D movement = new Point3D(direction.getX() + velocity * deltaTime, 0, direction.getZ() + velocity * deltaTime);

        model.setTranslateX(pos.getX() + movement.getX());
        model.setTranslateZ(pos.getZ() + movement.getZ());

        // Optional: rotate to face direction
        double angle = Math.toDegrees(Math.atan2(direction.getX(), direction.getZ()));
        model.setRotationAxis(Rotate.Y_AXIS);
        model.setRotate(angle);
    }




}

