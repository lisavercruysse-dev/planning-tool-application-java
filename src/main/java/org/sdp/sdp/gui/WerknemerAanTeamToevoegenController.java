package org.sdp.sdp.gui;

import domein.WerknemerController;
import dto.TeamDTO;
import dto.WerknemerDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.util.List;

public class WerknemerAanTeamToevoegenController {
    private final MainController mainController;
    private final WerknemerController werknemerController;
    private final TeamDetailsController detailsController;
    private final ObservableWerknemerFromTeamList werknemersList;
    private final TeamDTO team;

    @FXML
    private ComboBox<WerknemerDTO> werknemers;

    @FXML
    private Label werknemerError;

    public WerknemerAanTeamToevoegenController(MainController mainController, TeamDetailsController detailsController,  ObservableWerknemerFromTeamList list) {
        this.mainController = mainController;
        this.werknemerController = new WerknemerController();
        this.detailsController = detailsController;
        this.werknemersList = list;
        this.team = detailsController.getTeam();
    }

    @FXML
    private void initialize() {
        werknemerError.setText("");

        List<WerknemerDTO> werknemersAlInTeam = werknemerController.getWerknemersFromTeam(team.id());

        werknemers.getItems().addAll(werknemerController.getGewoneWerknemers()
                .stream().filter(w -> !werknemersAlInTeam.contains(w)).toList());
        werknemers.setConverter(new StringConverter<WerknemerDTO>() {
            @Override
            public String toString(WerknemerDTO werknemer) {
                return werknemer == null ? "" : werknemer.voornaam() + " " + werknemer.achternaam();
            }

            @Override
            public WerknemerDTO fromString(String s) {
                return null;
            }
        });
        werknemers.getSelectionModel().selectFirst();
    }

    public void btnCloseAction(ActionEvent actionEvent) {
        loadPrevPopup();
    }

    public void btnConfirmAction(ActionEvent actionEvent) {
        werknemersList.addWerknemer(werknemers.getValue());
        loadPrevPopup();
    }

    public void loadPrevPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/Details.fxml"));
            TeamDetailsController controller = detailsController;

            loader.setController(controller);

            Node popup = loader.load();
            mainController.closePopup();
            mainController.showPopup(popup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
