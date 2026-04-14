package org.sdp.sdp.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import domein.TeamController;
import java.io.IOException;

public class TeamControllerGUI extends VBox {
    private final MainController mainController;
    private final ObservableTeamsTable observableTeamsTable;

    public TeamControllerGUI(MainController mainController, TeamController controller) {
        this.mainController = mainController;
        this.observableTeamsTable = new ObservableTeamsTable(controller);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/teams.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toevoegenAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/teamToevoegen.fxml"));
            TeamToevoegenController controller = new TeamToevoegenController(mainController, observableTeamsTable);

            loader.setController(controller);

            Node popup = loader.load();
            mainController.showPopup(popup);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
