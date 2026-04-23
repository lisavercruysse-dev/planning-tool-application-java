package org.sdp.sdp.gui;

import domein.JobTitel;
import domein.Team;
import domein.TeamController;
import dto.TeamDTO;
import dto.WerknemerInputDTO;
import exception.WerknemerInformationException;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.time.LocalDate;

public class GebruikerToevoegenController {

    @FXML
    public TextField voornaam;

    @FXML
    public TextField achternaam;

    @FXML
    public TextField telefoon;

    @FXML
    public DatePicker geboortedatum;

    @FXML
    public ComboBox<String> jobTitelCombo;

    @FXML
    public ComboBox<TeamDTO> teamCombo;

    @FXML
    public TextField land;

    @FXML
    public TextField postcode;

    @FXML
    public TextField stad;

    @FXML
    public TextField straat;

    @FXML
    public TextField huisnummer;

    @FXML
    public TextField bus;

    private final MainController mainController;
    private final ObservableWerknemersTable werknemersTable;
    private final TeamController teamController;

    public GebruikerToevoegenController(MainController mainController, ObservableWerknemersTable werknemersTable) {
        this.mainController = mainController;
        this.werknemersTable = werknemersTable;
        this.teamController = new TeamController();
    }

    @FXML
    private void initialize() {

        for (JobTitel jobTitel: JobTitel.values()) {
            jobTitelCombo.getItems().add(jobTitel.name().toLowerCase());
        }
        jobTitelCombo.getSelectionModel().selectFirst();

        teamCombo.getItems().addAll(teamController.getAllTeams());
        teamCombo.setConverter(new StringConverter<TeamDTO>() {

            @Override
            public String toString(TeamDTO team) {
                return team == null ? "" : team.naam();
            }

            @Override
            public TeamDTO fromString(String s) {
                return null;
            }
        });
    }

    public void btnCloseAction(ActionEvent actionEvent) {
        mainController.closePopup();
    }

    @FXML
    public void btnConfirmAction(ActionEvent actionEvent) {

        String voornaam = this.voornaam.getText();
        String achternaam = this.achternaam.getText();
        JobTitel jobtitel = JobTitel.valueOf(this.jobTitelCombo.getSelectionModel().getSelectedItem().toUpperCase());
        String telefoon = this.telefoon.getText();
        LocalDate geboortedatum = this.geboortedatum.getValue();
        String land = this.land.getText();
        String postcode = this.postcode.getText();
        String stad = this.stad.getText();
        String straat = this.straat.getText();
        Integer huisnummer = Integer.parseInt(this.huisnummer.getText());
        Integer bus = Integer.parseInt(this.bus.getText());
        Team team = null;

        try {
            werknemersTable.addWerknemer(new WerknemerInputDTO(voornaam, achternaam, jobtitel, telefoon, geboortedatum, land, postcode, stad, straat, huisnummer, bus, team));

        } catch (WerknemerInformationException ex) {
            printExceptions(ex);
        }

        mainController.closePopup();
    }

    private void printExceptions(WerknemerInformationException ex) {
        System.out.println("WerknemerInformationException: " + ex.getMessage());
        ex.getInformationRequired().forEach((e, m) -> System.out.println(m));
        System.out.println();
    }
}
