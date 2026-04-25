package org.sdp.sdp.gui;

import domein.JobTitel;
import domein.WerknemerController;
import dto.WerknemerInputDTO;
import exception.WerknemerInformationException;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;

public class GebruikersController extends HBox {
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
    private TextField voornaamInput;
    
    @FXML
    private Label voornaamError;

    @FXML
    private TextField achternaamInput;

    @FXML
    private Label achternaamError;

    @FXML
    private TextField telefoonInput;
    
    @FXML
    private Label telefoonError;

    @FXML
    private DatePicker geboorteInput;

    @FXML
    private Label geboorteError;

    @FXML
    private ComboBox<String> jobtitelInput;

    @FXML
    private Label jobtitelError;

    @FXML
    private TextField landInput;

    @FXML
    private Label landError;

    @FXML
    private TextField postcodeInput;

    @FXML
    private Label postcodeError;

    @FXML
    private TextField stadInput;

    @FXML
    private Label stadError;
    
    @FXML
    private TextField straatInput;

    @FXML
    private Label straatError;

    @FXML
    private TextField huisnrInput;

    @FXML
    private Label huisnrError;

    @FXML
    private TextField busInput;

    @FXML
    private Label busError;

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

        //jobtitelCombo
        for (JobTitel jobTitel : JobTitel.values()) {
            jobtitelInput.getItems().add(jobTitel.name().toLowerCase());
        }
        jobtitelInput.getSelectionModel().selectFirst();
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
        voornaamError.setText("");
        achternaamError.setText("");
        telefoonError.setText("");
        geboorteError.setText("");
        jobtitelError.setText("");
        landError.setText("");
        postcodeError.setText("");
        stadError.setText("");
        straatError.setText("");
        huisnrError.setText("");
        busError.setText("");

        String voornaam =  voornaamInput.getText();
        String achternaam =  achternaamInput.getText();
        String telefoon =  telefoonInput.getText();
        JobTitel jobTitel = JobTitel.valueOf(jobtitelInput.getSelectionModel().getSelectedItem().toUpperCase());
        LocalDate geboorteDatum =  geboorteInput.getValue();
        String land =  landInput.getText();
        String postcode =  postcodeInput.getText();
        String stad =  stadInput.getText();
        String straat =  straatInput.getText();
        Integer huisnr;
        Integer bus;
        try {
            huisnr =  Integer.parseInt(huisnrInput.getText());
        } catch (NumberFormatException e) {
            huisnr = null;
        }

        try {
            bus = Integer.parseInt(busInput.getText());
        } catch (NumberFormatException e) {
            bus = null;
        }

        try {
            observableWerknemersTable.addWerknemer(new WerknemerInputDTO(voornaam, achternaam, jobTitel, telefoon, geboorteDatum, land, postcode, stad, straat, huisnr, bus));
        } catch (WerknemerInformationException ex) {
            handleErrors(ex);
        }
    }

    private void handleErrors (WerknemerInformationException ex) {
        ex.getInformationRequired().forEach((field, message) -> {
            switch (field) {
                case VOORNAAM    -> voornaamError.setText(message);
                case ACHTERNAAM  -> achternaamError.setText(message);
                case TELEFOON    -> telefoonError.setText(message);
                case GEBOORTEDATUM -> geboorteError.setText(message);
                case JOBTITEL    -> jobtitelError.setText(message);
                case LAND        -> landError.setText(message);
                case POSTCODE    -> postcodeError.setText(message);
                case STAD        -> stadError.setText(message);
                case STRAAT      -> straatError.setText(message);
                case HUISNUMMER      -> huisnrError.setText(message);
                case BUS         -> busError.setText(message);
            }
        });
    }

}