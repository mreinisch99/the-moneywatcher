package de.mnreinisch.pp.watcher.control;

import de.mnreinisch.pp.watcher.domain.ButtonTypes;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Alerts {
    public static void showInformation(String title, String content, boolean wait, Stage stage) {
        showDialog(title, content, wait, stage, Alert.AlertType.INFORMATION, "Information");
    }

    public static void showError(String title, String content, boolean wait, Stage stage) {
        showDialog(title, content, wait, stage, Alert.AlertType.ERROR, "Fehler");
    }

    public static void showWarning(String title, String content, boolean wait, Stage stage) {
        showDialog(title, content, wait, stage, Alert.AlertType.WARNING, "Warnung");
    }

    public static String showInputDialog(String title, String content, Stage stage){
        if(Platform.isFxApplicationThread()){
            return createAlert(title, content, stage);
        } else {
            final FutureTask query = new FutureTask(() -> createAlert(title, content, stage));
            Platform.runLater(query);
            try {
                return (String) query.get();
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        }
    }

    private static String createAlert(String title, String content, Stage stage){
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle(title);
        textInputDialog.setContentText(content);
        if(stage != null){
            textInputDialog.initOwner(stage);
        }
        Optional<String> result = textInputDialog.showAndWait();
        if(result.isPresent()){
            return result.get();
        }
        return null;
    }

    private static void showDialog(String title, String content, boolean wait, Stage stage, Alert.AlertType information, String information2) {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(information);
            alertBody(title, content, alert, wait, information2, stage);
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(information);
                alertBody(title, content, alert, wait, information2, stage);
            });
        }
    }

    private static void alertBody(String title, String content, Alert alert, boolean wait, String alertType, Stage pStage) {
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

    public static ButtonType showConfirmation(String title, String content, Stage stage, ButtonType... buttonTypes) {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> buttonType = confirmationBody(alert, title, content, stage, buttonTypes);
            return buttonType.orElse(null);
        } else {
            final FutureTask query = new FutureTask(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                return confirmationBody(alert, title, content, stage, buttonTypes);
            });
            Platform.runLater(query);
            try {
                return ((Optional<ButtonType>) query.get()).orElse(null);
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
        return null;
    }

    private static Optional<ButtonType> confirmationBody(Alert alert, String title, String content, Stage pStage, ButtonType... buttonTypes) {
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(buttonTypes);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        alert.initOwner(pStage);
        stage.setAlwaysOnTop(false);
        return alert.showAndWait();
    }
}
