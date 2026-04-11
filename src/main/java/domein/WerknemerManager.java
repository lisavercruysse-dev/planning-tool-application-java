package domein;

import repository.GenericDao;

import java.util.Collections;
import java.util.List;

public class WerknemerManager {

    private GenericDao<Werknemer> werknemerRepo;
    private List<Werknemer> werknemerList;

    public WerknemerManager(GenericDao<Werknemer> werknemerDao) {
        werknemerRepo = werknemerDao;
    }

    public void addWerknemer(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        Werknemer w = new Werknemer(voornaam, achternaam, JobTitel.valueOf(jobtitel.toUpperCase()), wachtwoord, team);

        werknemerRepo.startTransaction();
        try {
            werknemerRepo.insert(w);
            werknemerRepo.commitTransaction();
        } catch (Exception ex) {
            werknemerRepo.rollbackTransaction();
            throw ex;
        }

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

    public void closePersistency() {
        werknemerRepo.closePersistency();
    }
}
