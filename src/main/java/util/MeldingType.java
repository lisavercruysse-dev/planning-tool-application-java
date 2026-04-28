package util;

import lombok.Getter;

@Getter
public enum MeldingType {
    TEAM_AANGEMAAKT("Team aangemaakt"),
    WERKNEMER_TOEGEVOEGD("Werknemer toegevoegd"),
    SYSTEEM("Systeem");

    private final String display;

    MeldingType(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
