package org.sdp.sdp.gui;

import domein.Site;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class ObservableSite {
    private final StringProperty name;
    private final StringProperty locatie;
    private final StringProperty capaciteit;
    private final StringProperty operationeleStatus;
    private final StringProperty productieStatus;

    @Getter
    private final Site site; // koppeling naar domein

    public ObservableSite(Site site) {
        this.site = site;
        this.name = new SimpleStringProperty(site.getName());
        this.locatie = new SimpleStringProperty(site.getLocatie());
        this.capaciteit = new SimpleStringProperty(String.valueOf(site.getCapaciteit()));
        this.operationeleStatus = new SimpleStringProperty(site.getOperationeleStatus());
        this.productieStatus = new SimpleStringProperty(site.getProductieStatus());

        // wijzigingen in GUI doorgeven aan domein
        // this.name.addListener((obs, oldVal, newVal) -> site.setName(newVal));
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty locatieProperty() { return locatie; }
    public StringProperty capaciteitProperty() { return capaciteit; }
    public StringProperty operationeleStatusProperty() { return operationeleStatus; }
    public StringProperty productieStatusProperty() { return productieStatus; }

    public String getname() {
        return name.get();
    }

}
