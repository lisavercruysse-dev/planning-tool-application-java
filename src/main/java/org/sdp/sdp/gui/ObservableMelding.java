package org.sdp.sdp.gui;

import domein.Melding;
import domein.MeldingType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalTime;

public class ObservableMelding {

    private final Melding melding;
    private final BooleanProperty gelezen;

    public ObservableMelding(Melding melding) {
        this.melding = melding;
        this.gelezen = new SimpleBooleanProperty(melding.isGelezen());
    }

    public long getId() {
        return melding.getId();
    }

    public MeldingType getType() {
        return melding.getType();
    }

    public String getTitel() {
        return melding.getTitel();
    }

    public String getDetail() {
        return melding.getDetail();
    }

    public LocalTime getBeginTijd() {
        return melding.getBeginTijd();
    }

    public LocalTime getEindTijd() {
        return melding.getEindTijd();
    }

    public BooleanProperty gelezenProperty() {
        return gelezen;
    }

    public boolean isGelezen() {
        return gelezen.get();
    }

    public void setGelezen(boolean value) {
        melding.setGelezen(value);
        gelezen.set(value);
    }
}
