package org.sdp.sdp.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.sdp.sdp.domein.Gebruiker;
import org.sdp.sdp.domein.GebruikerBeheer;

public class GebruikersController {

    @FXML
    private TableView<Gebruiker> tblGebruikers;

    @FXML
    private TextField txtNaam;

    @FXML
    private TextField txtRol;

    @Setter
    private GebruikerBeheer beheer;

    @FXML
    private void btnWijzigAction() {
        var geselecteerd = tblGebruikers.getSelectionModel().getSelectedItem();
        if (geselecteerd != null) {
            beheer.wijzigGebruiker(geselecteerd, txtNaam.getText(), txtRol.getText());
        }
    }

    @FXML
    private void btnVerwijderAction() {
        var geselecteerd = tblGebruikers.getSelectionModel().getSelectedItem();
        if (geselecteerd != null) {
            beheer.verwijderGebruiker(geselecteerd);
            txtNaam.clear();
            txtRol.clear();
        }
    }
}