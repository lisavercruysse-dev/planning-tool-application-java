import domein.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sdp.sdp.gui.ObservableWerknemerFromTeamList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ObservableTeamsDetailsTest {

    @Mock
    WerknemerController werknemerController;

    private static final Werknemer VERANTWOORDELIJKE =
            new Werknemer("Bart", "De Smedt", JobTitel.VERANTWOORDELIJKE, "12345678", null);

    private static final Site SITE =
            new Site("Site noord", "Gent", 100, "actief", "gezond");

    private ObservableWerknemerFromTeamList list;

    private Team team;

    @BeforeEach
    public void setup() {
        Werknemer w1 = new Werknemer("Simon", "Van Aert", JobTitel.WERKNEMER, "12345678", null);
        Werknemer w2 = new Werknemer("Katrien", "De Bakker", JobTitel.WERKNEMER, "12345678", null);

        team = new Team(VERANTWOORDELIJKE, "Team A", SITE);
        team.getWerknemers().addAll(List.of(w1, w2));

        Mockito.when(werknemerController.getWerknemersFromTeam(team.getId()))
                .thenReturn(List.of(w1, w2));

        list = new ObservableWerknemerFromTeamList(werknemerController, team.getId());
    }

    @Test
    void lijstWordtCorrectOpgevuld() {
        assertEquals(2, list.getObservableList().size());
    }
}