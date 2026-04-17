package org.sdp.sdp.gui;

import domein.Team;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class ObservableTeam {
    private final StringProperty naam;
    private final StringProperty verantwoordelijke;
    private final StringProperty site;
    private final StringProperty aantalWerknemers;

    @Getter
    private final Team team;

    public ObservableTeam(Team team) {
        this.team = team;
        this.naam = new SimpleStringProperty(team.getNaam());
        this.verantwoordelijke = new SimpleStringProperty(team.getVerantwoordelijke().getVoornaam() + " " + team.getVerantwoordelijke().getAchternaam());
        this.site = new SimpleStringProperty(team.getSite().getName());
        this.aantalWerknemers = new SimpleStringProperty(String.valueOf(team.getWerknemers().size()));
    }

    public StringProperty naamProperty() {return naam;}
    public StringProperty verantwoordelijkeProperty() {return verantwoordelijke;}
    public StringProperty siteProperty() {return site;}
    public StringProperty aantalWerknemersProperty() {return aantalWerknemers;}
}
