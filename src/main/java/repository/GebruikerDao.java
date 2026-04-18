package repository;

import domein.Werknemer;

import java.util.List;

public interface GebruikerDao extends GenericDao<Werknemer>{
    public List<Werknemer> getVerantwoordelijken();
    public List<Werknemer> getWerknemersFromTeam(int id);
}
