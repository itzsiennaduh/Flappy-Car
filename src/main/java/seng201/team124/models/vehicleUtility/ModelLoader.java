package seng201.team124.models.vehicleUtility;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.Group;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;

import java.net.URL;

public class ModelLoader {
    public static Group loadModel(URL url) {
        return getGroup(url);
    }

    public static Group getGroup(URL url) {
        Group modelRoot = new Group();
        ObjModelImporter importer = new ObjModelImporter();
        try {
            importer.read(url);
            for (MeshView view : importer.getImport()) {
                view.setCullFace(CullFace.BACK);
                modelRoot.getChildren().add(view);
            }
        } catch (ImportException e) {
            // if the only problem is a missing material, log and continue
            System.err.println("Warning: OBJ referenced missing material: " + e.getMessage());
            // optionally try a second pass without materials, or assign a default PhongMaterial here
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelRoot;
    }
}
