package domein;

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
}
