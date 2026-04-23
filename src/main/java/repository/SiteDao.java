package repository;

import domein.Site;

import java.util.List;

public interface SiteDao extends GenericDao<Site>{
    Site getSiteFromTeam(int teamId);
}
