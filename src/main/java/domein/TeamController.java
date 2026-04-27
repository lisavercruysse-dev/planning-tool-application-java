package domein;

import dto.TeamDTO;
import repository.GenericDaoJpa;
import repository.TeamDaoJpa;

import java.util.List;

public class TeamController {

    private final TeamManager teamManager;

    public TeamController() {
        this.teamManager = new TeamManager(new TeamDaoJpa());
    }

    public TeamDTO addTeam(Werknemer verantwoordelijke, String naam, Site site) {
        Team team = teamManager.addTeam(verantwoordelijke, naam, site);
        return new TeamDTO(team.getId(), team.getNaam());    }

    public List<TeamDTO> getAllTeams(){
        List<Team> teams = teamManager.getAllTeams();
        return teams.stream().map(t -> new TeamDTO(t.getId(), t.getNaam())).toList();
    }

    public Team getTeamByID(int id){
        return teamManager.getTeamById(id);
    }

    public void deleteTeam(TeamDTO team) {
        teamManager.deleteTeam(team);
    }

    public List<TeamDTO> getTeamsVanWerknemer(int werknemerId) {
        List<Team> teams =  teamManager.getTeamsVanWerknemer(werknemerId);
        return teams.stream().map(t -> new TeamDTO(t.getId(), t.getNaam())).toList();
    }

    public List<TeamDTO> getTeamsVanVerantwoordelijke(int werknemerId) {
        List<Team> teams = teamManager.getTeamsVanVerantwoordelijke(werknemerId);
        return teams.stream().map(t -> new TeamDTO(t.getId(), t.getNaam())).toList();
    }
}
