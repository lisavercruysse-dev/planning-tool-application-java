import domein.JobTitel;
import domein.Team;
import domein.Werknemer;
import domein.WerknemerController;
import dto.WerknemerDTO;
import exception.WerknemerInformationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sdp.sdp.gui.ObservableWerknemer;
import org.sdp.sdp.gui.ObservableWerknemersTable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObservableWerknemersTableTest {

    private WerknemerController controller;
    private ObservableWerknemersTable table;

    private static final LocalDate GEBOORTEDATUM = LocalDate.of(2000, 1, 1);

    private Werknemer w1;
    private Werknemer w2;

    @BeforeEach
    void setUp() throws WerknemerInformationException {

        controller = mock(WerknemerController.class);

        w1 = Werknemer.builder()
                .voornaam("Jan")
                .achternaam("Jansen")
                .jobTitel(JobTitel.MANAGER)
                .geboortedatum(GEBOORTEDATUM)
                .land("België")
                .postcode("1000")
                .stad("Brussel")
                .straat("Kerkstraat")
                .huisnummer(1)
                .build();

        w2 = Werknemer.builder()
                .voornaam("Katrien")
                .achternaam("De Bakker")
                .jobTitel(JobTitel.WERKNEMER)
                .telefoon("0412345678")
                .geboortedatum(GEBOORTEDATUM)
                .land("België")
                .postcode("9000")
                .stad("Gent")
                .straat("Vlaanderenstraat")
                .huisnummer(12)
                .build();

        when(controller.getWerknemers()).thenReturn(List.of(
                new WerknemerDTO(w1.getId(), w1.getVoornaam(), w1.getAchternaam(), w1.getJobTitel().name(), w1.getTelefoon(), w1.getGeboortedatum(), w1.getLand(), w1.getPostcode(), w1.getStad(), w1.getStraat(), w1.getHuisnummer(), w1.getBus(), w1.getEmail(), w1.getStatus()),
                new WerknemerDTO(w2.getId(), w2.getVoornaam(), w2.getAchternaam(), w2.getJobTitel().name(), w2.getTelefoon(), w2.getGeboortedatum(), w2.getLand(), w2.getPostcode(), w2.getStad(), w2.getStraat(), w2.getHuisnummer(), w2.getBus(), w2.getEmail(), w2.getStatus())));

        table = new ObservableWerknemersTable(controller);
    }

    @Test
    void lijstWordtCorrectOpgevuld() {
        assertEquals(2, table.getFilteredList().size());
    }

    @Test
    void filterWerktAchternaam() {
        table.changeFilter("jans", "", "", "");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Jansen", table.getFilteredList().get(0).getLastName());
    }

    @Test
    void filterWerktOpVoornaam() {
        table.changeFilter("","jan", "", "");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Jan", table.getFilteredList().get(0).getFirstName());
    }

    @Test
    void filterWerktOpJobtitel() {
        table.changeFilter("","","manager", "");

        assertEquals(1, table.getFilteredList().size());
    }

    @Test
    void filterWerktOpEmail() {
        table.changeFilter("","","","Jan");

        assertEquals(1, table.getFilteredList().size());
    }

    @Test
    void filterVindtGeenOvereenkomst() {
        table.changeFilter("abc", "", "", "");

        assertEquals(0, table.getFilteredList().size());
    }

    @Test
    void legeFilterToontAlles() {
        table.changeFilter("", "", "", "");

        assertEquals(2, table.getFilteredList().size());
    }

    @Test
    void jobTitelWordtLowercaseWeergegeven() throws WerknemerInformationException {
        Team team = null;

        Werknemer w = Werknemer.builder()
                .voornaam("Jan")
                .achternaam("Jansen")
                .jobTitel(JobTitel.MANAGER)
                .geboortedatum(GEBOORTEDATUM)
                .land("België")
                .postcode("1000")
                .stad("Brussel")
                .straat("Kerkstraat")
                .huisnummer(1)
                .build();

        ObservableWerknemer observable = new ObservableWerknemer(new WerknemerDTO(w.getId(), w.getVoornaam(), w.getAchternaam(), w.getJobTitel().name(), w.getTelefoon(), w.getGeboortedatum(), w.getLand(), w.getPostcode(), w.getStad(), w.getStraat(), w.getHuisnummer(), w.getBus(), w.getEmail(), w.getStatus()));

        assertEquals("manager", observable.jobTitelProperty().get());
    }

    @Test
    void alleJobTitelsWordenCorrectGeformatteerd() {
        for (JobTitel jt : JobTitel.values()) {
            WerknemerDTO dto = new WerknemerDTO(
                    1,           // id
                    "Jan",          // voornaam
                    "Jansen",       // achternaam
                    jt.name(),      // jobTitel
                    null,           // telefoon
                    GEBOORTEDATUM,  // geboortedatum
                    "België",       // land
                    "1000",         // postcode
                    "Brussel",      // stad
                    "Kerkstraat",   // straat
                    1,              // huisnummer
                    null,           // bus
                    null,           // email
                    null            // status
            );

            ObservableWerknemer ow = new ObservableWerknemer(dto);

            assertEquals(jt.name().toLowerCase(), ow.jobTitelProperty().get());
        }
    }
}