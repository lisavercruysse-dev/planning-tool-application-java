package org.sdp.sdp.gui;

import domein.Werknemer;
import domein.WerknemerManager;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.Setter;

public class GebruikersController {

    @FXML
    private TableView<Werknemer> tblGebruikers;

    @FXML
    private TextField txtNaam;

    @FXML
    private TextField txtRol;

    @Setter
    private WerknemerManager manager;

    @FXML
    private void btnWijzigAction() {
        var geselecteerd = tblGebruikers.getSelectionModel().getSelectedItem();
        if (geselecteerd != null) {
            manager.wijzigWerknemer(geselecteerd, txtNaam.getText(), txtRol.getText());
        }
    }

    @FXML
    private void btnVerwijderAction() {
        var geselecteerd = tblGebruikers.getSelectionModel().getSelectedItem();
        if (geselecteerd != null) {
            manager.verwijderWerknemer(geselecteerd);
            txtNaam.clear();
            txtRol.clear();
        }
    }
}