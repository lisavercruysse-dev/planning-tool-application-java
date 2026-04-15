package repository;

import domein.Team;
import domein.Werknemer;

public class TeamDaoJpa extends GenericDaoJpa<Team> implements TeamDao{
    public TeamDaoJpa() {
        super(Team.class);
    }
}
