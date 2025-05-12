package seng201.team124.gui;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.List;

public class Raycast {

    private final List<Triangle> triangles = new ArrayList<>();

    // Triangle class to hold 3 points
    private static class Triangle {
        Point3D v0, v1, v2;

        Triangle(Point3D v0, Point3D v1, Point3D v2) {
            this.v0 = v0;
            this.v1 = v1;
            this.v2 = v2;
        }
    }

    // Initialize once
    public void MeshRaycaster(MeshView meshView) {
        TriangleMesh mesh = (TriangleMesh) meshView.getMesh();

        float[] points = new float[mesh.getPoints().size()];
        mesh.getPoints().toArray(points);

        int[] faces = new int[mesh.getFaces().size()];
        mesh.getFaces().toArray(faces);

        for (int i = 0; i < faces.length; i += 6) {
            int i0 = faces[i] * 3;
            int i1 = faces[i + 2] * 3;
            int i2 = faces[i + 4] * 3;

            Point3D v0 = meshView.localToScene(points[i0], points[i0 + 1], points[i0 + 2]);
            Point3D v1 = meshView.localToScene(points[i1], points[i1 + 1], points[i1 + 2]);
            Point3D v2 = meshView.localToScene(points[i2], points[i2 + 1], points[i2 + 2]);


            triangles.add(new Triangle(v0, v1, v2));
        }
    }


    // Perform the ray test
    public boolean isRayHitting(Point3D origin, Point3D direction) {
        for (Triangle tri : triangles) {
            if (rayIntersectsTriangle(origin, direction, tri.v0, tri.v1, tri.v2)) {
                return true;
            }
        }
        return false;
    }

    // Möller–Trumbore algorithm
    private boolean rayIntersectsTriangle(Point3D origin, Point3D dir, Point3D v0, Point3D v1, Point3D v2) {
        final double EPSILON = 1e-6;
        Point3D edge1 = v1.subtract(v0);
        Point3D edge2 = v2.subtract(v0);
        Point3D h = dir.crossProduct(edge2);
        double a = edge1.dotProduct(h);
        if (Math.abs(a) < EPSILON) return false;

        double f = 1.0 / a;
        Point3D s = origin.subtract(v0);
        double u = f * s.dotProduct(h);
        if (u < 0.0 || u > 1.0) return false;

        Point3D q = s.crossProduct(edge1);
        double v = f * dir.dotProduct(q);
        if (v < 0.0 || u + v > 1.0) return false;

        double t = f * edge2.dotProduct(q);
        return t > EPSILON;
    }
}
