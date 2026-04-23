package org.sdp.sdp.gui;

import domein.SiteController;
import domein.Team;
import domein.TeamController;
import domein.WerknemerController;
import dto.SiteDTO;
import dto.TeamDTO;
import dto.WerknemerDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.util.List;

public class ObservableTeam {
    private final StringProperty naam;
    private final StringProperty verantwoordelijke;
    private final StringProperty site;
    private final StringProperty aantalWerknemers;

    @Getter
    private final TeamDTO team;
    private final WerknemerDTO verantwoordelijkeDTO;
    private final List<WerknemerDTO> werknemers;
    private final SiteDTO siteDTO;

    public ObservableTeam(TeamDTO team, WerknemerController werknemerController, SiteController siteController) {
        this.team = team;
        this.verantwoordelijkeDTO = werknemerController.getVerantwoordelijkeVanTeam(team.id());
        this.werknemers = werknemerController.getWerknemersFromTeam(team.id());
        this.siteDTO = siteController.getSiteFromTeam(team.id());
        this.naam = new SimpleStringProperty(team.naam());
        this.verantwoordelijke = new SimpleStringProperty(verantwoordelijkeDTO.voornaam() + " " + verantwoordelijkeDTO.achternaam());
        this.site = new SimpleStringProperty(siteDTO.naam());
        this.aantalWerknemers = new SimpleStringProperty(String.valueOf(werknemers.size()));
    }

    public StringProperty naamProperty() {return naam;}
    public StringProperty verantwoordelijkeProperty() {return verantwoordelijke;}
    public StringProperty siteProperty() {return site;}
    public StringProperty aantalWerknemersProperty() {return aantalWerknemers;}
}
