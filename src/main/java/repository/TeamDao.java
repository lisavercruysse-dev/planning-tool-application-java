package repository;

import domein.Site;
import domein.Team;

public interface TeamDao extends GenericDao<Team>{

    public Site getSiteFromTeam(int teamId);
}
