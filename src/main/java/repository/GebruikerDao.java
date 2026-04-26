package repository;

import domein.Team;
import domein.Werknemer;

import java.util.List;

public interface GebruikerDao extends GenericDao<Werknemer>{
    public List<Werknemer> getVerantwoordelijken();
    public List<Werknemer> getWerknemersFromTeam(int id);
    public Werknemer voegWerknemerToeAanTeam(int werknemerId, int teamId);
    public List<Werknemer> getGewoneWerknemers();
    public Werknemer getVerantwoordelijkeVoorTeam(int teamId);
}
