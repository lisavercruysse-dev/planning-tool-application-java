package org.sdp.sdp.gui;

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

    public ObservableWerknemerFromTeamList(WerknemerController controller, int teamId) {
        this.controller = controller;
        this.observableList = FXCollections.observableArrayList();

        List<Werknemer> werknemers = this.controller.getWerknemersFromTeam(teamId);
        if (werknemers != null) {
            werknemers.forEach(w -> observableList.add(new ObservableWerknemer(w)));
        }
    }

}
