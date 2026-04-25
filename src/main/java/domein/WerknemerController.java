package domein;

import dto.WerknemerInputDTO;
import dto.WerknemerDTO;
import exception.WerknemerInformationException;
import repository.GebruikerDaoJpa;

import java.util.List;

public class WerknemerController {

    private final WerknemerManager werknemerManager;

    public WerknemerController() {
        this.werknemerManager = new WerknemerManager(new GebruikerDaoJpa());
    }

    private WerknemerDTO toDTO(Werknemer w) {
        return new WerknemerDTO(w.getId(), w.getVoornaam(), w.getAchternaam(), w.getJobTitel().name(),
                w.getTelefoon(), w.getGeboortedatum(), w.getLand(), w.getPostcode(), w.getStad(),
                w.getStraat(), w.getHuisnummer(), w.getBus(), w.getEmail(), w.getStatus());
    }

    public WerknemerDTO addWerknemer(WerknemerInputDTO werknemerInputDTO) throws WerknemerInformationException {
        Werknemer w = werknemerManager.addWerknemer(werknemerInputDTO);
        return toDTO(w);
     }
  
    public List<WerknemerDTO> getWerknemers() {
        List<Werknemer> werknemers = werknemerManager.getWerknemerList();
        return werknemers.stream().map(this::toDTO).toList();
    }

    public WerknemerDTO getVerantwoordelijkeVanTeam(int teamId) {
        Werknemer w = werknemerManager.getVerantwoordelijkeVanTeam(teamId);
        return toDTO(w);
    }

    public List<WerknemerDTO> getGewoneWerknemers() {
        List<Werknemer> werknemers = werknemerManager.getGewoneWerknemers();
        return werknemers.stream().map(this::toDTO).toList();
    }

    public List<WerknemerDTO> getWerknemersFromTeam(int id) {
        List<Werknemer> werknemers = werknemerManager.getWerknemersFromTeam(id);
        return werknemers.stream().map(this::toDTO).toList();
    }

    public void wijzigWerknemer(Werknemer werknemer, String nieuweNaam, String nieuweJobTitel) {
        werknemerManager.wijzigWerknemer(werknemer, nieuweNaam, nieuweJobTitel);
    }

    public void verwijderWerknemer(Werknemer werknemer) {
        werknemerManager.verwijderWerknemer(werknemer);
    }

    public List<Werknemer> getVerantwoordelijken() {
        return werknemerManager.getVerantwoordelijken();
    }

    public Werknemer voegWerknemerToeAanTeam(int werknemerId, int teamId) {
        return werknemerManager.voegWerknemerToeAanTeam(werknemerId, teamId);
    }

    public void verwijderWerknemerUitTeam(int werknemerId, int teamId) {
        werknemerManager.verwijderWerknemerUitTeam(werknemerId, teamId);
    }

}
