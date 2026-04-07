package domein;

import repository.GenericDao;

import java.util.List;

public class WerknemerManager {

    private GenericDao<Werknemer> werknemerRepo;
    private List<Werknemer> werknemerList;

    public WerknemerManager(GenericDao<Werknemer> werknemerDao) {
        werknemerRepo = werknemerDao;
    }

    public Werknemer addWerknemer(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        try {
            Werknemer w = new Werknemer(voornaam, achternaam, jobtitel, wachtwoord, team);
            werknemerRepo.startTransaction();
            werknemerRepo.insert(w);
            werknemerRepo.commitTransaction();
            return w;
        } catch (Exception ex) {
            werknemerRepo.rollbackTransaction();
            throw ex;
        }

    }

    public void wijzigWerknemer(Werknemer werknemer, String nieuweNaam, String nieuweJobtitel) {
        werknemerRepo.startTransaction();
        try {
            werknemer.setVoornaam(nieuweNaam);
            werknemer.setJobTitel(nieuweJobtitel);
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
