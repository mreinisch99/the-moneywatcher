package de.mnreinisch.pp.watcher;

import de.mnreinisch.pp.watcher.control.Alerts;
import de.mnreinisch.pp.watcher.control.GlobalHelper;
import de.mnreinisch.pp.watcher.control.LogInit;
import de.mnreinisch.pp.watcher.domain.TransactionRepository;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;
import de.mnreinisch.pp.watcher.gui.GUIStarter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    public static final double WIDTH = 700.0;
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
            TransactionRepository.closeConn();
            Platform.exit();
//            System.exit(0);
        }
    }

    private void createGeneralFolder() {
        File mainLogFolder = new File(".\\logs\\");
        if (!mainLogFolder.exists()) {
            mainLogFolder.mkdir();
        }

        File mainConfigFolder = new File(".\\config\\");
        if (!mainConfigFolder.exists()) {
            mainConfigFolder.mkdir();
        }

        File dbFolder = new File(mainConfigFolder, "db");
        if(!dbFolder.exists()){
            dbFolder.mkdir();
            copy(getClass().getResourceAsStream("./db/transactions.sqlite"),"./config/db/transactions.sqlite");
        }
    }

    public static void copy(InputStream source , String destination) {
        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IllegalAccessError("Error while copying new database.\n" + ex.getMessage());
        }

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
