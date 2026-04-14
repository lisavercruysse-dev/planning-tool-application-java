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
    void filterWerktOpVoornaam() {
        // Arrange
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);

        // Act
        table.changeFilter("jan");

        // Assert
        assertEquals(1, table.getFilteredPersonList().size());
        assertEquals("Jan", table.getFilteredPersonList().get(0).getFirstName());
    }

    @Test
    void filterWerktOpJobTitel() {
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);

        // Act
        table.changeFilter("manager");

        // Assert
        assertEquals(1, table.getFilteredPersonList().size());
    }

    @Test
    void legeFilterToontAlles() {
        WerknemerController controller = mock(WerknemerController.class);

        Werknemer w1 = new Werknemer("Jan", "Jansen", JobTitel.MANAGER, "pass1234", null);
        Werknemer w2 = new Werknemer("Piet", "Peeters", JobTitel.WERKNEMER, "pass1234", null);

        when(controller.getWerknemers()).thenReturn(List.of(w1, w2));

        ObservableWerknemersTable table = new ObservableWerknemersTable(controller);

        // Act
        table.changeFilter("");

        // Assert
        assertEquals(2, table.getFilteredPersonList().size());
    }

    @Test
    void jobTitelWordtLowercaseWeergegeven() {
        // Arrange
        Team team = null; // mag null volgens jouw constructor

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
