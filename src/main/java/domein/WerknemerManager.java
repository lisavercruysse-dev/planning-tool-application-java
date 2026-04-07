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
        Werknemer w = new Werknemer(voornaam, achternaam, jobtitel, wachtwoord, team);
        werknemerRepo.startTransaction();
        werknemerRepo.insert(w);
        werknemerRepo.commitTransaction();
        return w;
    }

    public void closePersistency() {
        werknemerRepo.closePersistency();
    }
}
