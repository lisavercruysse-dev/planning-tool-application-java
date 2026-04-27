package domein;

import dto.SiteDTO;
import repository.SiteDaoJpa;

import java.util.List;

public class SiteController {
    private final SiteManager siteManager;

    public SiteController() {
        this.siteManager = new SiteManager(new SiteDaoJpa());
    }

    public Site getSiteById(int id) {
        return siteManager.getSiteById(id);
    }

    public List<SiteDTO> getAllSites() {
        List<Site> sites = siteManager.getAllSites();
        return sites.stream()
                    .map(site -> new SiteDTO(
                            site.getId(),
                            site.getName(),
                            site.getLocatie(),
                            site.getCapaciteit(),
                            site.getOperationeleStatus() != null ? site.getOperationeleStatus().name() : OperationeleStatus.INACTIEF.name(),
                            site.getProductieStatus() != null ? site.getProductieStatus().name() : ProductieStatus.OFFLINE.name()
                    ))
                    .toList();
    }

    public SiteDTO getSiteFromTeam(int teamId) {
        Site site = siteManager.getSiteFromTeam(teamId);
        return new SiteDTO(
                site.getId(),
                site.getName(),
                site.getLocatie(),
                site.getCapaciteit(),
                site.getOperationeleStatus() != null ? site.getOperationeleStatus().name() : OperationeleStatus.INACTIEF.name(),
                site.getProductieStatus() != null ? site.getProductieStatus().name() : ProductieStatus.OFFLINE.name()
        );
    }
}
