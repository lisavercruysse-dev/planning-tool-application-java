package org.sdp.sdp.gui;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import domein.TeamController;
import java.io.IOException;

public class TeamControllerGUI extends VBox {
    private final MainController mainController;
    private final ObservableTeamsTable observableTeamsTable;

    @FXML
    private TableColumn<ObservableTeam, String> naamCol;

    @FXML
    private TableColumn<ObservableTeam, String> verantwoordelijkeCol;

    @FXML
    private TableColumn<ObservableTeam, String> aantalLedenCol;

    @FXML
    private TableColumn<ObservableTeam, String> siteCol;

    @FXML
    private TableView<ObservableTeam> teamsTbl;

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

        initializeTable();
    }

    private void initializeTable() {
        Label placeholder = new Label("Geen teams gevonden");
        placeholder.setStyle("-fx-text-fill: #999999; -fx-font-size: 13;");
        teamsTbl.setPlaceholder(placeholder);

        naamCol.setCellValueFactory(cellData -> cellData.getValue().naamProperty());
        verantwoordelijkeCol.setCellValueFactory(cellData -> cellData.getValue().verantwoordelijkeProperty());
        siteCol.setCellValueFactory(cellData -> cellData.getValue().siteProperty());
        aantalLedenCol.setCellValueFactory(cellData -> cellData.getValue().aantalWerknemersProperty());

        SortedList<ObservableTeam> sortedList = new SortedList<>(observableTeamsTable.getFilteredTeams());
        sortedList.comparatorProperty().bind(teamsTbl.comparatorProperty());
        teamsTbl.setItems(sortedList);

        naamCol.setSortType(TableColumn.SortType.ASCENDING);
        teamsTbl.getSortOrder().add(naamCol);

        teamsTbl.setFixedCellSize(45);
        teamsTbl.prefHeightProperty().bind(
                teamsTbl.fixedCellSizeProperty()
                        .multiply(Bindings.size(teamsTbl.getItems()).add(1.15))
        );
        teamsTbl.minHeightProperty().bind(teamsTbl.prefHeightProperty());
        teamsTbl.maxHeightProperty().bind(teamsTbl.prefHeightProperty());

        teamsTbl.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        int index = teamsTbl.getSelectionModel().getSelectedIndex();
                        System.out.printf("%d %s", index, newValue.naamProperty());
                    }
                });
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
