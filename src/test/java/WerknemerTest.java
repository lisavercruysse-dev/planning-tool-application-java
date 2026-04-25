import domein.*;
import dto.WerknemerInputDTO;
import exception.WerknemerInformationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GebruikerDao;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WerknemerTest {

    @Mock
    private GebruikerDao werknemerRepo;

    @InjectMocks
    private WerknemerManager werknemerManager;

    private static Werknemer VERANTWOORDELIJKE;
    private static Site SITE;
    private static Team TEAM;
    private static LocalDate GEBOORTEDATUM = LocalDate.of(2000, 1, 1);

    @BeforeEach
    public void setUp()  throws WerknemerInformationException {
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

        SITE = new Site("Site noord", "Gent", 100, "actief", "gezond");
        TEAM = new Team(VERANTWOORDELIJKE, "Team A", SITE);
    }

    private static Stream<Arguments> correcteWaardenToevoegenWerknemer() {
        return Stream.of(
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678",  12, 2, TEAM),
                Arguments.of("An", "De Bakker", JobTitel.WERKNEMER, "0412345678",  12, 2, TEAM),
                Arguments.of("Thomas", "De", JobTitel.WERKNEMER, "0412345678", 12, 2, TEAM),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "1234567", 12, 2, TEAM),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "123456789012345", 12, 2, TEAM),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "123456789012345", 1, 2, TEAM),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "123456789012345", 12, 1, TEAM),
                Arguments.of("Thomas", "De Bakker", JobTitel.MANAGER, null, 12, 1, TEAM),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", 12, 1, null)
                );
    }

    private static Stream<Arguments> fouteWaardenToevoegenWerknemer() {
        return Stream.of(
                //null waarden
                Arguments.of("T", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of(null, "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", null, JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", null, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, null, "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", null, "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", null, "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", null,"Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", null, 12, 2),

                //huis & busnummers
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 0, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 0),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", -2, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, -2),

                //voor & achternaam
                Arguments.of("Thomas", "D", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "  De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("     ", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "       ", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),

                //telefoon
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "        ", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "041234", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "1234567890123456", "België", "9000", "Gent", "Vlaanderenstraat", 12, 2),

                //land
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "", "9000", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "     ", "9000", "Gent", "Vlaanderenstraat", 12, 2),

                //postcode & stad
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "    ", "Gent", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "", "Vlaanderenstraat", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "    ", "Vlaanderenstraat", 12, 2),

                //straat
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "", 12, 2),
                Arguments.of("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", "België", "9000", "Gent", "    ", 12, 2)

        );
    }

    @ParameterizedTest
    @MethodSource("correcteWaardenToevoegenWerknemer")
    public void addWerknemerTest(String voornaam, String achternaam, JobTitel jobTitel,
                                 String telefoon, Integer huisnummer, Integer bus, Team team) throws WerknemerInformationException{
        WerknemerInputDTO dto = new WerknemerInputDTO(voornaam, achternaam, jobTitel, telefoon, GEBOORTEDATUM, "België", "9000", "Gent", "Vlaanderenstraat", huisnummer, bus);
        Werknemer werknemer = werknemerManager.addWerknemer(dto);

        verify(werknemerRepo).startTransaction();
        verify(werknemerRepo).insert(werknemer);
        verify(werknemerRepo).commitTransaction();

        String expectedEmail = voornaam + "." + achternaam.replaceAll(" ", "") + "@example.com";
        assertEquals(expectedEmail, werknemer.getEmail());
    }

    @ParameterizedTest
    @MethodSource("fouteWaardenToevoegenWerknemer")
    public void addWerknemerFoutTest(String voornaam, String achternaam, JobTitel jobTitel,
                                     String telefoon, String land, String postcode,
                                     String stad, String straat, Integer huisnummer, Integer bus) {
        WerknemerInputDTO dto = new WerknemerInputDTO(voornaam, achternaam, jobTitel, telefoon, GEBOORTEDATUM, land, postcode, stad, straat, huisnummer, bus);
        assertThrows(WerknemerInformationException.class, () -> werknemerManager.addWerknemer(dto));
    }

    @Test
    public void addTeamRollbackBijFoutTest() {
        doThrow(new RuntimeException()).when(werknemerRepo).insert(any());
        WerknemerInputDTO dto = new WerknemerInputDTO("Thomas", "De Bakker", JobTitel.WERKNEMER, "0412345678", GEBOORTEDATUM, "België", "9000", "Gent", "Vlaanderenstraat", 12, 1);
        assertThrows(RuntimeException.class, () -> werknemerManager.addWerknemer(dto));
        verify(werknemerRepo).rollbackTransaction();
    }

    @Test
    public void voegWerknemerToeAanTeamTest() throws WerknemerInformationException {
        Werknemer w = Werknemer.builder()
                .voornaam("Pieter")
                .achternaam("Willems")
                .jobTitel(JobTitel.WERKNEMER)
                .telefoon("0412345678")
                .geboortedatum(GEBOORTEDATUM)
                .land("België")
                .postcode("9000")
                .stad("Gent")
                .straat("Vlaanderenstraat")
                .huisnummer(12)
                .build();

        Mockito.when(werknemerRepo.get(w.getId())).thenReturn(w);
        Mockito.when(werknemerRepo.voegWerknemerToeAanTeam(w.getId(), TEAM.getId())).thenReturn(w);

        Werknemer result = werknemerManager.voegWerknemerToeAanTeam(w.getId(), TEAM.getId());

        assertEquals(w, result);
        Mockito.verify(werknemerRepo).startTransaction();
        Mockito.verify(werknemerRepo).voegWerknemerToeAanTeam(w.getId(), TEAM.getId());
        Mockito.verify(werknemerRepo).commitTransaction();
    }

    @Test
    public void voegWerknemerToeAanTeamTest_werknemerAlInTeam() throws WerknemerInformationException {
        Werknemer w = Werknemer.builder()
                .voornaam("Pieter")
                .achternaam("Willems")
                .jobTitel(JobTitel.WERKNEMER)
                .telefoon("0412345678")
                .geboortedatum(GEBOORTEDATUM)
                .land("België")
                .postcode("9000")
                .stad("Gent")
                .straat("Vlaanderenstraat")
                .huisnummer(12)
                .build();

        w.getTeams().add(TEAM);

        Mockito.when(werknemerRepo.get(w.getId())).thenReturn(w);
        assertThrows(IllegalArgumentException.class, () -> werknemerManager.voegWerknemerToeAanTeam(w.getId(), TEAM.getId()));
    }

    @Test
    public void verwijderWerknemerUitTeamTest() throws WerknemerInformationException {
        Werknemer w = Werknemer.builder()
                .voornaam("Pieter")
                .achternaam("Willems")
                .jobTitel(JobTitel.WERKNEMER)
                .telefoon("0412345678")
                .geboortedatum(GEBOORTEDATUM)
                .land("België")
                .postcode("9000")
                .stad("Gent")
                .straat("Vlaanderenstraat")
                .huisnummer(12)
                .build();

        TEAM.getWerknemers().add(w);
        w.getTeams().add(TEAM);

        Mockito.when(werknemerRepo.get(w.getId())).thenReturn(w);

        assertDoesNotThrow(() -> werknemerManager.verwijderWerknemerUitTeam(w.getId(), TEAM.getId()));
        assertFalse(w.getTeams().contains(TEAM));
        assertFalse(TEAM.getWerknemers().contains(w));
    }
}
