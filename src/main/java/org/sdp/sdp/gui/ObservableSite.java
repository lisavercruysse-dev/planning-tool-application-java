package org.sdp.sdp.gui;

import domein.Site;
import dto.SiteDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class ObservableSite {

    @Getter
    private final int id;
    private final StringProperty name;
    private final StringProperty locatie;
    private final StringProperty capaciteit;
    private final StringProperty operationeleStatus;
    private final StringProperty productieStatus;

    public ObservableSite(SiteDTO site) {
        id = site.id();
        this.name = new SimpleStringProperty(site.name());
        this.locatie = new SimpleStringProperty(site.locatie());
        this.capaciteit = new SimpleStringProperty(String.valueOf(site.capaciteit()));
        this.operationeleStatus = new SimpleStringProperty(site.operationeleStatus());
        this.productieStatus = new SimpleStringProperty(site.productieStatus());
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty locatieProperty() { return locatie; }
    public StringProperty capaciteitProperty() { return capaciteit; }
    public StringProperty operationeleStatusProperty() { return operationeleStatus; }
    public StringProperty productieStatusProperty() { return productieStatus; }

    public String getName() {
        return name.get();
    }

}
