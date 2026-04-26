package domein;

import dto.TeamDTO;
import repository.GenericDao;
import repository.TeamDao;

import java.util.List;
import java.util.Set;

public class TeamManager {

    private final TeamDao teamRepository;

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

    public Team getTeamById(int id) {
        return this.teamRepository.get(id);
    }

    public void deleteTeam(TeamDTO dto) {
        Team team = teamRepository.get(dto.id());
        if (team == null) {
            throw new IllegalArgumentException("Er is geen team geselecteerd.");
        }

        if (team.getWerknemers() != null && !team.getWerknemers().isEmpty()) {
            throw new IllegalArgumentException("Dit team kan niet worden verwijderd omdat het nog leden bevat.");
        }

        teamRepository.startTransaction();
        try {
            teamRepository.delete(team);
            teamRepository.commitTransaction();
        } catch (Exception ex) {
            teamRepository.rollbackTransaction();
            throw ex;
        }
    }

    public List<Team> getTeamsVanWerknemer(int werknemerId) {
        return teamRepository.getTeamsVanWerknemer(werknemerId);
    }

    public List<Team> getTeamsVanVerantwoordelijke(int werknemerId) {
        return teamRepository.getTeamsVanVerantwoordelijke(werknemerId);
    }
}