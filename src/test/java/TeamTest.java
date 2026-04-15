import domein.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GenericDao;
import repository.TeamDao;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeamTest {

    @Mock
    private TeamDao teamRepository;

    @InjectMocks
    private TeamManager teamManager;

    private static final Werknemer VERANTWOORDELIJKE = new Werknemer("Bart", "De Smedt", JobTitel.VERANTWOORDELIJKE, "12345678", null);
    private static final Site SITE = new Site("Site noord", "Gent", 100, "actief", "gezond");

    private static Stream<Arguments> correcteWaardenToevoegenTeam() {
        return Stream.of(
                Arguments.of(VERANTWOORDELIJKE, "Team A", SITE),
                Arguments.of(VERANTWOORDELIJKE, "B1", SITE)
        );
    }

    private static Stream<Arguments> fouteWaardenToevoegenTeam() {
        return Stream.of(
                Arguments.of(null, "Team A", SITE),
                Arguments.of(null, "Team A", null),
                Arguments.of(VERANTWOORDELIJKE, "Team A", null),
                Arguments.of(VERANTWOORDELIJKE, "", SITE),
                Arguments.of(VERANTWOORDELIJKE, "       ", SITE),
                Arguments.of(VERANTWOORDELIJKE, "a", SITE)
        );
    }

    private static Stream<Arguments> bestaandTeamWaarden() {
        return Stream.of(
                Arguments.of(VERANTWOORDELIJKE, "Team A", SITE),
                Arguments.of(VERANTWOORDELIJKE, "team a", SITE)
        );
    }

    @ParameterizedTest
    @MethodSource("correcteWaardenToevoegenTeam")
    public void addTeamTest(Werknemer verantwoordelijke, String naam, Site site) {
        Mockito.when(teamRepository.findAll()).thenReturn(Collections.emptyList());
        Team team = teamManager.addTeam(verantwoordelijke, naam, site);

        verify(teamRepository).startTransaction();
        verify(teamRepository).insert(team);
        verify(teamRepository).commitTransaction();

        assertEquals(VERANTWOORDELIJKE, team.getVerantwoordelijke());
        assertEquals(SITE, team.getSite());
        assertTrue(VERANTWOORDELIJKE.getTeams().contains(team));
    }

    @ParameterizedTest
    @MethodSource("fouteWaardenToevoegenTeam")
    public void addTeamNullVerantwoordelijkeTest(Werknemer verantwoordelijke, String naam, Site site) {
        assertThrows(IllegalArgumentException.class, () -> {
            teamManager.addTeam(verantwoordelijke, naam, site);
        });
    }

    @ParameterizedTest
    @MethodSource("bestaandTeamWaarden")
    public void addTeamBestaatAl(Werknemer verantwoordelijke, String naam, Site site) {
        Team bestaandTeam = new Team(VERANTWOORDELIJKE, "Team A", site);
        Mockito.when(teamRepository.findAll()).thenReturn(List.of(bestaandTeam));

        assertThrows(IllegalArgumentException.class, () -> {
            teamManager.addTeam(verantwoordelijke, naam, site);
        });
    }

    @Test
    public void addTeamRollbackBijFoutTest() {
        Mockito.when(teamRepository.findAll()).thenReturn(Collections.emptyList());
        doThrow(new RuntimeException()).when(teamRepository).insert(any());
        assertThrows(RuntimeException.class, () -> teamManager.addTeam(VERANTWOORDELIJKE, "Site A", SITE));
        verify(teamRepository).rollbackTransaction();
    }
}
