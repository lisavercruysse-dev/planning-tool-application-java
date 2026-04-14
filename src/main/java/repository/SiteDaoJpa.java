package repository;

import domein.Site;

public class SiteDaoJpa extends GenericDaoJpa<Site> implements SiteDao{
    public SiteDaoJpa() {super(Site.class);
    }
}
