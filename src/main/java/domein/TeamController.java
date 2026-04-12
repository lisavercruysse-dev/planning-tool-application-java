package domein;

import repository.GenericDaoJpa;

public class TeamController {

    private final TeamManager teamManager;

    public TeamController() {
        this.teamManager = new TeamManager(new GenericDaoJpa<Team>(Team.class));
    }

    public Team addTeam(Werknemer verantwoordelijke) {
        return teamManager.addTeam(verantwoordelijke);
    }
}
