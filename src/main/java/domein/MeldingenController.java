package domein;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MeldingenController {

    private final List<Melding> meldingen = new ArrayList<>();

    public MeldingenController() {
        // Mock data
        meldingen.add(new Melding(
                1, MeldingType.TAAK_TOEGEWEZEN, "Nieuwe taak toegewezen",
                "Taak ABC gepland op 5/12/2025",
                LocalDate.of(2025, 5, 12),
                LocalTime.of(14, 0), LocalTime.of(17, 0), false));

        meldingen.add(new Melding(
                2, MeldingType.TAAK_TOEGEWEZEN, "Nieuwe taak toegewezen",
                "Taak DEF gepland op 5/12/2025",
                LocalDate.of(2025, 5, 12),
                LocalTime.of(14, 0), LocalTime.of(17, 0), true));
    }

    public List<Melding> getMeldingen() {
        return List.copyOf(meldingen);
    }

    public void markeerAlsGelezen(long id) {
        meldingen.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .ifPresent(m -> m.setGelezen(true));
    }

    public void markeerAllesAlsGelezen() {
        meldingen.forEach(m -> m.setGelezen(true));
    }

    public void verwijderMelding(long id) {
        meldingen.removeIf(m -> m.getId() == id);
    }

    public long getAantalOngelezen() {
        return meldingen.stream().filter(m -> !m.isGelezen()).count();
    }
}
