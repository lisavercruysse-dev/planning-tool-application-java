import domein.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sdp.sdp.gui.ObservableTeam;
import org.sdp.sdp.gui.ObservableTeamsTable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ObservableTeamsTableTest {

    @Mock
    private TeamController teamController;

    @Mock
    private WerknemerController werknemerController;

    private static final Werknemer VERANTWOORDELIJKE = new Werknemer("Bart", "De Smedt", JobTitel.VERANTWOORDELIJKE, "12345678", null);
    private static final Site SITE = new Site("Site noord", "Gent", 100, "actief", "gezond");
    private ObservableTeamsTable observableTeamsTable;

    @BeforeEach
    public void setup() {
        Team t1 = new Team(VERANTWOORDELIJKE, "Team A", SITE);
        Team t2 = new Team(VERANTWOORDELIJKE, "Team B", SITE);

        Mockito.when(teamController.getAllTeams()).thenReturn(List.of(t1, t2));
        observableTeamsTable = new ObservableTeamsTable(teamController);
    }

    @Test
    void lijstWordtCorrectOpgevuld() {
        assertEquals(2, observableTeamsTable.getFilteredTeams().size());
    }

    @Test
    void lijstWordtGeupdateBijToevoegen() {

        Team t3 = new Team(VERANTWOORDELIJKE, "Team C", SITE);

        Mockito.when(teamController.addTeam(
                Mockito.any(),
                Mockito.anyString(),
                Mockito.any()
        )).thenReturn(t3);

        observableTeamsTable.addTeam(VERANTWOORDELIJKE, "Team C", SITE);

        assertEquals(3, observableTeamsTable.getFilteredTeams().size());
    }

    @Test
    void werknemerCountWordtGeupdateBijToevoegenAanTeam() {
        Team team = new Team(VERANTWOORDELIJKE, "Team A", SITE);
        ObservableTeam observableTeam = new ObservableTeam(team);

        int startAantal =  Integer.parseInt(observableTeam.aantalWerknemersProperty().get());

        Werknemer w = new Werknemer("Pieter", "Willems", JobTitel.WERKNEMER, "12345678", null);

        werknemerController.voegWerknemerToeAanTeam(w.getId(), team.getId());
        assertEquals(startAantal + 1, observableTeamsTable.getFilteredTeams().size());
    }
}
