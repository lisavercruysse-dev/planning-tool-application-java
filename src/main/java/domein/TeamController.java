package domein;

import dto.TeamDTO;
import dto.TeamInputDTO;
import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;
import repository.SiteDaoJpa;
import repository.TeamDaoJpa;

import java.util.List;

public class TeamController {

    private final TeamManager teamManager;

    public TeamController() {
        this.teamManager = new TeamManager(
                new TeamDaoJpa(),
                new GebruikerDaoJpa(),
                new SiteDaoJpa()

        );
    }

    public TeamDTO addTeam(TeamInputDTO dto) {
        Team team = teamManager.addTeam(dto);
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
