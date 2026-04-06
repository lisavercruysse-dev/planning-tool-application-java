package org.sdp.sdp.repository;

import org.sdp.sdp.domein.Gebruiker;

public class GebruikerRepositoryJpa extends GenericRepositoryJpa<Gebruiker> implements GebruikerRepository {
    public GebruikerRepositoryJpa() {
        super(Gebruiker.class);
    }
}