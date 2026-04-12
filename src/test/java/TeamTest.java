import domein.JobTitel;
import domein.Team;
import domein.TeamManager;
import domein.Werknemer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GenericDao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeamTest {

    @Mock
    private GenericDao<Team> teamRepository;

    @InjectMocks
    private TeamManager teamManager;

    private static final Werknemer VERANTWOORDELIJKE = new Werknemer("Bart", "De Smedt", JobTitel.VERANTWOORDELIJKE, "12345678", null);

    @Test
    public void addTeamTest() {
        Team team = teamManager.addTeam(VERANTWOORDELIJKE);

        verify(teamRepository).startTransaction();
        verify(teamRepository).insert(team);
        verify(teamRepository).commitTransaction();

        assertEquals(VERANTWOORDELIJKE, team.getVerantwoordelijke());
        assertTrue(VERANTWOORDELIJKE.getTeams().contains(team));
    }

    @Test
    public void addTeamNullVerantwoordelijkeTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            teamManager.addTeam(null);
        });
    }

    @Test
    public void addTeamRollbackBijFoutTest() {
        doThrow(new RuntimeException()).when(teamRepository).insert(any());
        assertThrows(RuntimeException.class, () -> teamManager.addTeam(VERANTWOORDELIJKE));
        verify(teamRepository).rollbackTransaction();
    }
}
