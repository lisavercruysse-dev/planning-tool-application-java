package org.sdp.sdp.gui;

import domein.Site;
import domein.Team;
import domein.Werknemer;
import domein.TeamController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableTeamsTable {
    private final TeamController teamController;
    private final ObservableList<ObservableTeam> observableTeamList;

    public ObservableTeamsTable(TeamController teamController) {
        this.teamController = teamController;
        this.observableTeamList = FXCollections.observableArrayList();
    }

    public ObservableTeam addTeam(Werknemer verantwoordelijke, String naam, Site site) {
        Team t = teamController.addTeam(verantwoordelijke, naam, site);
        ObservableTeam ot = new ObservableTeam(t);
        observableTeamList.add(ot);
        return ot;
    }
}
