package domein;

import dto.TeamDTO;
import dto.TeamInputDTO;
import repository.GebruikerDao;
import repository.GenericDao;
import repository.SiteDao;
import repository.TeamDao;

import java.util.List;
import java.util.Set;

public class TeamManager {

    private final TeamDao teamRepository;
    private final GebruikerDao werknemerRepository;
    private final SiteDao siteRepository;

    public TeamManager(TeamDao teamRepository, GebruikerDao werknemerRepository, SiteDao siteRepository) {
        this.teamRepository = teamRepository;
        this.werknemerRepository = werknemerRepository;
        this.siteRepository = siteRepository;
    }

    public Team addTeam(TeamInputDTO dto) {
        boolean bestaatAl = getAllTeams().stream()
                .anyMatch(t -> t.getNaam().trim().equalsIgnoreCase(dto.name().trim())
                        && t.getSite().getId() == dto.site().id());
        if (bestaatAl) {
            throw new IllegalArgumentException("Een team met deze naam en site bestaat al.");
        }

        Werknemer verantwoordelijke = werknemerRepository.get(dto.verantwoordelijke().id());
        Site site = siteRepository.get(dto.site().id());

        Team t = new Team(verantwoordelijke, dto.name(), site);

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