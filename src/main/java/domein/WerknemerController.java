package domein;

import repository.GebruikerDaoJpa;
import repository.GenericDao;
import repository.GenericDaoJpa;

import java.util.List;

public class WerknemerController {

    private final WerknemerManager werknemerManager;

    public WerknemerController() {
        this.werknemerManager = new WerknemerManager(new GebruikerDaoJpa());
    }

    public Werknemer addWerknemer(String voornaam, String achternaam, String jobtitel, String wachtwoord, Team team) {
        return werknemerManager.addWerknemer(voornaam, achternaam, jobtitel, wachtwoord, team);
     }
  
    public List<Werknemer> getWerknemers() {
        return werknemerManager.getWerknemerList();
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
}
