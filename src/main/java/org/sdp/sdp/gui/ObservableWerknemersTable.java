package org.sdp.sdp.gui;

import domein.WerknemerController;
import dto.WerknemerInputDTO;
import dto.WerknemerDTO;
import exception.WerknemerInformationException;
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

        List<WerknemerDTO> werknemers = this.controller.getWerknemers();
        if (werknemers != null) {
            werknemers.forEach(p -> werknemerObservableList.add(new ObservableWerknemer(p)));
        }
        this.filteredPersonList = new FilteredList<>(werknemerObservableList, p -> true);
    }

    public void changeFilter(String filterValue, String kolom) {
        filteredPersonList.setPredicate(p -> {
            if (filterValue == null || filterValue.isBlank()) return true;
            String lower = filterValue.toLowerCase();
            return switch (kolom) {
                case "Achternaam" -> p.lastNameProperty().get().toLowerCase().contains(lower);
                case "Voornaam" -> p.firstNameProperty().get().toLowerCase().contains(lower);
                case "Jobtitel" -> p.jobTitelProperty().get().toLowerCase().contains(lower);
                case "Email" -> p.emailProperty().get().toLowerCase().contains(lower);
                default -> p.firstNameProperty().get().toLowerCase().contains(lower) ||
                        p.lastNameProperty().get().toLowerCase().contains(lower) ||
                        p.jobTitelProperty().get().toLowerCase().contains(lower) ||
                        p.emailProperty().get().toLowerCase().contains(lower);
            };
        });
    }

    public ObservableWerknemer addWerknemer(WerknemerInputDTO dto) throws WerknemerInformationException {
        WerknemerDTO w = controller.addWerknemer(dto);
        ObservableWerknemer ow = new ObservableWerknemer(w);
        werknemerObservableList.add(ow);
        return ow;
    }
}
