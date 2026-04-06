package org.sdp.sdp.domein;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Gebruiker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String naam;
    @Setter
    private String rol;

    protected Gebruiker() {}

    public Gebruiker(String naam, String rol) {
        this.naam = naam;
        this.rol = rol;
    }

}