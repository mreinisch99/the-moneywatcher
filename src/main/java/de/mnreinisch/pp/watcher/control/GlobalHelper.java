package de.mnreinisch.pp.watcher.control;

import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GlobalHelper {
    private Stage startStage;
    private Stage addStage;
    private Stage configStage;

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

    public Stage getConfigStage() {
        return configStage;
    }

    public void setConfigStage(Stage configStage) {
        this.configStage = configStage;
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

    public static double round(double number){
        BigDecimal bigDecimal = BigDecimal.valueOf(number);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
