package de.mnreinisch.pp.watcher;

import de.mnreinisch.pp.watcher.control.Alerts;
import de.mnreinisch.pp.watcher.control.GlobalHelper;
import de.mnreinisch.pp.watcher.control.LogInit;
import de.mnreinisch.pp.watcher.gui.GUIStarter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    public static final double WIDTH = 600.0;
    public static final double HEIGHT = 400.0;
    private static final Logger LOGGER = LogInit.LOGGER;
    private GlobalHelper globalHelper = GlobalHelper.getInstance();


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        try{
            createGeneralFolder();
            LogInit.initializeLogger();
            Platform.setImplicitExit(false);
            GUIStarter.generateStartView();
        } catch(Throwable throwable){
            LOGGER.log(Level.SEVERE, "Error while starting application.\n" + throwable.getMessage(), throwable);
            Alerts.showError("Error on startup", "Error while starting application.\n" + throwable.getMessage() + "\nApplication will be closed!", true, globalHelper.getStartStage());
            Platform.exit();
        }
    }

    private void createGeneralFolder() {
        File mainLogFolder = new File(".\\logs\\");
        if (!mainLogFolder.exists()) {
            mainLogFolder.mkdir();
        }

        File mainConfigFolder = new File(".\\configs\\");
        if (!mainConfigFolder.exists()) {
            mainConfigFolder.mkdir();
        }

        File dbFolder = new File(mainConfigFolder, "db");
        if(!dbFolder.exists()){
            dbFolder.mkdir();
        }
    }
}
