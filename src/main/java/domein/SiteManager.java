package domein;

import repository.SiteDao;

import java.util.List;

public class SiteManager {
    private SiteDao siteRepo;

    public SiteManager(SiteDao siteRepo) {
        this.siteRepo = siteRepo;
    }

    public Site getSiteById(int id) {
        return siteRepo.get(id);
    }

    public List<Site> getAllSites() {
        List<Site> sites = siteRepo.findAll();
        return sites;
    }

    public Site getSiteFromTeam(int teamId) {
        return siteRepo.getSiteFromTeam(teamId);
    }
}
