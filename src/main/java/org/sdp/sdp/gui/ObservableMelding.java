package org.sdp.sdp.gui;

import dto.MeldingDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDate;

public class ObservableMelding {

    private final MeldingDTO dto;
    private final BooleanProperty gelezen;

    public ObservableMelding(MeldingDTO dto) {
        this.dto = dto;
        this.gelezen = new SimpleBooleanProperty(dto.gelezen());
    }

    public long getId() {
        return dto.id();
    }

    public String getType() {
        return dto.type();
    }

    public String getTitel() {
        return dto.titel();
    }

    public String getDetail() {
        return dto.detail();
    }

    public LocalDate getDatum() {
        return dto.datum();
    }

    public BooleanProperty gelezenProperty() {
        return gelezen;
    }

    public boolean isGelezen() {
        return gelezen.get();
    }
}
