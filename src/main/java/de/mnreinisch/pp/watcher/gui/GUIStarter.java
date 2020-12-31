package de.mnreinisch.pp.watcher.gui;

import de.mnreinisch.pp.watcher.Main;
import de.mnreinisch.pp.watcher.control.GlobalHelper;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GUIStarter {
    public static void generateStartView() throws TechnicalException {
        try{
            GlobalHelper gh = GlobalHelper.getInstance();
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(GUIStarter.class.getResource("start.fxml"));

            Parent load = fxmlLoader.load();
            Stage dashboard = createNewGUI(load, "Dashboard");

            dashboard.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, Start::exitApplication);

            gh.setStartStage(dashboard);
            Start.reloadTable();

        } catch (IOException e) {
            throw new TechnicalException("Couldn't load start-view.\n" +e.getMessage(), e);
        }
    }

    public static void generateConfigView() throws TechnicalException {
        try{
            GlobalHelper gh = GlobalHelper.getInstance();
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(GUIStarter.class.getResource("config.fxml"));

            Parent load = fxmlLoader.load();
            Stage config = createNewGUI(load, "Configuration");

            config.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, Config::exitApplication);

            gh.setConfigStage(config);

        } catch (IOException e) {
            throw new TechnicalException("Couldn't load start-view.\n" +e.getMessage());
        }
    }

    public static void generateAddView() throws TechnicalException {
        try{
            GlobalHelper gh = GlobalHelper.getInstance();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(GUIStarter.class.getResource("add.fxml"));
            Parent load = fxmlLoader.load();
            Stage add = createNewGUI(load, "Add transaction");
            gh.setAddStage(add);
        } catch (IOException e) {
            throw new TechnicalException("Couldn't load add-view.\n" +e.getMessage());
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
