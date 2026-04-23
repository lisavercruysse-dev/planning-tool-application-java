package repository;

import domein.Site;
import domein.Team;

import java.util.List;

public class SiteDaoJpa extends GenericDaoJpa<Site> implements SiteDao{
    public SiteDaoJpa() {super(Site.class);
    }

    @Override
    public Site getSiteFromTeam(int teamId) {
        return em.createQuery(
                "SELECT t.site FROM Team t WHERE t.id = :teamId", Site.class
        ).setParameter("teamId", teamId).getResultList().stream().findFirst().orElse(null);
    }
}
