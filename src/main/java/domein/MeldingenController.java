package domein;

import dto.MeldingDTO;
import dto.TeamDTO;
import dto.WerknemerDTO;
import util.MeldingType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MeldingenController {

    private final List<Melding> meldingen = new ArrayList<>();

    public MeldingenController(List<TeamDTO> teams, List<WerknemerDTO> werknemers) {
        laadMeldingen(teams, werknemers);
    }

    private void laadMeldingen(List<TeamDTO> teams, List<WerknemerDTO> werknemers) {
        AtomicLong idTeller = new AtomicLong(1);
        LocalDate vandaag = LocalDate.now();

        for (TeamDTO team : teams) {
            meldingen.add(new Melding(
                    idTeller.getAndIncrement(),
                    MeldingType.TEAM_AANGEMAAKT,
                    "Team aangemaakt",
                    "Het team '" + team.naam() + "' is beschikbaar in het systeem.",
                    vandaag, false));
        }

        for (WerknemerDTO w : werknemers) {
            meldingen.add(new Melding(
                    idTeller.getAndIncrement(),
                    MeldingType.WERKNEMER_TOEGEVOEGD,
                    "Werknemer geregistreerd",
                    String.format("%s %s is geregistreerd als %s.", w.voornaam(), w.achternaam(), w.jobTitel().toLowerCase()),
                    vandaag, false));
        }
    }

    private MeldingDTO toDTO(Melding m) {
        return new MeldingDTO(m.getId(), m.getType().getDisplay(),
                m.getTitel(), m.getDetail(), m.getDatum(), m.isGelezen());
    }

    public List<MeldingDTO> getMeldingen(String statusFilter, String typeFilter) {
        return meldingen.stream()
                .filter(m -> switch (statusFilter) {
                    case "Ongelezen" -> !m.isGelezen();
                    case "Gelezen"   -> m.isGelezen();
                    default          -> true;
                })
                .filter(m -> "Alle types".equals(typeFilter)
                        || m.getType().getDisplay().equals(typeFilter))
                .map(this::toDTO)
                .toList();
    }

    public List<String> getMeldingTypes() {
        return Arrays.stream(MeldingType.values())
                .map(MeldingType::getDisplay)
                .toList();
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
