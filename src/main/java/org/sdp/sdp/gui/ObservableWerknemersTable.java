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
import java.util.Objects;

public class ObservableWerknemersTable {

    private final WerknemerController controller;
    private final ObservableList<ObservableWerknemer> werknemerObservableList;
    @Getter
    private final FilteredList<ObservableWerknemer> filteredList;

    public ObservableWerknemersTable(WerknemerController controller) {
        this.controller = controller;
        this.werknemerObservableList = FXCollections.observableArrayList();

        List<WerknemerDTO> werknemers = this.controller.getWerknemers();
        if (werknemers != null) {
            werknemers.forEach(p -> werknemerObservableList.add(new ObservableWerknemer(p)));
        }
        this.filteredList = new FilteredList<>(werknemerObservableList, p -> true);
    }

    public void changeFilter(String achternaam, String voornaam, String jobtitel, String email) {
        filteredList.setPredicate(p ->
            matchesFilter(p.lastNameProperty().get(), achternaam) &&
            matchesFilter(p.firstNameProperty().get(), voornaam) &&
            matchesFilter(p.jobTitelProperty().get(), jobtitel) &&
            matchesFilter(p.emailProperty().get(), email)
        );
    }

    private boolean matchesFilter(String value, String filter) {
        if (filter == null || filter.isBlank()) return true;
        return Objects.toString(value, "").toLowerCase().contains(filter.toLowerCase());
    }

    public ObservableWerknemer addWerknemer(WerknemerInputDTO dto) throws WerknemerInformationException {
        WerknemerDTO w = controller.addWerknemer(dto);
        ObservableWerknemer ow = new ObservableWerknemer(w);
        werknemerObservableList.add(ow);
        return ow;
    }
}
