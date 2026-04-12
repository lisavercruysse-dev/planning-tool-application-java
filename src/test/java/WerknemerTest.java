import domein.JobTitel;
import domein.Team;
import domein.Werknemer;
import domein.WerknemerManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GenericDao;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WerknemerTest {

    @Mock
    private GenericDao<Werknemer> werknemerRepo;

    @InjectMocks
    private WerknemerManager werknemerManager;

    private static final Werknemer VERANTWOORDELIJKE = new Werknemer("Bart", "De Smedt", JobTitel.VERANTWOORDELIJKE, "12345678", null);
    private static final Team TEAM = new Team(VERANTWOORDELIJKE);

    private static Stream<Arguments> correcteWaardenToevoegenWerknemer() {
        return Stream.of(
                Arguments.of("Thomas", "De Bakker", "werknemer", "12345678", null),
                Arguments.of("Thomas", "De Bakker", "werknemer", "12345678", TEAM),
                Arguments.of("An", "De Bakker", "werknemer", "12345678", TEAM),
                Arguments.of("An", "An", "werknemer", "12345678", TEAM)
        );
    }

    private static Stream<Arguments> fouteWaardenToevoegenWerknemer() {
        return Stream.of(
                Arguments.of("A", "De Bakker", "werknemer", "12345678", TEAM),
                Arguments.of("An", "B", "werknemer", "12345678", TEAM),
                Arguments.of("", "Bakker", "werknemer", "12345678", TEAM),
                Arguments.of("Thomas", "", "werknemer", "12345678", TEAM),
                Arguments.of("       ", "bakker","werknemer", "12345678", TEAM),
                Arguments.of("           ", "          ", "werknemer", "12345678", TEAM),
                Arguments.of("Thomas", "        ", "werknemer", "12345678", TEAM),
                Arguments.of("Thomas", "De Bakker", "", "12345678", TEAM),
                Arguments.of("Thomas", "De Bakker", "         ", "12345678", TEAM),
                Arguments.of("Thomas", "De Bakker", "Directeur", "12345678", TEAM),
                Arguments.of("Thomas", "De Bakker", "werknemer", "1234567", TEAM),
                Arguments.of("Thomas", "De Bakker", "werknemer", "hallo", TEAM),
                Arguments.of("Thomas", "De Bakker", "werknemer", "", TEAM),
                Arguments.of("Thomas", "De Bakker", "werknemer", "       ", TEAM),
                Arguments.of("11111", "De Bakker", "werknemer", "12345678", TEAM),
                Arguments.of("Thomas23", "De Bakker", "werknemer", "12345678", TEAM),
                Arguments.of("11", "De Bakker", "werknemer", "12345678", TEAM),
                Arguments.of("Thomas", "De1 Bakker", "werknemer", "12345678", TEAM),
                Arguments.of("11", "a1", "werknemer", "12345678", TEAM),
                Arguments.of("11", "111111111", "werknemer", "12345678", TEAM)
        );
    }

    @ParameterizedTest
    @MethodSource("correcteWaardenToevoegenWerknemer")
    public void addWerknemerTest(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        Werknemer werknemer = werknemerManager.addWerknemer(voornaam, achternaam, jobtitel, wachtwoord, team);

        verify(werknemerRepo).startTransaction();
        verify(werknemerRepo).insert(werknemer);
        verify(werknemerRepo).commitTransaction();

        String expectedEmail = voornaam + "." + achternaam.replaceAll(" ", "") + "@example.com";
        assertEquals(expectedEmail, werknemer.getEmail());
    }

    @ParameterizedTest
    @MethodSource("fouteWaardenToevoegenWerknemer")
    public void addWerknemerFoutTest(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        assertThrows(IllegalArgumentException.class, () -> {
            werknemerManager.addWerknemer(voornaam, achternaam, jobtitel, wachtwoord, team);
        });
    }

    @Test
    public void addTeamRollbackBijFoutTest() {
        doThrow(new RuntimeException()).when(werknemerRepo).insert(any());
        assertThrows(RuntimeException.class, () -> werknemerManager.addWerknemer("Thomas", "De Bakker", "werknemer", "12345678", TEAM));
        verify(werknemerRepo).rollbackTransaction();
    }
}