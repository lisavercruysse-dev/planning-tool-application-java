package domein;

import repository.GenericDao;
import repository.TeamDao;

import java.util.List;
import java.util.Set;

public class TeamManager {

    private GenericDao<Team> teamRepository;

    public TeamManager(TeamDao teamRepository) {this.teamRepository = teamRepository;}

    public Team addTeam(Werknemer verantwoordelijke, String naam, Site site) {
        Set<String> errors = Team.validate(verantwoordelijke, naam, site);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        boolean bestaatAl = getAllTeams().stream()
                .anyMatch(t -> t.getNaam().trim().equalsIgnoreCase(naam.trim())
                        && t.getSite().equals(site));
        if (bestaatAl) {
            throw new IllegalArgumentException("Een team met deze naam en site bestaat al.");
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

    public List<Team> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams;
    }
}
