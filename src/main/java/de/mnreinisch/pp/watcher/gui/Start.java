package de.mnreinisch.pp.watcher.gui;

import de.mnreinisch.pp.watcher.control.*;
import de.mnreinisch.pp.watcher.control.dto.TransactionDTO;
import de.mnreinisch.pp.watcher.domain.EMFactory;
import de.mnreinisch.pp.watcher.domain.exceptions.CustomException;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.joda.time.LocalDate;

import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static de.mnreinisch.pp.watcher.control.GlobalHelper.round;

public class Start implements Initializable {
    @FXML
    private TableView<TransactionDTO> tvtran;
    @FXML
    private TableColumn<TransactionDTO, String> day;
    @FXML
    private TableColumn<TransactionDTO, Double> amount;
    @FXML
    private TableColumn<TransactionDTO, String> info;
    @FXML
    private TableColumn<TransactionDTO, CheckBox> vac;
    /*@FXML
    private TableColumn<TransactionDTO, String> image;*/
    @FXML
    private TableColumn<TransactionDTO, Button> action;
    @FXML
    private Label lbldate;
    @FXML
    private Label lblsum;

    @FXML
    private MenuItem close;


    private GlobalHelper globalHelper = GlobalHelper.getInstance();
    private TransactionControl transactionControl;
    private ConfigurationControl configurationControl;
    private LocalDate currentTime = new LocalDate();
    private LocalDate endTime = new LocalDate();
    private int startOfMonth = 1;
    private static Start start;

    public static <T extends Event> void exitApplication(T t) {
        EMFactory.closeConnection();
        Platform.exit();
    }

    public static void reloadTable() {

        start.lbldate.setText(start.currentTime.toString("dd.MM.yyyy") + " - " + start.endTime.toString("dd.MM.yyyy"));

        List<TransactionDTO> allTransactions = start.transactionControl.getTransactionInRange(start.currentTime, start.endTime);
        Collections.sort(allTransactions);
        Collections.reverse(allTransactions);
        double sum = round(allTransactions
                .stream()
                .mapToDouble(TransactionDTO::getAmount)
                .sum());

        start.lblsum.setText("Total: " + sum + "€");

        if(sum < 0.0){
            start.lblsum.setTextFill(Color.rgb(255,0,0));
        } else if(sum == 0.0) {
            start.lblsum.setTextFill(Color.rgb(255,255,255));
        } else {
            start.lblsum.setTextFill(Color.rgb(48,170,0));
        }

        start.tvtran.setItems(FXCollections.observableList(allTransactions));
    }

    public static void reloadConfiguration(){
        try {
            start.startOfMonth = start.configurationControl.getStartDay();
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, start.currentTime.getYear());
            c.set(Calendar.MONTH, start.currentTime.getMonthOfYear() - 1);
            c.set(Calendar.DAY_OF_MONTH, start.startOfMonth);
            start.setTimes(c);
            reloadTable();
        } catch (TechnicalException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionControl = new TransactionControl();
        configurationControl = new ConfigurationControl();
        start = this;

        reloadConfiguration();
        addEventListener();
    }

    private void addEventListener() {
        setTableProps();
        tvtran.setEditable(true);
        close.setOnAction(Start::exitApplication);

    }

    private void setTableProps() {
        int cols = tvtran.getColumns().size();
        day.prefWidthProperty().bind(tvtran.widthProperty().divide(cols));
        amount.prefWidthProperty().bind(tvtran.widthProperty().divide(cols));
        info.prefWidthProperty().bind(tvtran.widthProperty().divide(cols));
        vac.prefWidthProperty().bind(tvtran.widthProperty().divide(cols));
//        image.prefWidthProperty().bind(tvtran.widthProperty().divide(cols));
        action.prefWidthProperty().bind(tvtran.widthProperty().divide(cols));

        day.setSortable(true);
        vac.setSortable(true);
        amount.setSortable(false);
        info.setSortable(false);
//        image.setSortable(false);

        Callback<TableColumn<TransactionDTO, Double>, TableCell<TransactionDTO, Double>> cellFactoryDouble = (TableColumn<TransactionDTO, Double> param) -> new EditCellDouble<>();
        amount.setCellFactory(cellFactoryDouble);

        Callback<TableColumn<TransactionDTO, String>, TableCell<TransactionDTO, String>> cellFactoryString = (TableColumn<TransactionDTO, String> param) -> new EditCellString<>();
        day.setCellFactory(cellFactoryString);
        info.setCellFactory(cellFactoryString);

        day.setCellValueFactory(new PropertyValueFactory<>("Date"));
        amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        info.setCellValueFactory(new PropertyValueFactory<>("Info"));
//        image.setCellValueFactory(new PropertyValueFactory<>("ImgSrc"));

        day.setOnEditCommit(t -> {
            TransactionDTO transactionDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
            transactionDTO.setDate(t.getNewValue());
            updateTransaction(transactionDTO);
        });
        amount.setOnEditCommit(t -> {
            TransactionDTO transactionDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
            transactionDTO.setAmount(round(t.getNewValue()));
            updateTransaction(transactionDTO);
        });
        info.setOnEditCommit(t -> {
            TransactionDTO transactionDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
            transactionDTO.setInfo(t.getNewValue());
            updateTransaction(transactionDTO);
        });

        vac.setCellValueFactory(arg0 -> {
            TransactionDTO transaction = arg0.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(transaction.isVac());
            checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                transaction.setVac(new_val);
                updateTransaction(transaction);
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        action.setCellValueFactory(arg0 -> {
            TransactionDTO transactionDTO = arg0.getValue();
            Button delBtn = new Button("Delete");
            delBtn.setOnAction((e) -> deleteTransaction(transactionDTO));
            return new SimpleObjectProperty<>(delBtn);
        });
    }

    private void updateTransaction(TransactionDTO transactionDTO) {
        try {
            transactionControl.updateEntity(transactionDTO);
            reloadTable();
        } catch (CustomException e) {
            Alerts.showWarning("Error while updating", "An error occurred while updating transaction.\n" + e.getMessage(), false, globalHelper.getStartStage());
        }
    }


    @FXML
    private void createAddDialog() {
        try {
            GUIStarter.generateAddView();
        } catch (TechnicalException e) {
            Alerts.showError("Error while create view", "An error occurred while creating add view.\n" + e.getMessage(), true, globalHelper.getStartStage());
        }
    }

    @FXML
    private void createEditConfigDialog() {
        try {
            GUIStarter.generateConfigView();
        } catch (TechnicalException e) {
            Alerts.showError("Error while create view", "An error occurred while creating config view.\n" + e.getMessage(), true, globalHelper.getStartStage());
        }
    }

    @FXML
    private void addMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, currentTime.getYear());
        c.set(Calendar.MONTH, currentTime.getMonthOfYear() - 1);
        c.set(Calendar.DAY_OF_MONTH, startOfMonth);
        c.add(Calendar.MONTH, 1);
        setTimes(c);
        reloadTable();
    }

    private void setTimes(Calendar c) {
        currentTime = new LocalDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));

        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        endTime = new LocalDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
    }

    @FXML
    private void subMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, currentTime.getYear());
        c.set(Calendar.MONTH, currentTime.getMonthOfYear() - 1);
        c.set(Calendar.DAY_OF_MONTH, startOfMonth);
        c.add(Calendar.MONTH, -1);
        setTimes(c);
        reloadTable();
    }

    private void deleteTransaction(TransactionDTO transactionDTO) {
        try {
            transactionControl.removeTransaction(transactionDTO);
            reloadTable();
        } catch (CustomException e) {
            Alerts.showWarning("Error while deleting", "An error occurred while deleting selected transaction.\n" + e.getMessage(), false, globalHelper.getStartStage());
        }
    }
}
