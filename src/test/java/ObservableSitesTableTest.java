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
    private Site s3;

    @BeforeEach
    void setUp(){
        controller = mock(SiteController.class);

        s1 = Site.builder()
                .name("Site A")
                .locatie("Gent")
                .capaciteit(100)
                .operationeleStatus(OperationeleStatus.ACTIEF)
                .productieStatus(ProductieStatus.GEZOND)
                .build();
        s2 = Site.builder()
                .name("Site B")
                .locatie("Aalst")
                .capaciteit(120)
                .operationeleStatus(OperationeleStatus.INACTIEF)
                .productieStatus(ProductieStatus.OFFLINE)
                .build();
        s3 = Site.builder()
                .name("Site C")
                .locatie("Brussel")
                .capaciteit(150)
                .operationeleStatus(OperationeleStatus.INACTIEF)
                .productieStatus(ProductieStatus.PROBLEMEN)
                .build();

        when(controller.getAllSites()).thenReturn(List.of(
                new SiteDTO(s1.getId(), s1.getName(), s1.getLocatie(), s1.getCapaciteit(), s1.getOperationeleStatus().name(), s1.getProductieStatus().name()),
                new SiteDTO(s2.getId(), s2.getName(), s2.getLocatie(), s2.getCapaciteit(), s2.getOperationeleStatus().name(), s2.getProductieStatus().name()),
                new SiteDTO(s3.getId(), s3.getName(), s3.getLocatie(), s3.getCapaciteit(), s3.getOperationeleStatus().name(), s3.getProductieStatus().name())
                ));

        table = new ObservableSitesTable(controller);
    }

    @Test
    void lijstWordtCorrectOpgevuld() {
        assertEquals(3, table.getFilteredList().size());
    }

    @Test
    void filterWerktOpNaam() {
        table.changeFilter("a", "","","","");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Site A", table.getFilteredList().get(0).getName());
    }

    @Test
    void filterWerktOpLocatie() {
        table.changeFilter("", "Gen","","","");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Site A", table.getFilteredList().get(0).getName());
    }

    @Test
    void filterWerktOpOperationeleStatus() {
        table.changeFilter("","","","inactief", "");

        assertEquals(2, table.getFilteredList().size());
    }

    @Test
    void filterWerktOpProductieStatus() {
        table.changeFilter("","","","","gezond");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Site A", table.getFilteredList().get(0).getName());
    }

    @Test
    void filterWerktOpComboOperationeleEnProductieStatus() {
        table.changeFilter("","","","inactief","offline");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Site B", table.getFilteredList().get(0).getName());

        table.changeFilter("","","","inactief","problemen");

        assertEquals(1, table.getFilteredList().size());
        assertEquals("Site C", table.getFilteredList().get(0).getName());
    }

    @Test
    void filterVindtGeenOvereenkomst() {
        table.changeFilter("abc", "","","","");

        assertEquals(0, table.getFilteredList().size());
    }

    @Test
    void legeFilterToontAlles() {
        table.changeFilter("", "","","","");

        assertEquals(3, table.getFilteredList().size());
    }


}
