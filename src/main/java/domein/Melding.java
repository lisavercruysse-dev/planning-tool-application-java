package domein;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class Melding {

    private final long id;
    private final MeldingType type;
    private final String titel;
    private final String detail;
    private final LocalDate datum;
    private final LocalTime beginTijd;
    private final LocalTime eindTijd;

    @Setter
    private boolean gelezen;

    public Melding(long id, MeldingType type, String titel, String detail,
                   LocalDate datum, LocalTime beginTijd, LocalTime eindTijd, boolean gelezen) {
        this.id = id;
        this.type = type;
        this.titel = titel;
        this.detail = detail;
        this.datum = datum;
        this.beginTijd = beginTijd;
        this.eindTijd = eindTijd;
        this.gelezen = gelezen;
    }
}
