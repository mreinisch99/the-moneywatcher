package de.mnreinisch.pp.watcher.control;

import javafx.stage.Stage;

public class GlobalHelper {
    private Stage startStage;
    private Stage addStage;

    private static GlobalHelper globalHelper;

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

    public Stage getAddStage() {
        return addStage;
    }

    public void setAddStage(Stage addStage) {
        this.addStage = addStage;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
