package domein;

import repository.GenericDao;
import repository.GenericDaoJpa;

public class WerknemerController {

    private final WerknemerManager werknemerManager;
    public WerknemerController() {
        this.werknemerManager = new WerknemerManager(new GenericDaoJpa<Werknemer>(Werknemer.class) {
        });
    }

    public void addWerknemer(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        werknemerManager.addWerknemer(voornaam, achternaam, jobtitel, wachtwoord, team);
    }

    public void wijzigWerknemer(Werknemer werknemer, String nieuweNaam, String nieuweJobTitel) {
        werknemerManager.wijzigWerknemer(werknemer, nieuweNaam, nieuweJobTitel);
    }

    public void verwijderWerknemer(Werknemer werknemer) {
        werknemerManager.verwijderWerknemer(werknemer);
    }
}
