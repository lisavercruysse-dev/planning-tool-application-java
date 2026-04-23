package domein;

import dto.WerknemerInputDTO;
import exception.WerknemerInformationException;
import repository.GebruikerDao;

import java.util.Collections;
import java.util.List;

public class WerknemerManager {

    private GebruikerDao werknemerRepo;
    private List<Werknemer> werknemerList;

    public WerknemerManager(GebruikerDao werknemerDao) {
        werknemerRepo = werknemerDao;
    }

    public Werknemer addWerknemer(WerknemerInputDTO werknemerInputDTO) throws WerknemerInformationException {
            Werknemer w = Werknemer.builder()
                    .voornaam(werknemerInputDTO.voornaam())
                    .achternaam(werknemerInputDTO.achternaam())
                    .jobTitel(werknemerInputDTO.jobTitel())
                    .telefoon(werknemerInputDTO.telefoon())
                    .geboortedatum(werknemerInputDTO.geboortedatum())
                    .land(werknemerInputDTO.land())
                    .postcode(werknemerInputDTO.postcode())
                    .stad(werknemerInputDTO.stad())
                    .straat(werknemerInputDTO.straat())
                    .huisnummer(werknemerInputDTO.huisnummer())
                    .bus(werknemerInputDTO.bus())
                    .team(werknemerInputDTO.team()).build();

        werknemerRepo.startTransaction();
        try {
            werknemerRepo.insert(w);
            werknemerRepo.commitTransaction();
        } catch (Exception ex) {
            werknemerRepo.rollbackTransaction();
            throw ex;
        }
        return w;
    }

      public List<Werknemer> getWerknemerList() {
        List<Werknemer> result = werknemerRepo.findAll();
        return result != null ? result : Collections.emptyList();
    }

    public Werknemer getVerantwoordelijkeVanTeam(int teamId) {
        return werknemerRepo.getVerantwoordelijkeVoorTeam(teamId);
    }

    public void wijzigWerknemer(Werknemer werknemer, String nieuweNaam, String nieuweJobtitel) {
        werknemerRepo.startTransaction();
        try {
            werknemer.setVoornaam(nieuweNaam);
            werknemer.setJobTitel(JobTitel.valueOf(nieuweJobtitel.toUpperCase()));
            werknemerRepo.update(werknemer);
            werknemerRepo.commitTransaction();
        } catch (Exception ex) {
            werknemerRepo.rollbackTransaction();
            throw ex;
        }
    }

    public void verwijderWerknemer(Werknemer werknemer) {
        werknemerRepo.startTransaction();
        try {
            werknemerRepo.delete(werknemer);
            werknemerRepo.commitTransaction();
        } catch (Exception ex) {
            werknemerRepo.rollbackTransaction();
            throw ex;
        }
    }

    public List<Werknemer> getVerantwoordelijken() {
        return werknemerRepo.getVerantwoordelijken();
    }

    public List<Werknemer> getWerknemersFromTeam(int id) {
        return werknemerRepo.getWerknemersFromTeam(id);
    }

    public void closePersistency() {
        werknemerRepo.closePersistency();
    }

    public Werknemer voegWerknemerToeAanTeam(int werknemerId, int teamId) {
        boolean zitAlInTeam = werknemerRepo.get(werknemerId)
                .getTeams()
                .stream()
                .anyMatch(t -> t.getId() == teamId);

        if (zitAlInTeam) {
            throw new IllegalArgumentException("Deze werknemer zit al in dit team");
        }
        werknemerRepo.startTransaction();
        Werknemer w;
        try {
            w = werknemerRepo.voegWerknemerToeAanTeam(werknemerId, teamId);
            werknemerRepo.commitTransaction();
        } catch (Exception ex) {
            werknemerRepo.rollbackTransaction();
            throw ex;
        }
        return w;
    }

    public List<Werknemer> getGewoneWerknemers() {
        return werknemerRepo.getGewoneWerknemers();
    }

    public void verwijderWerknemerUitTeam(int werknemerId, int teamId) {
        werknemerRepo.startTransaction();
        try {
            Werknemer w = werknemerRepo.get(werknemerId);
            Team t = w.getTeams().stream()
                    .filter(team -> team.getId() == teamId)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Werknemer zit niet in dit team"));

            w.getTeams().remove(t);
            t.getWerknemers().remove(w);

            werknemerRepo.commitTransaction();
        } catch (Exception ex) {
            werknemerRepo.rollbackTransaction();
            throw ex;
        }
    }

}
