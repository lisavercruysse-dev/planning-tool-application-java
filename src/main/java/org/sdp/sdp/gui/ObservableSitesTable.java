package org.sdp.sdp.gui;

import domein.*;
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
    private final FilteredList<ObservableSite> filteredSiteList;

    public ObservableSitesTable(SiteController controller) {
        this.controller = controller;
        this.sitesObservableList = FXCollections.observableArrayList();

        List<Site> sites = this.controller.getAllSites();
        if (sites != null) {
            sites.forEach(site -> sitesObservableList.add(new ObservableSite(site)));
        }
        this.filteredSiteList = new FilteredList<>(sitesObservableList, site -> true);
    }

    public void changeFilter(String filterValue) {
        filteredSiteList.setPredicate(p -> {
            if (filterValue == null || filterValue.isBlank()) return true;
            String lower = filterValue.toLowerCase();
            return p.nameProperty().get().toLowerCase().contains(lower) ||
                    Objects.toString(p.locatieProperty().get(),"").toLowerCase().contains(lower) ||
                    Objects.toString(p.operationeleStatusProperty().get(),"").toLowerCase().contains(lower) ||
                    Objects.toString(p.productieStatusProperty().get(),"").toLowerCase().contains(lower);
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
