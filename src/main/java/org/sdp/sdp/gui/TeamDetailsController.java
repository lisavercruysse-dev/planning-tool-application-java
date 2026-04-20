package org.sdp.sdp.gui;

import domein.Team;
import domein.WerknemerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;

public class TeamDetailsController extends VBox {
    private final MainController mainController;
    private final ObservableWerknemerFromTeamList werknemersList;
    private final WerknemerController werknemerController;
    @Getter
    private final Team team;

    @FXML
    private ListView<ObservableWerknemer> listWerknemers;

    @FXML
    private Label teamName;

    @FXML
    private Label verantwoordelijke;

    @FXML
    private Label site;

    public TeamDetailsController(MainController mainController, ObservableTeam team){
        this.mainController = mainController;
        this.team = team.getTeam();
        this.werknemerController = new WerknemerController();
        this.werknemersList = new ObservableWerknemerFromTeamList(werknemerController, team.getTeam().getId(), team);
    }

    @FXML
    private void initialize() {
        listWerknemers.setItems(werknemersList.getObservableList());

        listWerknemers.setCellFactory(lv -> new ListCell<>(){
            @Override
            protected void updateItem(ObservableWerknemer item, boolean empty){
                super.updateItem(item, empty);

                if (empty || item == null){
                    setText(null);
                } else {
                    setText(item.firstNameProperty().get() + " " + item.lastNameProperty().get());
                }
            }
        });

        teamName.setText(team.getNaam());
        verantwoordelijke.setText(team.getVerantwoordelijke().getVoornaam() + " " + team.getVerantwoordelijke().getAchternaam());
        site.setText(team.getSite().getName());

    }

    public void btnCloseAction(ActionEvent event) {
        mainController.closePopup();
    }

    public void btnWerknemerToevoegenAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/WerknemerToevoegen.fxml"));
            WerknemerAanTeamToevoegenController controller = new WerknemerAanTeamToevoegenController(mainController, this, werknemersList);

            loader.setController(controller);

            Node popup = loader.load();
            mainController.closePopup();
            mainController.showPopup(popup);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void onVerwijderAction(ActionEvent event) {
        ObservableWerknemer selected = listWerknemers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Weet je zeker dat je deze werknemer uit het team wil verwijderen?", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Werknemer verwijderen");
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                werknemersList.verwijderWerknemerUitTeam(selected);
            }
        }
    }
}
