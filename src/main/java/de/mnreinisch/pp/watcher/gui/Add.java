package de.mnreinisch.pp.watcher.gui;

import de.mnreinisch.pp.watcher.control.Alerts;
import de.mnreinisch.pp.watcher.control.GlobalHelper;
import de.mnreinisch.pp.watcher.control.TransactionControl;
import de.mnreinisch.pp.watcher.domain.Date;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

import static de.mnreinisch.pp.watcher.control.GlobalHelper.round;

public class Add implements Initializable {
    @FXML
    private CheckBox cbVAC;
    @FXML
    private TextField tfDay;
    @FXML
    private TextField tfInfo;
    @FXML
    private TextField tfAmount;

    private TransactionControl transactionControl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transactionControl = new TransactionControl();
    }

    @FXML
    private void add() {
        String text = tfAmount.getText();
        text = text.replaceAll(",", ".");

        if(text.isEmpty() || !GlobalHelper.isNumeric(text)) {
            Alerts.showWarning("Missing amount", "Please provide a value for amount!\nNo separation for thousands!\nCents separation with ',' or '.' possible!", false, null);
            return;
        }

        if(tfInfo.getText().isEmpty()) {
            Alerts.showWarning("Missing info", "Please provide a info!", false, null);
            return;
        }

        String day = tfDay.getText();
        if(day.isEmpty() || !Date.isValidDate(day)) {
            Alerts.showWarning("Missing day", "Please provide a day! Format: dd.MM.yyyy", false, null);
            return;
        }

        double amount = round(Double.parseDouble(text));

        try{
            transactionControl.addTransaction(amount, tfInfo.getText(), cbVAC.isSelected(), null, day);
            Start.reloadTable();
            GlobalHelper.getInstance().getAddStage().close();
        } catch(Throwable e){
            Alerts.showError("Error while adding", "An error occurred while adding a new transaction.\n" + e.getMessage(), true, null);
        }
    }
}
