package org.sdp.sdp.gui;

import domein.*;
import dto.SiteDTO;
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
    public ComboBox<Werknemer> verantwoordelijke;

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
        verantwoordelijke.setConverter(new StringConverter<Werknemer>() {
            @Override
            public String toString(Werknemer w) {
                return w == null ? "" : w.getVoornaam() + " " + w.getAchternaam();
            }

            @Override
            public Werknemer fromString(String s) {return null; }
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
        Werknemer verantwoordelijke = this.verantwoordelijke.getSelectionModel().getSelectedItem();
        SiteDTO siteDTO = this.site.getSelectionModel().getSelectedItem();
        Site site = siteDTO != null ? siteController.getSiteById(siteDTO.id()) : null;

        Set<String> errors = Team.validate( verantwoordelijke, naam, site);

        Map<Label, String> errorMap = Map.of(
                naamError, "naam",
                verantwoordelijkeError, "verantwoordelijke",
                siteError, "site"
        );

        errorMap.forEach((label, keyword) -> {
            label.setText(errors.stream().filter(e -> e.toLowerCase().contains(keyword.toLowerCase())).findFirst().orElse(""));
        });

        if(!errors.isEmpty()) return;

        try {
            teamsTable.addTeam(verantwoordelijke, naam, site);
            mainController.closePopup();
        } catch(IllegalArgumentException e) {
            naamError.setText(e.getMessage());
        }

    }
}
