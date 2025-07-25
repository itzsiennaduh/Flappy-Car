package seng201.team124.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng201.team124.gui.importantControllers.MainController;

import java.io.IOException;

/**
 * Class starts the javaFX application window
 * @author seng201 teaching team
 */
public class MainWindow extends Application {

    /**
     * Opens the gui with the fxml content specified in resources/fxml/main.fxml
     * @param primaryStage The current fxml stage, handled by javaFX Application class
     * @throws IOException if there is an issue loading fxml file
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = baseLoader.load();

        primaryStage.setTitle("Flappy Car"); // sets the title of the stage
        MainController ctrl = baseLoader.getController();
        Scene scene = new Scene(root, 600, 400); // creates a scene with the root node and dimensions
        ctrl.setScene(scene);
        primaryStage.setScene(scene); // add the scene to the stage
        primaryStage.setFullScreen(true); // makes it full screen
        primaryStage.show(); // To show the stage
    }

    /**
     * Launches the FXML application, this must be called from another class (in this case App.java) otherwise JavaFX
     * errors out and does not run
     * @param args command line arguments
     */
    public static void launchWrapper(String [] args) {
        launch(args);
    }

}
