package de.mnreinisch.pp.watcher.control;

import javafx.stage.Stage;

public class GlobalHelper {
    private Stage startStage;

    private static GlobalHelper globalHelper;
    private static Settings settings;

    public static GlobalHelper getInstance(){
        if(globalHelper == null){
            globalHelper = new GlobalHelper();
        }

        return globalHelper;
    }

    private GlobalHelper() {}

    public Stage getStartStage() {
        return startStage;
    }

    public void setStartStage(Stage startStage) {
        this.startStage = startStage;
    }
}
