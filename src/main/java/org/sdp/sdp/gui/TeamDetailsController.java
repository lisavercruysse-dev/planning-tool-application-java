package org.sdp.sdp.gui;

import domein.Team;
import domein.WerknemerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;

public class TeamDetailsController extends VBox {
    private final MainController mainController;
    private final ObservableWerknemerFromTeamList werknemersList;
    private final WerknemerController werknemerController;
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
        this.werknemersList = new ObservableWerknemerFromTeamList(werknemerController, team.getTeam().getId());
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
}
