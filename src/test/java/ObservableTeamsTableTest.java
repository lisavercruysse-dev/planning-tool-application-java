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
import org.sdp.sdp.gui.ObservableTeamsTable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ObservableTeamsTableTest {

    @Mock
    private TeamController teamController;
    @Mock
    WerknemerController werknemerController;
    @Mock
    SiteController siteController;

    private static Werknemer VERANTWOORDELIJKE;
    private static final LocalDate GEBOORTEDATUM = LocalDate.of(2000, 1, 1);
    private static final Site SITE = new Site("Site noord", "Gent", 100, OperationeleStatus.ACTIEF, ProductieStatus.GEZOND);
    private ObservableTeamsTable observableTeamsTable;

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
                .bus(1)
                .build();

        Team t1 = new Team(VERANTWOORDELIJKE, "Team A", SITE);
        Team t2 = new Team(VERANTWOORDELIJKE, "Team B", SITE);

        TeamDTO t1DTO = new TeamDTO(t1.getId(), t1.getNaam());
        TeamDTO t2DTO = new TeamDTO(t2.getId(), t2.getNaam());

        WerknemerDTO wDTO = new WerknemerDTO(1, "Bart", "De Smedt", JobTitel.WERKNEMER.name(), "0123456789", GEBOORTEDATUM,
                "België", "9000", "Gent", "Straat", 1, 1, "Bart.DeSmedt@example.com", "actief");
        SiteDTO sDTO = new SiteDTO(1, "Site Noord", "Gent", 100, "Actief", "Gezond");
        Mockito.when(werknemerController.getVerantwoordelijkeVanTeam(Mockito.anyInt()))
                .thenReturn(wDTO);

        Mockito.when(werknemerController.getWerknemersFromTeam(Mockito.anyInt()))
                .thenReturn(List.of(wDTO)); // at least 1 for size()

        Mockito.when(siteController.getSiteFromTeam(Mockito.anyInt()))
                .thenReturn(sDTO);

        Mockito.when(teamController.getAllTeams()).thenReturn(List.of(t1DTO, t2DTO));
        observableTeamsTable = new ObservableTeamsTable(teamController, werknemerController, siteController);
    }

    @Test
    void lijstWordtCorrectOpgevuld() {
        assertEquals(2, observableTeamsTable.getFilteredTeams().size());
    }

    @Test
    void lijstWordtGeupdateBijToevoegen() {

        Team t3 = new Team(VERANTWOORDELIJKE, "Team C", SITE);
        TeamDTO t3DTO = new TeamDTO(t3.getId(), t3.getNaam());

        Mockito.when(teamController.addTeam(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.any()
        )).thenReturn(t3DTO);

        observableTeamsTable.addTeam(VERANTWOORDELIJKE, "Team C", SITE);

        assertEquals(3, observableTeamsTable.getFilteredTeams().size());
    }

}
