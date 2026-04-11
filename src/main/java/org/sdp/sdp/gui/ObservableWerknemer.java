package org.sdp.sdp.gui;

import domein.Werknemer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class ObservableWerknemer {

    private final StringProperty voornaam;
    private final StringProperty achternaam;
    private final StringProperty jobTitel;
    private final StringProperty email;

    @Getter
    private final Werknemer werknemer; // koppeling naar domein

    public ObservableWerknemer(Werknemer werknemer) {
        this.werknemer = werknemer;
        this.voornaam = new SimpleStringProperty(werknemer.getVoornaam());
        this.achternaam = new SimpleStringProperty(werknemer.getAchternaam());
        this.jobTitel = new SimpleStringProperty(werknemer.getJobTitel().name());
        this.email = new SimpleStringProperty(werknemer.getEmail());

        // wijzigingen in GUI doorgeven aan domein
        this.voornaam.addListener((obs, oldVal, newVal) -> werknemer.setVoornaam(newVal));
        this.achternaam.addListener((obs, oldVal, newVal) -> werknemer.setAchternaam(newVal));
    }

    public StringProperty firstNameProperty() { return voornaam; }
    public StringProperty lastNameProperty() { return achternaam; }
    public StringProperty jobTitelProperty() { return jobTitel; }
    public StringProperty emailProperty() { return email; }

    public String getFirstName() {
        return voornaam.get();
    }
    public String getLastName() {
        return achternaam.get();
    }
    //public String getJobTitel() { return jobTitel.get(); }
    //public String getEmail() { return email.get(); }
}

