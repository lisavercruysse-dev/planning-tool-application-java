import domein.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sdp.sdp.gui.ObservableSitesTable;
import org.sdp.sdp.gui.ObservableWerknemersTable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObservableSitesTableTest {
    @Test
    void lijstWordtCorrectOpgevuld() {
        // Arrange
        SiteController controller = mock(SiteController.class);

        Site s1 = new Site("Site A", "Gent", 100, "Actief", "Lopend");
        Site s2 = new Site("Site B", "Eindhoven", 125, "Inactief", "Gestopt");

        when(controller.getAllSites()).thenReturn(List.of(s1, s2));

        // Act
        ObservableSitesTable table = new ObservableSitesTable(controller);

        // Assert
        assertEquals(2, table.getFilteredSiteList().size());
    }

    @Test
    void filterWerktOpNaam() {
        // Arrange
        SiteController controller = mock(SiteController.class);

        Site s1 = new Site("Site Gent", "Gent", 100, "Actief", "Lopend");
        Site s2 = new Site("Site Eindhoven", "Eindhoven", 125, "Inactief", "Gestopt");

        when(controller.getAllSites()).thenReturn(List.of(s1, s2));

        // Act
        ObservableSitesTable table = new ObservableSitesTable(controller);
        table.changeFilter("Site Gent", "Naam");

        // Assert
        assertEquals(1, table.getFilteredSiteList().size());
        assertEquals("Site Gent", table.getFilteredSiteList().get(0).getName());
    }

    @Test
    void filterWerktOpLocatie() {
        // Arrange
        SiteController controller = mock(SiteController.class);

        Site s1 = new Site("Site A", "Gent", 100, "Actief", "Lopend");
        Site s2 = new Site("Site B", "Eindhoven", 125, "Inactief", "Gestopt");

        when(controller.getAllSites()).thenReturn(List.of(s1, s2));

        // Act
        ObservableSitesTable table = new ObservableSitesTable(controller);
        table.changeFilter("Gent", "Locatie");

        // Assert
        assertEquals(1, table.getFilteredSiteList().size());
        assertEquals("Site A", table.getFilteredSiteList().get(0).getName());
    }

    @Test
    void filterWerktOpOperationeleStatus() {
        // Arrange
        SiteController controller = mock(SiteController.class);

        Site s1 = new Site("Site A", "Gent", 100, "Actief", "Lopend");
        Site s2 = new Site("Site B", "Eindhoven", 125, "Inactief", "Gestopt");

        when(controller.getAllSites()).thenReturn(List.of(s1, s2));

        // Act
        ObservableSitesTable table = new ObservableSitesTable(controller);
        table.changeFilter("Inactief", "Operationele Status");

        // Assert
        assertEquals(1, table.getFilteredSiteList().size());
        assertEquals("Site B", table.getFilteredSiteList().get(0).getName());
    }

    @Test
    void filterWerktOpProductieStatus() {
        // Arrange
        SiteController controller = mock(SiteController.class);

        Site s1 = new Site("Site A", "Gent", 100, "Actief", "Lopend");
        Site s2 = new Site("Site B", "Eindhoven", 125, "Inactief", "Gestopt");

        when(controller.getAllSites()).thenReturn(List.of(s1, s2));

        // Act
        ObservableSitesTable table = new ObservableSitesTable(controller);
        table.changeFilter("Gestopt", "Productie Status");

        // Assert
        assertEquals(1, table.getFilteredSiteList().size());
        assertEquals("Site B", table.getFilteredSiteList().get(0).getName());
    }

    @Test
    void filterVindtGeenOvereenkomst() {
        // Arrange
        SiteController controller = mock(SiteController.class);

        Site s1 = new Site("Site A", "Gent", 100, "Actief", "Lopend");
        Site s2 = new Site("Site B", "Eindhoven", 125, "Inactief", "Gestopt");

        when(controller.getAllSites()).thenReturn(List.of(s1, s2));

        // Act
        ObservableSitesTable table = new ObservableSitesTable(controller);
        table.changeFilter("abc", "Alle kolommen");

        // Assert
        assertEquals(0, table.getFilteredSiteList().size());
    }

    @Test
    void legeFilterToontAlles() {
        // Arrange
        SiteController controller = mock(SiteController.class);

        Site s1 = new Site("Site A", "Gent", 100, "Actief", "Lopend");
        Site s2 = new Site("Site B", "Eindhoven", 125, "Inactief", "Gestopt");

        when(controller.getAllSites()).thenReturn(List.of(s1, s2));

        // Act
        ObservableSitesTable table = new ObservableSitesTable(controller);
        table.changeFilter("", "Alle kolommen");

        // Assert
        assertEquals(2, table.getFilteredSiteList().size());
    }
}
