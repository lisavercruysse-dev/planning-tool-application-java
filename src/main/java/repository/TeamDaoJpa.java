package repository;

import domein.Site;
import domein.Team;
import domein.Werknemer;

public class TeamDaoJpa extends GenericDaoJpa<Team> implements TeamDao{
    public TeamDaoJpa() {
        super(Team.class);
    }

    @Override
    public Site getSiteFromTeam(int teamId) {
        Team team = em.find(Team.class, teamId);
        return team != null ? team.getSite() : null;
    }


}
