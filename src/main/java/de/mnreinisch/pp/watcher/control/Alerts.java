package de.mnreinisch.pp.watcher.control;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Alerts {

    public static void showError(String title, String content, boolean wait, Stage stage) {
        showDialog(title, content, wait, stage, Alert.AlertType.ERROR);
    }

    public static void showWarning(String title, String content, boolean wait, Stage stage) {
        showDialog(title, content, wait, stage, Alert.AlertType.WARNING);
    }

    private static void showDialog(String title, String content, boolean wait, Stage stage, Alert.AlertType information) {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(information);
            alertBody(title, content, alert, wait, stage);
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(information);
                alertBody(title, content, alert, wait, stage);
            });
        }
    }

    private static void alertBody(String title, String content, Alert alert, boolean wait, Stage pStage) {
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        if(pStage != null){
            alert.initOwner(pStage);
            pStage.show();
            pStage.toFront();
        }

        stage.setAlwaysOnTop(true);
        if (wait) {
            alert.showAndWait();
        } else {
            alert.show();
        }
    }

}
