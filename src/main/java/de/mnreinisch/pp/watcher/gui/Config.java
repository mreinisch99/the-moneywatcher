package de.mnreinisch.pp.watcher.gui;

import de.mnreinisch.pp.watcher.control.Alerts;
import de.mnreinisch.pp.watcher.control.ConfigurationControl;
import de.mnreinisch.pp.watcher.control.GlobalHelper;
import de.mnreinisch.pp.watcher.control.dto.ConfigurationDTO;
import de.mnreinisch.pp.watcher.domain.exceptions.CustomException;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Config implements Initializable {
    @FXML private TableView<ConfigurationDTO> tvconfig;
    @FXML private TableColumn<ConfigurationDTO, String> key;
    @FXML private TableColumn<ConfigurationDTO, String> val;
    private ConfigurationControl configurationControl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurationControl = new ConfigurationControl();
        addEventListener();
    }

    private void addEventListener(){
        setTableProps();
        List<ConfigurationDTO> allConfigs = configurationControl.getAllConfigs();
        tvconfig.setItems(FXCollections.observableList(allConfigs));
    }

    private void setTableProps() {
        tvconfig.setEditable(true);
        int size = tvconfig.getColumns().size();
        key.prefWidthProperty().bind(tvconfig.widthProperty().divide(size));
        val.prefWidthProperty().bind(tvconfig.widthProperty().divide(size));

        key.setSortable(true);
        val.setSortable(false);
        Callback<TableColumn<ConfigurationDTO, String>, TableCell<ConfigurationDTO, String>> cellFactoryString = (TableColumn<ConfigurationDTO, String> param) -> new EditCellString<>();
        val.setCellFactory(cellFactoryString);

        val.setOnEditCommit(t -> {
            int row = t.getTablePosition().getRow();
            ConfigurationDTO configurationDTO = t.getTableView().getItems().get(row);
            String oldVal = configurationDTO.getValue();
            configurationDTO.setValue(t.getNewValue());

            try{
                configurationControl.editConfiguration(configurationDTO);
            } catch (CustomException | TechnicalException e) {
                configurationDTO.setValue(oldVal);
                t.getTableView().getItems().set(row, configurationDTO);
                Alerts.showError("Error", "An error occurred while updating configuration " + configurationDTO.getKey() + "\n\n" + e.getMessage(), true, GlobalHelper.getInstance().getConfigStage());
            }
        });

        key.setCellValueFactory(new PropertyValueFactory<>("Key"));
        val.setCellValueFactory(new PropertyValueFactory<>("Value"));
    }

    public static <T extends Event> void exitApplication(T t) {
        Start.reloadConfiguration();
    }
}
