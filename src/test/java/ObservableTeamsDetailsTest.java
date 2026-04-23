import domein.*;
import dto.SiteDTO;
import dto.TeamDTO;
import dto.WerknemerDTO;
import exception.WerknemerInformationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sdp.sdp.gui.ObservableTeam;
import org.sdp.sdp.gui.ObservableWerknemerFromTeamList;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ObservableTeamsDetailsTest {

    @Mock
    WerknemerController werknemerController;
    @Mock
    SiteController siteController;
    @Mock
    TeamController teamController;

    private ObservableTeam observableTeam;
    private static final LocalDate GEBOORTEDATUM = LocalDate.of(2000, 1, 1);
    private static Werknemer VERANTWOORDELIJKE;
    private static final Site SITE = new Site("Site noord", "Gent", 100, "actief", "gezond");
    private ObservableWerknemerFromTeamList list;
    private Team team;

    @BeforeEach
    public void setup() throws WerknemerInformationException {
        VERANTWOORDELIJKE = Werknemer.builder()
                .voornaam("Bart")
                .achternaam("De Smedt")
                .jobTitel(JobTitel.VERANTWOORDELIJKE)
                .geboortedatum(GEBOORTEDATUM)
                .land("België")
                .postcode("9000")
                .stad("Gent")
                .straat("Vlaanderenstraat")
                .huisnummer(12)
                .build();

        Werknemer w1 = Werknemer.builder()
                .voornaam("Simon")
                .achternaam("Van Aert")
                .jobTitel(JobTitel.WERKNEMER)
                .geboortedatum(GEBOORTEDATUM)
                .telefoon("123456789")
                .land("België")
                .postcode("9000")
                .stad("Gent")
                .straat("Vlaanderenstraat")
                .huisnummer(12)
                .build();

        Werknemer w2 = Werknemer.builder()
                .voornaam("Katrien")
                .achternaam("De Bakker")
                .jobTitel(JobTitel.WERKNEMER)
                .geboortedatum(GEBOORTEDATUM)
                .telefoon("123456789")
                .land("België")
                .postcode("9000")
                .stad("Gent")
                .straat("Vlaanderenstraat")
                .huisnummer(12)
                .build();

        team = new Team(VERANTWOORDELIJKE, "Team A", SITE);
        team.getWerknemers().addAll(List.of(w1, w2));

        TeamDTO teamDTO = new TeamDTO(team.getId(), team.getNaam());

        Mockito.when(werknemerController.getVerantwoordelijkeVanTeam(team.getId()))
                .thenReturn(new WerknemerDTO(VERANTWOORDELIJKE.getId(), VERANTWOORDELIJKE.getVoornaam(), VERANTWOORDELIJKE.getAchternaam(), VERANTWOORDELIJKE.getJobTitel().name(), VERANTWOORDELIJKE.getTelefoon(), VERANTWOORDELIJKE.getGeboortedatum(), VERANTWOORDELIJKE.getLand(), VERANTWOORDELIJKE.getPostcode(), VERANTWOORDELIJKE.getStad(), VERANTWOORDELIJKE.getStraat(), VERANTWOORDELIJKE.getHuisnummer(), VERANTWOORDELIJKE.getBus(), VERANTWOORDELIJKE.getEmail(), VERANTWOORDELIJKE.getStatus()));

        Mockito.when(siteController.getSiteFromTeam(team.getId()))
                .thenReturn(new SiteDTO(SITE.getId(), SITE.getName()));

        Mockito.when(werknemerController.getWerknemersFromTeam(team.getId()))
                .thenReturn(List.of(
                        new WerknemerDTO(w1.getId(), w1.getVoornaam(), w1.getAchternaam(), w1.getJobTitel().name(), w1.getTelefoon(), w1.getGeboortedatum(), w1.getLand(), w1.getPostcode(), w1.getStad(), w1.getStraat(), w1.getHuisnummer(), w1.getBus(), w1.getEmail(), w1.getStatus()),
                        new WerknemerDTO(w2.getId(), w2.getVoornaam(), w2.getAchternaam(), w2.getJobTitel().name(), w2.getTelefoon(), w2.getGeboortedatum(), w2.getLand(), w2.getPostcode(), w2.getStad(), w2.getStraat(), w2.getHuisnummer(), w2.getBus(), w2.getEmail(), w2.getStatus())
                ));

        observableTeam = new ObservableTeam(teamDTO, werknemerController, siteController);

        list = new ObservableWerknemerFromTeamList(werknemerController, team.getId(), observableTeam);
    }

    @Test
    void lijstWordtCorrectOpgevuld() {
        assertEquals(2, list.getObservableList().size());
    }

    @Test
    void lijstWordtCorrectGeupdate() throws WerknemerInformationException {
        Werknemer w3 = Werknemer.builder()
                .voornaam("Jana")
                .achternaam("De Mol")
                .jobTitel(JobTitel.WERKNEMER)
                .geboortedatum(GEBOORTEDATUM)
                .land("België")
                .postcode("9000")
                .telefoon("123456789")
                .stad("Gent")
                .straat("Vlaanderenstraat")
                .huisnummer(12)
                .build();

        WerknemerDTO w3DTO = new WerknemerDTO(w3.getId(), w3.getVoornaam(), w3.getAchternaam(), w3.getJobTitel().name(), w3.getTelefoon(), w3.getGeboortedatum(), w3.getLand(), w3.getPostcode(), w3.getStad(), w3.getStraat(), w3.getHuisnummer(), w3.getBus(), w3.getEmail(), w3.getStatus());

        Mockito.when(werknemerController.voegWerknemerToeAanTeam(w3.getId(), team.getId()))
                .thenReturn(w3);

        list.addWerknemer(w3DTO);
        assertEquals(3, list.getObservableList().size());
    }
}