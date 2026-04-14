package repository;

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
}
