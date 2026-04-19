package org.sdp.sdp.gui;

import domein.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import lombok.Getter;

import java.util.List;

public class ObservableSitesTable {

    private final SiteController controller;
    private final ObservableList<ObservableSite> sitesObservableList;
    @Getter
    private final FilteredList<ObservableSite> filteredSiteList;

    public ObservableSitesTable(SiteController controller) {
        this.controller = controller;
        this.sitesObservableList = FXCollections.observableArrayList();

        List<Site> sites = this.controller.getAllSites();
        if (sites != null) {
            sites.forEach(p -> sitesObservableList.add(new ObservableSite(p)));
        }
        this.filteredSiteList = new FilteredList<>(sitesObservableList, p -> true);
    }

    public void changeFilter(String filterValue) {
        filteredSiteList.setPredicate(p -> {
            if (filterValue == null || filterValue.isBlank()) return true;
            String lower = filterValue.toLowerCase();
            return p.nameProperty().get().toLowerCase().contains(lower) ||
                    p.locatieProperty().get().toLowerCase().contains(lower) ||
                    p.operationeleStatusProperty().get().toLowerCase().contains(lower) ||
                    p.productieStatusProperty().get().toLowerCase().contains(lower);
        });
    }

    /*
    public ObservableSite addSite(String name, String locatie, int capaciteit, String operationeleStatus, String productieStatus) {
        Site s = controller.addSite(name, locatie, capaciteit, operationeleStatus, productieStatus);
        ObservableSite os = new ObservableSite(s);
        sitesObservableList.add(os);
        return os;
    } */
}
