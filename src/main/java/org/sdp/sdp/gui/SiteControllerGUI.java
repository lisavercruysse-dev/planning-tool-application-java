package org.sdp.sdp.gui;

import domein.SiteController;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;

public class SiteControllerGUI extends VBox {
    private final MainController mainController;

    @FXML
    private ComboBox<String> cmbFilterKolom;

    @FXML
    private TextField txtFilter;

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

    private final ObservableSitesTable observableSitesTable;

    @FXML
    private TextField addName;
    @FXML
    private TextField addLocatie;

    @Setter
    private SiteController controller;

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

        cmbFilterKolom.setItems(FXCollections.observableArrayList(
                "Alle kolommen", "Naam", "Locatie", "Operationele Status", "Productie Status"
        ));
        cmbFilterKolom.setValue("Alle kolommen");

        // Herfilter wanneer de keuze verandert
        cmbFilterKolom.setOnAction(e -> filter(null));

        // SortedList in GUI
        SortedList<ObservableSite> sortedList =
                new SortedList<>(observableSitesTable.getFilteredSiteList());

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

    @FXML
    private void filter(KeyEvent event) {
        String filterValue = txtFilter.getText();
        String kolom = cmbFilterKolom.getValue();
        observableSitesTable.changeFilter(filterValue, kolom);
    }


}
