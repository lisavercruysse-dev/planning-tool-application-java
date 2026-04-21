package org.sdp.sdp.gui;

import domein.WerknemerController;
import domein.WerknemerManager;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.Setter;

import javax.imageio.IIOException;
import java.io.IOException;

public class GebruikersController extends VBox {
    private final MainController mainController;

    @FXML
    private ComboBox<String> cmbFilterKolom;

    @FXML
    private TextField txtFilter;

    @FXML
    private TableView<ObservableWerknemer> tblWerknemers;

    @FXML
    private TableColumn<ObservableWerknemer, String> firstNameCol;

    @FXML
    private TableColumn<ObservableWerknemer, String> lastNameCol;

    @FXML
    private TableColumn<ObservableWerknemer, String> jobTitelCol;

    @FXML
    private TableColumn<ObservableWerknemer, String> emailCol;

    private final ObservableWerknemersTable observableWerknemersTable;

    @FXML
    private TextField addFirstName;

    @FXML
    private TextField addLastName;

    @FXML
    private TextField txtRol;

    @Setter
    private WerknemerController controller;

    public GebruikersController(MainController mainController, WerknemerController controller){

        this.mainController = mainController;
        // wrapper maken
        this.observableWerknemersTable = new ObservableWerknemersTable(controller);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/WerknemersFrame.fxml"));
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

        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        jobTitelCol.setCellValueFactory(cellData -> cellData.getValue().jobTitelProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        cmbFilterKolom.setItems(FXCollections.observableArrayList(
                "Alle kolommen", "Achternaam", "Voornaam", "Jobtitel", "Email"
        ));
        cmbFilterKolom.setValue("Alle kolommen");

        // Herfilter wanneer de keuze verandert
        cmbFilterKolom.setOnAction(e -> filter(null));

        // SortedList in GUI
        SortedList<ObservableWerknemer> sortedList =
                new SortedList<>(observableWerknemersTable.getFilteredPersonList());

        // Binding voor kolomsortering
        sortedList.comparatorProperty().bind(tblWerknemers.comparatorProperty());

        tblWerknemers.setItems(sortedList);

        // Default sortering instellen
        firstNameCol.setSortType(TableColumn.SortType.ASCENDING);
        tblWerknemers.getSortOrder().add(lastNameCol);

        tblWerknemers.getSelectionModel().selectedItemProperty().
                addListener((observableValue, oldPerson, newPerson) -> {
                    //Controleer of er een persoon is geselecteerd
                    if (newPerson != null) {
                        int index = tblWerknemers.
                                getSelectionModel().getSelectedIndex();
                        System.out.printf("%d %s %s%n", index,
                                newPerson.getFirstName(),
                                newPerson.getLastName());
                    }
                });
    }

    @FXML
    private void filter(KeyEvent event) {
        String filterValue = txtFilter.getText();
        String kolom = cmbFilterKolom.getValue();
        observableWerknemersTable.changeFilter(filterValue, kolom);
    }

/*
    @FXML
    private void btnWijzigAction() {
        var geselecteerd = tblWerknemers.getSelectionModel().getSelectedItem();
        if (geselecteerd != null) {
            controller.wijzigWerknemer(geselecteerd, txtNaam.getText(), txtRol.getText());
        }
    }

    @FXML
    private void btnVerwijderAction() {
        var geselecteerd = tblWerknemers.getSelectionModel().getSelectedItem();
        if (geselecteerd != null) {
            controller.verwijderWerknemer(geselecteerd);
            txtNaam.clear();
            txtRol.clear();
        }
    } */

    public void btnToevoegenAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/GebruikerToevoegen.fxml"));
            GebruikerToevoegenController controller = new GebruikerToevoegenController(mainController, observableWerknemersTable);

            loader.setController(controller);

            Node popup = loader.load();
            mainController.showPopup(popup);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}