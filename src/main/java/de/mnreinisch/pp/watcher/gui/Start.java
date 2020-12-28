package de.mnreinisch.pp.watcher.gui;

import de.mnreinisch.pp.watcher.control.Alerts;
import de.mnreinisch.pp.watcher.control.GlobalHelper;
import de.mnreinisch.pp.watcher.control.LogInit;
import de.mnreinisch.pp.watcher.control.TransactionControl;
import de.mnreinisch.pp.watcher.control.dto.TransactionDTO;
import de.mnreinisch.pp.watcher.domain.TransactionRepository;
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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

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
    private LocalDate currentTime = new LocalDate();
    private static Start start;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionControl = new TransactionControl();
        addEventListener();
        start = this;
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

        Callback<TableColumn<TransactionDTO, Double>, TableCell<TransactionDTO, Double>> cellFactoryDouble = (TableColumn<TransactionDTO, Double> param) -> new EditCellDouble();
        amount.setCellFactory(cellFactoryDouble);

        Callback<TableColumn<TransactionDTO, String>, TableCell<TransactionDTO, String>> cellFactoryString = (TableColumn<TransactionDTO, String> param) -> new EditCellString();
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

    public static void reloadTable() {
        start.lbldate.setText(start.currentTime.toString("MM.yyyy"));

        List<TransactionDTO> allTransactions = start.transactionControl.getTransactionInMonth(start.currentTime);
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

    @FXML
    private void createAddDialog() {
        try {
            GUIStarter.createAddView();
        } catch (TechnicalException e) {
            Alerts.showError("Error while create view", "An error occurred while creating add view.\n" + e.getMessage(), true, globalHelper.getStartStage());
        }
    }

    private void createEditDialog(TransactionDTO transactionDTO) {

    }

    @FXML
    private void addMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, currentTime.getYear());
        c.set(Calendar.MONTH, currentTime.getMonthOfYear() - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, 1);
        currentTime = new LocalDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 1);
        reloadTable();
    }

    @FXML
    private void subMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, currentTime.getYear());
        c.set(Calendar.MONTH, currentTime.getMonthOfYear() - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, -1);
        currentTime = new LocalDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 1);
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

    public static <T extends Event> void exitApplication(T t) {
        TransactionRepository.closeConn();
        Platform.exit();
    }
}
