package domein;

public enum MeldingType {
    TAAK_TOEGEWEZEN("Taak Toegewezen"),
    TAAK_GEWIJZIGD("Taak Gewijzigd"),
    SYSTEEM("Systeem");

    private final String display;

    MeldingType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    @Override
    public String toString() {
        return display;
    }
}
