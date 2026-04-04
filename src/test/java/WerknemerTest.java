import domein.Werknemer;
import domein.WerknemerManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GenericDao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WerknemerTest {

    @Mock
    private GenericDao<Werknemer> werknemerRepo;

    @InjectMocks
    private WerknemerManager werknemerManager;

    @Test
    public void addWerknemer() {
        final String VOORNAAM = "Thomas";
        final String ACHTERNAAM = "De Bakker";
        final String JOBTITEL = "werknemer";
        final String WACHTWOORD = "12345678";

        Werknemer werknemer = werknemerManager.addWerknemer(VOORNAAM, ACHTERNAAM, JOBTITEL, WACHTWOORD);

        verify(werknemerRepo).startTransaction();
        verify(werknemerRepo).insert(werknemer);
        verify(werknemerRepo).commitTransaction();

        assertEquals("Thomas.DeBakker@example.com", werknemer.getEmail());
    }
}
