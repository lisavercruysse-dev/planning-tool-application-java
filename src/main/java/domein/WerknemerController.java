package domein;

import repository.GenericDao;
import repository.GenericDaoJpa;

public class WerknemerController {

    private final WerknemerManager werknemerManager;
    public WerknemerController() {
        this.werknemerManager = new WerknemerManager(new GenericDaoJpa<Werknemer>(Werknemer.class) {
        });
    }

    public Werknemer addWerknemer(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        return werknemerManager.addWerknemer(voornaam, achternaam, jobtitel, wachtwoord, team);
    }
}
