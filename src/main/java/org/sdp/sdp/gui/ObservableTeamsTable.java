package org.sdp.sdp.gui;

import domein.Site;
import domein.Team;
import domein.Werknemer;
import domein.TeamController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

import java.util.List;

public class ObservableTeamsTable {
    private final TeamController teamController;
    private final ObservableList<ObservableTeam> observableTeamList;
    @Getter
    private final FilteredList<ObservableTeam> filteredTeams;

    public ObservableTeamsTable(TeamController teamController) {
        this.teamController = teamController;
        this.observableTeamList = FXCollections.observableArrayList();

        List<Team> teams = this.teamController.getAllTeams();
        if (teams != null) {
            teams.forEach(t -> observableTeamList.add(new ObservableTeam(t)));
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

    public void editAantalWerknemers(ObservableTeam team) {
        team.aantalWerknemersProperty().set(String.valueOf(team.getTeam().getWerknemers().size()));
    }

    public ObservableTeam addTeam(Werknemer verantwoordelijke, String naam, Site site) {
        Team t = teamController.addTeam(verantwoordelijke, naam, site);
        ObservableTeam ot = new ObservableTeam(t);
        observableTeamList.add(ot);
        return ot;
    }

}
