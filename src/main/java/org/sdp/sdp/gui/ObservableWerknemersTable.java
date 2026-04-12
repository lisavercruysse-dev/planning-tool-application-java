package org.sdp.sdp.gui;

import domein.Werknemer;
import domein.WerknemerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

import java.util.List;

public class ObservableWerknemersTable {

    private final WerknemerController controller;
    private final ObservableList<ObservableWerknemer> werknemerObservableList;
    @Getter
    private final FilteredList<ObservableWerknemer> filteredPersonList;

    public ObservableWerknemersTable(WerknemerController controller) {
        this.controller = controller;
        this.werknemerObservableList = FXCollections.observableArrayList();

        List<Werknemer> werknemers = this.controller.getWerknemers();
        if (werknemers != null) {
            werknemers.forEach(p -> werknemerObservableList.add(new ObservableWerknemer(p)));
        }
        this.filteredPersonList = new FilteredList<>(werknemerObservableList, p -> true);
    }

    public void changeFilter(String filterValue) {
        filteredPersonList.setPredicate(p -> {
            if (filterValue == null || filterValue.isBlank()) return true;
            String lower = filterValue.toLowerCase();
            return p.firstNameProperty().get().toLowerCase().contains(lower) ||
                    p.lastNameProperty().get().toLowerCase().contains(lower);
        });
    }
}
