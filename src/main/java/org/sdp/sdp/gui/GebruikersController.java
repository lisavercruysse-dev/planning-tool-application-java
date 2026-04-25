package org.sdp.sdp.gui;

import domein.WerknemerController;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GebruikersController extends VBox {
    private final MainController mainController;

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

    private final Map<String, TextField> columnFilterFields = new LinkedHashMap<>();

    private final ObservableWerknemersTable observableWerknemersTable;

    @FXML
    private TextField addFirstName;

    @FXML
    private TextField addLastName;

    @FXML
    private TextField txtRol;

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
        setupColumnFilter(lastNameCol,  "Achternaam");
        setupColumnFilter(firstNameCol, "Voornaam");
        setupColumnFilter(jobTitelCol,  "Jobtitel");
        setupColumnFilter(emailCol,     "Email");

        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        jobTitelCol.setCellValueFactory(cellData -> cellData.getValue().jobTitelProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        // SortedList in GUI
        SortedList<ObservableWerknemer> sortedList =
                new SortedList<>(observableWerknemersTable.getFilteredList());

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
        observableWerknemersTable.changeFilter(
                columnFilterFields.get("Achternaam").getText(),
                columnFilterFields.get("Voornaam").getText(),
                columnFilterFields.get("Jobtitel").getText(),
                columnFilterFields.get("Email").getText()
        );
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