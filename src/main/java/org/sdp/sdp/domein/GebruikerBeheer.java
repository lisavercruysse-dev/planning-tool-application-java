package org.sdp.sdp.domein;

import org.sdp.sdp.repository.GebruikerRepository;
import org.sdp.sdp.repository.GebruikerRepositoryJpa;

public class GebruikerBeheer {

    private final GebruikerRepository gebruikerRepository;

    public GebruikerBeheer() {
        this.gebruikerRepository = new GebruikerRepositoryJpa();
    }

    public void wijzigGebruiker(Gebruiker gebruiker, String nieuweNaam, String nieuweRol) {
        gebruikerRepository.startTransaction();
        try {
            gebruiker.setNaam(nieuweNaam);
            gebruiker.setRol(nieuweRol);
            gebruikerRepository.update(gebruiker);
            gebruikerRepository.commitTransaction();
        } catch (Exception e) {
            gebruikerRepository.rollbackTransaction();
            throw e;
        }
    }

    public void verwijderGebruiker(Gebruiker gebruiker) {
        gebruikerRepository.startTransaction();
        try {
            gebruikerRepository.delete(gebruiker);
            gebruikerRepository.commitTransaction();
        } catch (Exception e) {
            gebruikerRepository.rollbackTransaction();
            throw e;
        }
    }
}