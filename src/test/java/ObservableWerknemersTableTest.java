import domein.JobTitel;
import domein.Team;
import domein.Werknemer;
import domein.WerknemerController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sdp.sdp.gui.ObservableWerknemer;
import org.sdp.sdp.gui.ObservableWerknemersTable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObservableWerknemersTableTest {

    @Test
    void lijstWordtCorrectOpgevuld() {
        // Arrange
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        // Act
        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);

        // Assert
        assertEquals(2, table.getFilteredPersonList().size());
    }

    @Test
    void filterWerktAchternaam() {
        // Arrange
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);

        // Act
        table.changeFilter("jans", "Achternaam");

        // Assert
        assertEquals(1, table.getFilteredPersonList().size());
        assertEquals("Jansen", table.getFilteredPersonList().get(0).getLastName());
    }

    @Test
    void filterWerktOpVoornaam() {
        // Arrange
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);

        // Act
        table.changeFilter("jan", "Voornaam");

        // Assert
        assertEquals(1, table.getFilteredPersonList().size());
        assertEquals("Jan", table.getFilteredPersonList().get(0).getFirstName());
    }

    @Test
    void filterWerktOpJobtitel() {
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);

        // Act
        table.changeFilter("manager", "Jobtitel");

        // Assert
        assertEquals(1, table.getFilteredPersonList().size());
    }

    @Test
    void filterWerktOpEmail() {
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        // Act
        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);
        table.changeFilter("Jan", "Email");

        // Assert
        assertEquals(1, table.getFilteredPersonList().size());
    }

    @Test
    void filterVindtGeenOvereenkomst() {
        // Arrange
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        // Act
        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);
        table.changeFilter("abc", "Alle kolommen");

        // Assert
        assertEquals(0, table.getFilteredPersonList().size());
    }

    @Test
    void legeFilterToontAlles() {
        // Arrange
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        // Act
        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);
        table.changeFilter("", "Alle kolommen");

        // Assert
        assertEquals(2, table.getFilteredPersonList().size());
    }

    @Test
    void jobTitelWordtLowercaseWeergegeven() {
        // Arrange
        Team team = null;
        Werknemer werknemer = new Werknemer(
                "Jan",
                "Jansen",
                JobTitel.MANAGER,
                "wachtwoord123",
                team
        );

        ObservableWerknemer observable = new ObservableWerknemer(werknemer);

        // Act
        String jobTitel = observable.jobTitelProperty().get();

        // Assert
        assertEquals("manager", jobTitel);
    }

    @Test
    void alleJobTitelsWordenCorrectGeformatteerd() {
        for (JobTitel jt : JobTitel.values()) {
            Werknemer w = new Werknemer("Jan", "Test", jt, "password123", null);
            ObservableWerknemer ow = new ObservableWerknemer(w);

            assertEquals(jt.name().toLowerCase(), ow.jobTitelProperty().get());
        }
    }
}
