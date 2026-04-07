package org.sdp.sdp.gui;

import domein.Werknemer;
import domein.WerknemerController;
import domein.WerknemerManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.Setter;

import javax.imageio.IIOException;
import java.io.IOException;

public class GebruikersController implements CanPopup {
    private MainController mainController;

    @FXML
    private TableView<Werknemer> tblGebruikers;

    @FXML
    private TextField txtNaam;

    @FXML
    private TextField txtRol;

    @Setter
    private WerknemerController controller;

    @FXML
    private void btnWijzigAction() {
        var geselecteerd = tblGebruikers.getSelectionModel().getSelectedItem();
        if (geselecteerd != null) {
            controller.wijzigWerknemer(geselecteerd, txtNaam.getText(), txtRol.getText());
        }
    }

    @FXML
    private void btnVerwijderAction() {
        var geselecteerd = tblGebruikers.getSelectionModel().getSelectedItem();
        if (geselecteerd != null) {
            controller.verwijderWerknemer(geselecteerd);
            txtNaam.clear();
            txtRol.clear();
        }
    }

    public void btnToevoegenAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/GebruikerToevoegen.fxml"));
            Node popup = loader.load();

            GebruikerToevoegenController controller = loader.getController();
            controller.setMainController(mainController);

            mainController.showPopup(popup);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}