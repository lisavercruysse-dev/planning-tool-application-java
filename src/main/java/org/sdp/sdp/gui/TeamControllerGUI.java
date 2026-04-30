package org.sdp.sdp.gui;

import domein.SiteController;
import domein.Werknemer;
import domein.WerknemerController;
import dto.SiteDTO;
import dto.TeamDTO;
import dto.TeamInputDTO;
import dto.WerknemerDTO;
import exception.TeamInformationException;
import exception.WerknemerInformationException;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import domein.TeamController;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamControllerGUI extends VBox {
    private final MainController mainController;
    private final ObservableTeamsTable observableTeamsTable;
    private final SiteController siteController;
    private final WerknemerController werknemerController;

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

    @FXML
    private TextField naamInput;

    @FXML
    private Label  naamError, verantwoordelijkeError, siteError;

    @FXML
    private ComboBox<SiteDTO> siteInput;

    @FXML
    private ComboBox<WerknemerDTO> verantwoordelijkeInput;

    public TeamControllerGUI(MainController mainController, TeamController teamController, WerknemerController werknemerController, SiteController siteController) {
        this.mainController = mainController;
        this.werknemerController = werknemerController;
        this.siteController = siteController;
        this.observableTeamsTable = new ObservableTeamsTable(teamController, werknemerController, siteController);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/teams.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initialize();
    }

    private void initialize() {
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

        List<SiteDTO> sites = siteController.getAllSites();

        siteInput.getItems().addAll(sites);
        siteInput.setConverter(new StringConverter<SiteDTO>() {
            @Override
            public String toString(SiteDTO s) {
                return s == null ? "" : s.name();
            }

            @Override
            public SiteDTO fromString(String s) {
                return null;
            }
        });

        siteInput.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            verantwoordelijkeInput.getItems().clear();
            if (newValue != null) {
                List<WerknemerDTO> filteredVerantwoordelijken = new ArrayList<>();
                filteredVerantwoordelijken.addAll(werknemerController.getVerantwoordelijkenVanSite(newValue.id()));
                filteredVerantwoordelijken.addAll(werknemerController.getVerantwoordelijkenZonderSite());
                verantwoordelijkeInput.getItems().addAll(filteredVerantwoordelijken);
                }
            verantwoordelijkeInput.getSelectionModel().selectFirst();
        });
        verantwoordelijkeInput.setConverter(new StringConverter<WerknemerDTO>() {
            @Override
            public String toString(WerknemerDTO w) {
                return w == null ? "" : w.voornaam() + " " + w.achternaam();
            }

            @Override
            public WerknemerDTO fromString(String s) {
                return null;
            }
        });
        siteInput.getSelectionModel().selectFirst();
    }

    public void toevoegenAction(ActionEvent actionEvent) {
        naamError.setText("");

        String naam =  naamInput.getText();
        SiteDTO site = siteInput.getSelectionModel().getSelectedItem();
        WerknemerDTO verantwoordelijke = verantwoordelijkeInput.getSelectionModel().getSelectedItem();

        try {
            observableTeamsTable.addTeam(new TeamInputDTO(naam, site, verantwoordelijke));
        } catch (IllegalArgumentException e) {
            naamError.setText(e.getMessage());
        } catch (TeamInformationException ex) {
            handleErrors(ex);
        }
    }

    private void handleErrors (TeamInformationException ex) {
        ex.getErrors().forEach((el, message) -> {
            switch (el) {
                case NAAM -> naamError.setText(message);
                case VERANTWOORDELIJKE -> verantwoordelijkeError.setText(message);
                case SITE -> siteError.setText(message);
            }
        });
    }

    public void onDetailsAction(ActionEvent actionEvent) {
        ObservableTeam selected = teamsTbl.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org.sdp.sdp/gui/Details.fxml"));
                TeamDetailsController controller = new TeamDetailsController(mainController, selected);

                loader.setController(controller);

                Node popup = loader.load();
                mainController.showPopup(popup);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onVerwijderenAction(ActionEvent actionEvent) {
        ObservableTeam selected = teamsTbl.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Weet je zeker dat je dit team wilt verwijderen?", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Team verwijderen");
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                try {
                    observableTeamsTable.removeTeam(selected);
                } catch (IllegalArgumentException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fout bij verwijderen");
                    alert.setHeaderText("Kan team niet verwijderen");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Geen team geselecteerd");
            alert.setHeaderText(null);
            alert.setContentText("Selecteer eerst een team in de tabel om te verwijderen.");
            alert.showAndWait();
        }
    }
}