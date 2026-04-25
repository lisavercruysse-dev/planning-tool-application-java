package org.sdp.sdp.gui;

import domein.*;
import dto.SiteDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

public class ObservableSitesTable {

    private final SiteController controller;
    private final ObservableList<ObservableSite> sitesObservableList;
    @Getter
    private final FilteredList<ObservableSite> filteredList;

    public ObservableSitesTable(SiteController controller) {
        this.controller = controller;
        this.sitesObservableList = FXCollections.observableArrayList();

        List<SiteDTO> sites = this.controller.getAllSites();
        if (sites != null) {
            sites.forEach(site -> sitesObservableList.add(new ObservableSite(site)));
        }
        this.filteredList = new FilteredList<>(sitesObservableList, site -> true);
    }

    public void changeFilter(String naam, String locatie, String capaciteit, String operationeleStatus, String productieStatus) {
        filteredList.setPredicate(p ->
            matchesFilter(p.nameProperty().get(), naam) &&
            matchesFilter(p.locatieProperty().get(), locatie) &&
            matchesFilter(p.capaciteitProperty().get(), capaciteit) &&
            matchesFilter(p.operationeleStatusProperty().get(), operationeleStatus) &&
            matchesFilter(p.productieStatusProperty().get(), productieStatus)
        );
    }

    private boolean matchesFilter(String value, String filter) {
        if (filter == null || filter.isBlank()) return true;
        return Objects.toString(value, "").toLowerCase().contains(filter.toLowerCase());
    }

    /*
    public ObservableSite addSite(String name, String locatie, int capaciteit, String operationeleStatus, String productieStatus) {
        Site s = controller.addSite(name, locatie, capaciteit, operationeleStatus, productieStatus);
        ObservableSite os = new ObservableSite(s);
        sitesObservableList.add(os);
        return os;
    } */
}
