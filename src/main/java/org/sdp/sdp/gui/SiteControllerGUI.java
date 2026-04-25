package org.sdp.sdp.gui;

import domein.SiteController;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SiteControllerGUI extends VBox {
    private final MainController mainController;

    @FXML
    private TableView<ObservableSite> tblSites;

    @FXML
    private TableColumn<ObservableSite, String> nameCol;

    @FXML
    private TableColumn<ObservableSite, String> locatieCol;

    @FXML
    private TableColumn<ObservableSite, String> capaciteitCol;

    @FXML
    private TableColumn<ObservableSite, String> operationeleStatusCol;

    @FXML
    private TableColumn<ObservableSite, String> productieStatusCol;

    private final Map<String, TextField> columnFilterFields = new LinkedHashMap<>();
    private final ObservableSitesTable observableSitesTable;

    @FXML
    private TextField addName;
    @FXML
    private TextField addLocatie;

    public SiteControllerGUI(MainController mainController, SiteController controller){

        this.mainController = mainController;
        // wrapper maken
        this.observableSitesTable = new ObservableSitesTable(controller);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/SitesFrame.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        initializeTable();
    }

    private void initializeTable() {
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        locatieCol.setCellValueFactory(cellData -> cellData.getValue().locatieProperty());
        capaciteitCol.setCellValueFactory(cellData -> cellData.getValue().capaciteitProperty());
        operationeleStatusCol.setCellValueFactory(cellData -> cellData.getValue().operationeleStatusProperty());
        productieStatusCol.setCellValueFactory(cellData -> cellData.getValue().productieStatusProperty());

        setupColumnFilter(nameCol, "Naam");
        setupColumnFilter(locatieCol, "Locatie");
        setupColumnFilter(capaciteitCol, "Capaciteit");
        setupColumnFilter(operationeleStatusCol, "Operationele Status");
        setupColumnFilter(productieStatusCol, "Productie Status");

        // SortedList in GUI
        SortedList<ObservableSite> sortedList =
                new SortedList<>(observableSitesTable.getFilteredList());

        // Binding voor kolomsortering
        sortedList.comparatorProperty().bind(tblSites.comparatorProperty());

        tblSites.setItems(sortedList);

        // Default sortering instellen
        nameCol.setSortType(TableColumn.SortType.ASCENDING);
        tblSites.getSortOrder().add(nameCol);

        tblSites.getSelectionModel().selectedItemProperty().
                addListener((observableValue, oldValue, newValue) -> {
                    //Controleer of er een site is geselecteerd
                    if (newValue != null) {
                        int index = tblSites.
                                getSelectionModel().getSelectedIndex();
                        System.out.printf("%d %s%n", index,
                                newValue.getName());
                    }
                });
    }

    private void setupColumnFilter(TableColumn<?, ?> col, String label) {
        TextField tf = new TextField();
        tf.setPromptText("filter...");
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle("-fx-font-weight: normal; -fx-padding: 2 4 2 4;");

        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-weight: bold;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(3, tf, lbl);
        vbox.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);
        col.setGraphic(vbox);
        col.setText("");

        columnFilterFields.put(label, tf);
        tf.textProperty().addListener((obs, old, newVal) -> updateFilter());
    }

    private void updateFilter() {
        observableSitesTable.changeFilter(
                columnFilterFields.get("Naam").getText(),
                columnFilterFields.get("Locatie").getText(),
                columnFilterFields.get("Capaciteit").getText(),
                columnFilterFields.get("Operationele Status").getText(),
                columnFilterFields.get("Productie Status").getText()
        );
    }


}
