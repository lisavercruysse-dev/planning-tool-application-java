package org.sdp.sdp.gui;

import domein.JobTitel;
import domein.Team;
import domein.WerknemerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GebruikerToevoegenController implements CanPopup {

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


    private MainController mainController;
    private final WerknemerController werknemerController = new WerknemerController();

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        for (JobTitel jt: JobTitel.values()) {
            jobTitelCombo.getItems().add(jt.name().toLowerCase());
        }
        jobTitelCombo.getSelectionModel().selectFirst();
    }

    public void btnCloseAction(ActionEvent actionEvent) {
        mainController.closePopup();
    }

    public void btnConfirmAction(ActionEvent actionEvent) {
        String voornaam = this.voornaam.getText();
        String achternaam = this.achternaam.getText();
        String wachtwoord = this.wachtwoord.getText();
        String jobTitel = this.jobTitelCombo.getSelectionModel().getSelectedItem();
        Team team = null;

        werknemerController.addWerknemer(voornaam, achternaam, jobTitel, wachtwoord, team);
    }
}
