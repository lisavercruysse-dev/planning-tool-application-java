package org.sdp.sdp.gui;

import dto.WerknemerDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class ObservableWerknemer {

    @Getter
    private final int id;
    private final StringProperty voornaam;
    private final StringProperty achternaam;
    private final StringProperty jobTitel;
    private final StringProperty email;
    private final StringProperty telefoon;
    private final StringProperty land;
    private final StringProperty postcode;
    private final StringProperty stad;
    private final StringProperty straat;
    private final StringProperty huisnr;
    private final StringProperty bus;

   /* @Getter
    private final Werknemer werknemer;*/ // koppeling naar domein

    public ObservableWerknemer(WerknemerDTO werknemer) {
        id = werknemer.id();
        this.voornaam = new SimpleStringProperty(werknemer.voornaam());
        this.achternaam = new SimpleStringProperty(werknemer.achternaam());
        this.jobTitel = new SimpleStringProperty(werknemer.jobTitel().toLowerCase());
        this.email = new SimpleStringProperty(werknemer.email());
        this.telefoon = new SimpleStringProperty(werknemer.telefoon());
        this.land = new SimpleStringProperty(werknemer.land());
        this.postcode = new SimpleStringProperty(werknemer.postcode());
        this.stad = new SimpleStringProperty(werknemer.stad());
        this.straat = new SimpleStringProperty(werknemer.straat());
        this.huisnr = new SimpleStringProperty(werknemer.huisnummer().toString());
        this.bus = new SimpleStringProperty();

        // wijzigingen in GUI doorgeven aan domein
        //this.voornaam.addListener((obs, oldVal, newVal) -> werknemer.setVoornaam(newVal));
        //this.achternaam.addListener((obs, oldVal, newVal) -> werknemer.setAchternaam(newVal));
    }

    public StringProperty firstNameProperty() { return voornaam; }
    public StringProperty lastNameProperty() { return achternaam; }
    public StringProperty jobTitelProperty() { return jobTitel; }
    public StringProperty emailProperty() { return email; }
    public StringProperty telefoonProperty() { return telefoon; }
    public StringProperty landProperty() { return land; }
    public StringProperty postcodeProperty() { return postcode; }
    public StringProperty stadProperty() { return stad; }
    public StringProperty straatProperty() { return straat; }
    public StringProperty huisnrProperty() { return huisnr; }
    public StringProperty busProperty() { return bus; }

    public String getFirstName() {
        return voornaam.get();
    }
    public String getLastName() {
        return achternaam.get();
    }
    //public String getJobTitel() { return jobTitel.get(); }
    //public String getEmail() { return email.get(); }
}

