package seng201.team124.gui.loadingScreen;

import javafx.concurrent.Task;
import javafx.scene.Group;
import seng201.team124.gui.importantcontrollers.GameController;

public class GameLoadTask extends Task<Group> {
    private final GameController controller;

    public GameLoadTask(GameController ctrl) {
        this.controller = ctrl;
    }

    @Override
    protected Group call() throws Exception {
        updateMessage("Initializing game...");
        updateProgress(0, 100);

        updateMessage("Loading assets...");
        updateProgress(30, 100);
        Thread.sleep(300);               // optional pacing

        updateMessage("Building scene...");
        Group gameRoot = controller.setupGameRootNode();
        if (gameRoot == null) {
            throw new Exception("Failed to construct game root node");
        }

        updateMessage("Finalizing...");
        updateProgress(100, 100);
        Thread.sleep(300);

        updateMessage("Done!");
        return gameRoot;
    }
}
