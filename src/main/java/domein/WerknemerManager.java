package domein;

import repository.GebruikerDao;
import repository.GenericDao;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class WerknemerManager {

    private GebruikerDao werknemerRepo;
    private List<Werknemer> werknemerList;

    public WerknemerManager(GebruikerDao werknemerDao) {
        werknemerRepo = werknemerDao;
    }

    public Werknemer addWerknemer(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        Set<String> errors = Werknemer.validate(voornaam, achternaam, jobtitel, wachtwoord);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        Werknemer w = new Werknemer(voornaam, achternaam, JobTitel.valueOf(jobtitel.toUpperCase()), wachtwoord, team);

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
        System.out.print("lijst werknemers: " + result);
        return result != null ? result : Collections.emptyList();
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
}
