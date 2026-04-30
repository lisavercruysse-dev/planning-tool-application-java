package org.sdp.sdp.gui;

import domein.SiteController;
import domein.WerknemerController;
import dto.SiteDTO;
import dto.TeamInputDTO;
import dto.WerknemerDTO;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.*;

public class TeamToevoegenController extends VBox {
    private final MainController mainController;
    private final WerknemerController werknemerController = new WerknemerController();
    private final SiteController siteController = new SiteController();
    private final ObservableTeamsTable teamsTable;

    @FXML
    public TextField naam;

    @FXML
    public ComboBox<WerknemerDTO> verantwoordelijke;

    @FXML ComboBox<SiteDTO> site;

    @FXML
    public Label naamError;

    @FXML
    public Label verantwoordelijkeError;

    @FXML
    public Label siteError;

    public TeamToevoegenController(MainController mainController, ObservableTeamsTable observableTeamsTable) {
        this.mainController = mainController;
        this.teamsTable = observableTeamsTable;
    }

    @FXML
    private void initialize() {
        naamError.setText("");
        verantwoordelijkeError.setText("");
        siteError.setText("");

        verantwoordelijke.getItems().addAll(werknemerController.getVerantwoordelijken());
        verantwoordelijke.setConverter(new StringConverter<WerknemerDTO>() {
            @Override
            public String toString(WerknemerDTO w) {
                return w == null ? "" : w.voornaam() + " " + w.achternaam();
            }

            @Override
            public WerknemerDTO fromString(String s) {return null; }
        });

        site.getItems().addAll(siteController.getAllSites());
        site.setConverter(new StringConverter<SiteDTO>() {
            @Override
            public String toString(SiteDTO site) {
                return site == null ? "" : site.name(); // ← null-check hier;
            }

            @Override
            public SiteDTO fromString(String s) {
                return null;
            }
        });

        verantwoordelijke.getSelectionModel().selectFirst();
        site.getSelectionModel().selectFirst();
    }

    public void btnCloseAction(ActionEvent actionEvent) {
        mainController.closePopup();
    }

    public void btnConfirmAction(ActionEvent actionEvent) {
        naamError.setText("");
        verantwoordelijkeError.setText("");
        siteError.setText("");

        String naam = this.naam.getText();
        WerknemerDTO verantwoordelijke = this.verantwoordelijke.getSelectionModel().getSelectedItem();
        SiteDTO site = this.site.getSelectionModel().getSelectedItem();


        try {
            teamsTable.addTeam(new TeamInputDTO(naam, site, verantwoordelijke));
            mainController.closePopup();
        } catch(IllegalArgumentException e) {
            naamError.setText(e.getMessage());
        }

    }
}
