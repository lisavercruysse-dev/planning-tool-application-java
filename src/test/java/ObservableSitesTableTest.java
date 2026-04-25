import domein.*;
import dto.SiteDTO;
import org.junit.jupiter.api.BeforeEach;
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
    private SiteController controller;
    private ObservableSitesTable table;
    private Site s1;
    private Site s2;

    @BeforeEach
    void setUp(){
        controller = mock(SiteController.class);

        s1 = Site.builder()
                .name("Plant A")
                .locatie("Gent")
                .capaciteit(100)
                .operationeleStatus("Gezond")
                .productieStatus("Actief")
                .build();
        s2 = Site.builder()
                .name("Plant B")
                .locatie("Aalst")
                .capaciteit(100)
                .operationeleStatus("Gezond")
                .productieStatus("Non-Actief")
                .build();

        when(controller.getAllSites()).thenReturn(List.of(
            new SiteDTO(s1.getId(), s1.getName(), s1.getLocatie(), s1.getCapaciteit(), s1.getOperationeleStatus(), s1.getProductieStatus()),
            new SiteDTO(s2.getId(), s2.getName(), s2.getLocatie(), s2.getCapaciteit(), s2.getOperationeleStatus(), s2.getProductieStatus())));

        table = new ObservableSitesTable(controller);
    }

    @Test
    void lijstWordtCorrectOpgevuld() {
        assertEquals(2, table.getFilteredList().size());
    }

    @Test
    void filterWerktOpNaam() {
        table.changeFilter("a", "","","","");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Plant A", table.getFilteredList().get(0).getName());
    }

    @Test
    void filterWerktOpLocatie() {
        table.changeFilter("", "Gen","","","");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Plant A", table.getFilteredList().get(0).getName());
    }

    @Test
    void filterWerktOpOperationeleStatus() {
        table.changeFilter("","","","Inactief", "");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Site B", table.getFilteredList().get(0).getName());
    }

    @Test
    void filterWerktOpProductieStatus() {
        table.changeFilter("","","","","Gestopt");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Site B", table.getFilteredList().get(0).getName());
    }

    @Test
    void filterVindtGeenOvereenkomst() {
        table.changeFilter("abc", "","","","");

        assertEquals(0, table.getFilteredList().size());
    }

    @Test
    void legeFilterToontAlles() {
        table.changeFilter("", "","","","");

        assertEquals(2, table.getFilteredList().size());
    }


}
