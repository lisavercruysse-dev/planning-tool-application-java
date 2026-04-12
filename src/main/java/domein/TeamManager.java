package domein;

import repository.GenericDao;

import java.util.Set;

public class TeamManager {

    private GenericDao<Team> teamRepository;

    public TeamManager(GenericDao<Team> teamRepository) {this.teamRepository = teamRepository;}

    public Team addTeam(Werknemer verantwoordelijke, String naam, Site site) {
        Set<String> errors = Team.validate(verantwoordelijke, naam, site);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        Team t = new Team(verantwoordelijke, naam, site);

        teamRepository.startTransaction();
        try {
            teamRepository.insert(t);
            teamRepository.commitTransaction();
        } catch (Exception ex) {
            teamRepository.rollbackTransaction();
            throw ex;
        }
        return t;
    }
}
