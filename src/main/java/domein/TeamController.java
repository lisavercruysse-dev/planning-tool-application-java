package domein;

import repository.GenericDaoJpa;
import repository.TeamDaoJpa;

import java.util.List;

public class TeamController {

    private final TeamManager teamManager;

    public TeamController() {
        this.teamManager = new TeamManager(new TeamDaoJpa());
    }

    public Team addTeam(Werknemer verantwoordelijke, String naam, Site site) {
        return teamManager.addTeam(verantwoordelijke, naam, site);
    }

    public List<Team> getAllTeams(){
        return teamManager.getAllTeams();
    }

    public Team getTeamByID(int id){
        return teamManager.getTeamById(id);
    }

    public void deleteTeam(Team team) {
        teamManager.deleteTeam(team);
    }
}
