package org.sdp.sdp.gui;

import domein.*;
import dto.TeamDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

import java.util.List;

public class ObservableTeamsTable {
    private final TeamController teamController;
    private final WerknemerController werknemerController;
    private final SiteController siteController;
    private final ObservableList<ObservableTeam> observableTeamList;
    @Getter
    private final FilteredList<ObservableTeam> filteredTeams;

    public ObservableTeamsTable(TeamController teamController, WerknemerController werknemerController, SiteController siteController) {
        this.teamController = teamController;
        this.werknemerController = werknemerController;
        this.siteController = siteController;
        this.observableTeamList = FXCollections.observableArrayList();

        List<TeamDTO> teams = this.teamController.getAllTeams();
        if (teams != null) {
            teams.forEach(t -> observableTeamList.add(new ObservableTeam(t, werknemerController, siteController)));
        }
        this.filteredTeams = new FilteredList<>(observableTeamList, t -> true);
    }

    public void changeFilter(String filterValue) {
        filteredTeams.setPredicate(t -> {
            if(filterValue == null || filterValue.isBlank()) return true;
            String lower = filterValue.toLowerCase();
            return  t.naamProperty().get().toLowerCase().contains(lower) ||
                    t.siteProperty().get().toLowerCase().contains(lower) ||
                    t.verantwoordelijkeProperty().get().toLowerCase().contains(lower);
        });
    }

    public ObservableTeam addTeam(Werknemer verantwoordelijke, String naam, Site site) {
        TeamDTO t = teamController.addTeam(verantwoordelijke, naam, site);
        ObservableTeam ot = new ObservableTeam(t, werknemerController, siteController);
        observableTeamList.add(ot);
        return ot;
    }

    public void removeTeam(ObservableTeam observableTeam) {
        teamController.deleteTeam(observableTeam.getTeam());
        observableTeamList.remove(observableTeam);
    }
}