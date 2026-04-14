package org.sdp.sdp.gui;

import domein.JobTitel;
import domein.Team;
import domein.Werknemer;
import domein.WerknemerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.Set;

public class GebruikerToevoegenController {

    @FXML
    public TextField voornaam;

    @FXML
    public TextField achternaam;

    @FXML
    public PasswordField wachtwoord;

    @FXML
    public ComboBox<String> jobTitelCombo;

    @FXML
    public ComboBox<String> teamCombo;

    @FXML
    public Label voornaamError;

    @FXML
    public Label achternaamError;

    @FXML
    public Label wachtwoordError;


    private final MainController mainController;
    private final ObservableWerknemersTable werknemersTable;

    public GebruikerToevoegenController(MainController mainController, ObservableWerknemersTable werknemersTable) {
        this.mainController = mainController;
        this.werknemersTable = werknemersTable;
    }

    @FXML
    private void initialize() {

        voornaamError.setText("");
        achternaamError.setText("");
        wachtwoordError.setText("");

        for (JobTitel jobTitel: JobTitel.values()) {
            jobTitelCombo.getItems().add(jobTitel.name().toLowerCase());
        }
        jobTitelCombo.getSelectionModel().selectFirst();
    }

    public void btnCloseAction(ActionEvent actionEvent) {
        mainController.closePopup();
    }

    @FXML
    public void btnConfirmAction(ActionEvent actionEvent) {
        voornaamError.setText("");
        achternaamError.setText("");
        wachtwoordError.setText("");

        String voornaam = this.voornaam.getText();
        String achternaam = this.achternaam.getText();
        String wachtwoord = this.wachtwoord.getText();
        String jobtitel = this.jobTitelCombo.getSelectionModel().getSelectedItem().toUpperCase();
        Team team = null;

        Set<String> errors = Werknemer.validate(voornaam, achternaam, jobtitel, wachtwoord);

        Map<Label, String> errorMap = Map.of(
                voornaamError, "Voornaam",
                achternaamError, "Achternaam",
                wachtwoordError, "Wachtwoord"
        );

        errorMap.forEach((label, keyword) ->
                label.setText(errors.stream()
                        .filter(e -> e.contains(keyword))
                        .findFirst()
                        .orElse(""))
        );

        if (!errors.isEmpty()) return;

        werknemersTable.addWerknemer(voornaam, achternaam, jobtitel, wachtwoord, team);
        mainController.closePopup();
    }
}
