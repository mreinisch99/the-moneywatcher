package de.mnreinisch.pp.watcher.gui;

import de.mnreinisch.pp.watcher.control.dto.TransactionDTO;
import de.mnreinisch.pp.watcher.control.Alerts;
import de.mnreinisch.pp.watcher.control.GlobalHelper;
import de.mnreinisch.pp.watcher.control.LogInit;
import de.mnreinisch.pp.watcher.control.Settings;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Start implements Initializable {
    @FXML
    private MenuItem close;


    private GlobalHelper globalHelper = GlobalHelper.getInstance();
    private Settings settings = Settings.getInstance();
    private static Start start;
    private static Logger LOGGER = LogInit.LOGGER;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        start = this;
        try{
            settings.checkSettings();

        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Error while loading Dashboard.\n" + e.getMessage() + "\nApplication will be closed!", e);
            Alerts.showError("Error on startup", "Error while loading Dashboard.\n" + e.getMessage() + "\nApplication will be closed!", true, globalHelper.getStartStage());
            Platform.exit();
        }
        addEventListener();

    }

    @FXML
    private void createAddDialog() {

    }

    private void createEditDialog(TransactionDTO transactionDTO){

    }

    private void deleteTransaction(TransactionDTO transactionDTO){

    }

    private void addEventListener() {
        close.setOnAction(Start::exitApplication);
    }

    public static <T extends Event> void exitApplication(T t) {
        Platform.exit();
    }
}
