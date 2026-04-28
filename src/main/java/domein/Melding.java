package domein;

import lombok.Getter;
import lombok.Setter;
import util.MeldingType;

import java.time.LocalDate;

@Getter
public class Melding {

    private final long id;
    private final MeldingType type;
    private final String titel;
    private final String detail;
    private final LocalDate datum;

    @Setter
    private boolean gelezen;

    public Melding(long id, MeldingType type, String titel, String detail,
                   LocalDate datum, boolean gelezen) {
        this.id = id;
        this.type = type;
        this.titel = titel;
        this.detail = detail;
        this.datum = datum;
        this.gelezen = gelezen;
    }
}
