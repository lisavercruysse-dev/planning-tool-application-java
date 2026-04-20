package org.sdp.sdp.gui;

import domein.Team;
import domein.Werknemer;
import domein.WerknemerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

import java.util.List;

public class ObservableWerknemerFromTeamList {

    private WerknemerController controller;
    @Getter
    private final ObservableList<ObservableWerknemer> observableList;
    private int teamId;
    private final ObservableTeam observableTeam;

    public ObservableWerknemerFromTeamList(WerknemerController controller, int teamId, ObservableTeam observableTeam) {
        this.controller = controller;
        this.observableList = FXCollections.observableArrayList();
        this.teamId = teamId;
        this.observableTeam = observableTeam;

        List<Werknemer> werknemers = this.controller.getWerknemersFromTeam(teamId);
        if (werknemers != null) {
            werknemers.forEach(w -> observableList.add(new ObservableWerknemer(w)));
        }
    }

    public ObservableWerknemer addWerknemer(Werknemer werknemer) {
        Werknemer w = controller.voegWerknemerToeAanTeam(werknemer.getId(), teamId);
        ObservableWerknemer ow = new ObservableWerknemer(werknemer);
        observableList.add(ow);
        observableTeam.aantalWerknemersProperty().set(String.valueOf(observableList.size()));
        return ow;
    }

    public void verwijderWerknemerUitTeam(ObservableWerknemer observableWerknemer) {
        controller.verwijderWerknemerUitTeam(observableWerknemer.getWerknemer().getId(), teamId);
        observableList.remove(observableWerknemer);
        observableTeam.aantalWerknemersProperty().set(String.valueOf(observableList.size()));
    }
}

