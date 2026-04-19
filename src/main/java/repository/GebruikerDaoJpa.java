package repository;

import domein.Team;
import domein.Werknemer;

import java.util.List;

public class GebruikerDaoJpa extends GenericDaoJpa<Werknemer> implements GebruikerDao{

    public GebruikerDaoJpa() {super(Werknemer.class);}

    @Override
    public List<Werknemer> getVerantwoordelijken() {
        return em.createQuery(
                        "SELECT w FROM Werknemer w WHERE UPPER(w.jobTitel) = UPPER(:jobTitel)",
                        Werknemer.class
                )
                .setParameter("jobTitel", "verantwoordelijke")
                .getResultList();
    }

    @Override
    public List<Werknemer> getWerknemersFromTeam(int id) {
        return em.createQuery(
                "SELECT w FROM Werknemer w JOIN w.teams t WHERE t.id = :teamId", Werknemer.class
        ).setParameter("teamId", id).getResultList();
    }

    @Override
    public Werknemer voegWerknemerToeAanTeam(int werknemerId, int teamId) {
        Werknemer w = em.find(Werknemer.class, werknemerId);
        Team t = em.find(Team.class, teamId);

        if (!w.getTeams().contains(t)) {
            w.getTeams().add(t);
            t.getWerknemers().add(w);
        }
        return w;
    }

    @Override
    public List<Werknemer> getGewoneWerknemers() {
        return em.createQuery(
                "SELECT w FROM Werknemer w WHERE UPPER(w.jobTitel) = UPPER(:jobTitel) ",
                Werknemer.class
        ).setParameter("jobTitel", "werknemer").getResultList();
    }
}
