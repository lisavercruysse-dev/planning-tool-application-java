import domein.*;
import dto.SiteDTO;
import dto.TeamDTO;
import dto.TeamInputDTO;
import exception.WerknemerInformationException;
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
import repository.GebruikerDao;
import repository.GenericDao;
import repository.SiteDao;
import repository.TeamDao;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TeamTest {

    @Mock
    private TeamDao teamRepository;

    @Mock
    private GebruikerDao werknemerRepository;

    @Mock
    private SiteDao siteRepository;

    @InjectMocks
    private TeamManager teamManager;
    private final WerknemerController werknemerController = new WerknemerController();

    private static Werknemer VERANTWOORDELIJKE;
    private static final LocalDate GEBOORTEDATUM = LocalDate.of(2000, 1, 1);
    private static final Site SITE = new Site("Site noord", "Gent", 100, OperationeleStatus.ACTIEF, ProductieStatus.GEZOND);

    private static Stream<Arguments> correcteWaardenToevoegenTeam() throws WerknemerInformationException {
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

        return Stream.of(
                Arguments.of(VERANTWOORDELIJKE, "Team A", SITE),
                Arguments.of(VERANTWOORDELIJKE, "B1", SITE)
        );
    }

    private static Stream<Arguments> fouteWaardenToevoegenTeam() throws WerknemerInformationException {
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

        return Stream.of(
                Arguments.of(null, "Team A", SITE),
                Arguments.of(null, "Team A", null),
                Arguments.of(VERANTWOORDELIJKE, "Team A", null),
                Arguments.of(VERANTWOORDELIJKE, "", SITE),
                Arguments.of(VERANTWOORDELIJKE, "       ", SITE),
                Arguments.of(VERANTWOORDELIJKE, "a", SITE)
        );
    }

    private static Stream<Arguments> bestaandTeamWaarden() throws WerknemerInformationException {
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

        return Stream.of(
                Arguments.of(VERANTWOORDELIJKE, "Team A", SITE),
                Arguments.of(VERANTWOORDELIJKE, "team a", SITE)
        );
    }

    @ParameterizedTest
    @MethodSource("correcteWaardenToevoegenTeam")
    public void addTeamTest(Werknemer verantwoordelijke, String naam, Site site) {
        Mockito.when(werknemerRepository.get(verantwoordelijke.getId()))
                .thenReturn(verantwoordelijke);

        Mockito.when(siteRepository.get(site.getId()))
                .thenReturn(site);

        TeamInputDTO dto = new TeamInputDTO(naam, new SiteDTO(site.getId(), site.getName(), site.getLocatie(), site.getCapaciteit(),
                site.getProductieStatus().toString(), site.getOperationeleStatus().toString()), werknemerController.toDTO(verantwoordelijke));
        Mockito.when(teamRepository.findAll()).thenReturn(Collections.emptyList());
        Team team = teamManager.addTeam(dto);

        verify(teamRepository).startTransaction();
        verify(teamRepository).insert(team);
        verify(teamRepository).commitTransaction();

        assertEquals(verantwoordelijke.getId(), team.getVerantwoordelijke().getId());
        assertEquals(SITE, team.getSite());
    }

    @ParameterizedTest
    @MethodSource("bestaandTeamWaarden")
    public void addTeamBestaatAlTest(Werknemer verantwoordelijke, String naam, Site site) {
        TeamInputDTO dto = new TeamInputDTO(naam, new SiteDTO(site.getId(), site.getName(), site.getLocatie(), site.getCapaciteit(),
                site.getProductieStatus().toString(), site.getOperationeleStatus().toString()), werknemerController.toDTO(verantwoordelijke));
        Team bestaandTeam = new Team(VERANTWOORDELIJKE, "Team A", site);
        Mockito.lenient().when(teamRepository.findAll()).thenReturn(List.of(bestaandTeam));

        assertThrows(IllegalArgumentException.class, () -> {
            teamManager.addTeam(dto);
        });
    }

    @Test
    public void addTeamRollbackBijFoutTest() {
        TeamInputDTO dto = new TeamInputDTO("Site A", new SiteDTO(SITE.getId(), SITE.getName(), SITE.getLocatie(), SITE.getCapaciteit(),
                SITE.getProductieStatus().toString(), SITE.getOperationeleStatus().toString()), werknemerController.toDTO(VERANTWOORDELIJKE));
        Mockito.when(teamRepository.findAll()).thenReturn(Collections.emptyList());
        doThrow(new RuntimeException()).when(teamRepository).insert(any());
        assertThrows(RuntimeException.class, () -> teamManager.addTeam(dto));
        verify(teamRepository).rollbackTransaction();
    }

    @Test
    public void deleteTeamSuccesTest() {
        Team team = new Team(VERANTWOORDELIJKE, "Leeg Team", SITE);
        team.getWerknemers().clear();
        TeamDTO dto = new TeamDTO(1, team.getNaam());

        Mockito.when(teamRepository.get(any())).thenReturn(team);

        assertDoesNotThrow(() -> teamManager.deleteTeam(dto));
    }

    @Test
    public void deleteTeamMetLedenGooitExceptionTest() throws WerknemerInformationException{
        Team team = new Team(VERANTWOORDELIJKE, "Team met Leden", SITE);
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
        team.getWerknemers().add(w);

        TeamDTO dto = new TeamDTO(team.getId(), team.getNaam());

        assertThrows(IllegalArgumentException.class, () -> teamManager.deleteTeam(dto));
    }

    @Test
    public void deleteTeamRollbackBijFoutTest() {
        Team team = new Team(VERANTWOORDELIJKE, "Fout Team", SITE);
        team.getWerknemers().clear();

        doThrow(new RuntimeException()).when(teamRepository).delete(any());
        Mockito.when(teamRepository.get(any())).thenReturn(team);

        TeamDTO dto = new TeamDTO(1, team.getNaam());

        assertThrows(RuntimeException.class, () -> teamManager.deleteTeam(dto));
    }
}
