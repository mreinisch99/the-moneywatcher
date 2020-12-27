package de.mnreinisch.pp.watcher.gui;

import de.mnreinisch.pp.watcher.Main;
import de.mnreinisch.pp.watcher.control.GlobalHelper;
import de.mnreinisch.pp.watcher.control.Settings;
import de.mnreinisch.pp.watcher.domain.Setting;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIStarter {
    public static final String DARK_THEME_SHEET = GUIStarter.class.getResource("./css/dark.css").toExternalForm();

    public static void generateStartView() throws TechnicalException {
        try{
            GlobalHelper gh = GlobalHelper.getInstance();
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(GUIStarter.class.getResource("./start.fxml"));

            Parent load = fxmlLoader.load();
            Stage dashboard = createNewGUI(load, "Dashboard");
            dashboard.setOnCloseRequest(Start::exitApplication);

            Setting setting = Settings.getInstance().getSetting();
            if(setting.getTheme() == Settings.DARK_THEME){
                dashboard.getScene().getStylesheets().add(DARK_THEME_SHEET);
            }

            gh.setStartStage(dashboard);

        } catch (IOException e) {
            throw new TechnicalException("Couldn't load Startview.\n" +e.getMessage());
        }
    }

    public static void reloadTheme(){
        GlobalHelper globalHelper = GlobalHelper.getInstance();
        Setting setting = Settings.getInstance().getSetting();
        changeTheme(globalHelper.getStartStage(), setting.getTheme());
    }

    private static void changeTheme(Stage stage, int theme){
        if(stage == null) return;
        Scene scene = stage.getScene();
        if(theme == Settings.LIGHT_THEME){
            scene.getStylesheets().clear();
        } else {
            scene.getStylesheets().add(DARK_THEME_SHEET);
        }
    }

    private static Stage createNewGUI(Parent estPar, String title) {
        GlobalHelper globalHelper = GlobalHelper.getInstance();
        Stage newStage = new Stage();
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setTitle("The Moneywatcher - " + title);
        newStage.setScene(new Scene(estPar));

        if (globalHelper.getStartStage() != null) {
            newStage.setX(globalHelper.getStartStage().getX() + 50.0);
            newStage.setY(globalHelper.getStartStage().getY() + 50.0);
        } else {
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            newStage.setX((primScreenBounds.getWidth() - Main.WIDTH) / 2 - 100.0);
            newStage.setY((primScreenBounds.getHeight() - Main.HEIGHT) / 2 - 100.0);
        }
        newStage.show();
        return newStage;
    }
}
