package domein;

import dto.SiteDTO;
import repository.SiteDaoJpa;

import java.util.List;

public class SiteController {
    private final SiteManager siteManager;

    public SiteController() {
        this.siteManager = new SiteManager(new SiteDaoJpa());
    }

    public List<Site> getAllSites() {
        return siteManager.getAllSites();
    }

    public SiteDTO getSiteFromTeam(int teamId) {
        Site site = siteManager.getSiteFromTeam(teamId);
        return new SiteDTO(site.getId(), site.getName());
    }
}
